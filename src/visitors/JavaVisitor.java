package visitors;

import ast.*;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import langUtil.*;

import java.util.List;
import java.util.Map;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.LinkedHashMap;

public class JavaVisitor extends Visitor {

    private STGroup groupTemplate;
    private ST type, cmd, exp, block;
    private List<ST> funs, params, datas;
    private TyEnv<LocalEnv<SType>> envs;
    private LocalEnv<SType> currentEnv;
    private Set<String> declaredInScope = new HashSet<>();
    private ArrayList<String> javaProgram;

    public JavaVisitor(TyEnv<LocalEnv<SType>> envs){
        groupTemplate = new STGroupFile("./template/java.stg");
        javaProgram = new ArrayList<String>();
        this.envs = envs;
    }

    public void printProg() {
        for (String s : javaProgram) {
            System.out.println(s);
        }
    }

    // recebe um objeto de tipo semântico e retorna a String correspondente
    // essencial para asos que não tem declaração explícita na AST e temos o nome da variavel. 
    // Ex: verificar se a variavel já foi declarada ou não.
    private String mapTypeToJava(SType type) {
        if (type instanceof STyInt) { return "int";}
        if (type instanceof STyFloat) { return "float"; }
        if (type instanceof STyBool) { return "boolean"; }
        if (type instanceof STyChar) { return "char"; }
        if (type instanceof STyData) { return ((STyData) type).getID();  }
        if (type instanceof STyArr) { return mapTypeToJava(((STyArr) type).getType()) + "[]"; }
        if (type instanceof STyFun) {
            STyFun funType = (STyFun)type;
            if(funType.getReturns().length > 1) {return "ArrayList<Object>"; }
            if (funType.getReturns().length == 1) { return mapTypeToJava(funType.getReturns()[0]); }
            return "void";
        }
        return "Object"; 
    }
    // foi necessário criar essa função auxiliar no lugar dos visits TyInt,TyFloat...,para não alterar as asts 
    // e o TypeChecker ( já que seus visit são void ), pois era preciso retornar de alguma forma uma String do tipo - TType -> String
    private String generateTypeString(TType typeNode) { 
        if (typeNode instanceof TyInt) { return "int"; }
        if (typeNode instanceof TyFloat) { return "float"; }
        if (typeNode instanceof TyChar) { return "char"; }
        if (typeNode instanceof TyBool) { return "boolean"; }
        if (typeNode instanceof TyId) { return ((TyId) typeNode).getName(); }
        if (typeNode instanceof TTypeArray) {
            TType baseType = ((TTypeArray) typeNode).getBase();
            return generateTypeString(baseType) + "[]";
        }
        return "Object"; 
    }
    private void visitBinaryExpr(String templateName, Exp left, Exp right) { // add, sub, mul, mod, lt, eq ...
        right.accept(this);
        ST right_expr = this.exp;

        left.accept(this);
        ST left_expr = this.exp;

        ST final_expr = groupTemplate.getInstanceOf(templateName);
        final_expr.add("left_expr", left_expr);
        final_expr.add("right_expr", right_expr);

        this.exp = final_expr;
    }

    @Override
    public void visit(Prog node){
        ST prog = groupTemplate.getInstanceOf("program");

        funs = new ArrayList<ST>();
        datas = new ArrayList<ST>();

        for (Def def : node.getDefs()){
            def.accept(this);
        }

        prog.add("fun_defs", funs);
        prog.add("data_defs", datas);

        javaProgram.add(prog.render());
    }

    @Override
    public void visit(Fun node){
        ST fun = groupTemplate.getInstanceOf("fun_def");
        this.currentEnv = envs.get(node.getName());
        this.declaredInScope.clear();
        if (node.getName().equals("main")){
            fun.add("name", "_main");
        } else {
            fun.add("name", node.getName());
        }

        fun.add("ret_type", mapTypeToJava(this.currentEnv.getType()));
        params = new ArrayList<ST>();
        for (Param p : node.getParams()){
            declaredInScope.add(p.getID());
            p.accept(this);
        }
        fun.add("params", params);

        node.getBody().accept(this);
        fun.add("body", block);
        
        funs.add(fun);
    }
    
    @Override
    public void visit(Param node){
        String typeParam = generateTypeString(node.getType());
        ST param = groupTemplate.getInstanceOf("param_def");
        param.add("type", typeParam);
        param.add("name", node.getID());
        params.add(param);
    }

    @Override
    public void visit(Block node){
        ST blockT = groupTemplate.getInstanceOf("block");
        List<ST> commands = new ArrayList<>();
        for (Cmd cmd : node.getCmds()) {
            cmd.accept(this);
            commands.add(this.cmd);
        }
        blockT.add("commands", commands);
        this.block = blockT ;
    }

    
    @Override
    public void visit (Assign node){
        node.getRhs().accept(this);
        ST rhs = this.exp;

        node.getLhs().accept(this);
        ST lhs = this.exp;

        ST assignTemplate;

        if(node.getLhs() instanceof Var){
            String varName = ((Var) node.getLhs()).getName();
            if (!declaredInScope.contains(varName)) {
                declaredInScope.add(varName);
                assignTemplate = groupTemplate.getInstanceOf("declare_assign_stmt");
                assignTemplate.add("type", mapTypeToJava(currentEnv.get(varName)));
            } else {
                assignTemplate = groupTemplate.getInstanceOf("assign_stmt");
            }
            } 
        else { // se nao for variavel simples
            assignTemplate = groupTemplate.getInstanceOf("assign_stmt");
        }
        assignTemplate.add("lhs", lhs);
        assignTemplate.add("rhs", rhs);
        this.cmd = assignTemplate;
    }

    @Override
    public void visit (Return node){
        List<Exp> exps = node.getExps();
        if (exps.size() <= 1) { // retorno único ou void
            ST returnTpl = groupTemplate.getInstanceOf("single_return");
            if (!exps.isEmpty()) {
                exps.get(0).accept(this);
                returnTpl.add("expr", this.exp);
            }
            this.cmd = returnTpl;
        } 
        else { // múltiplos retornos
            ST returnTpl = groupTemplate.getInstanceOf("multi_return_stmt");
            List<ST> expsL = new ArrayList<>();
            for (Exp e : exps) {
                e.accept(this);
                expsL.add(this.exp);
            }
            returnTpl.add("exprs", expsL);
            this.cmd = returnTpl;
        }
    }

    @Override
    public void visit (CallStmt node){
       
        ST callTpl = groupTemplate.getInstanceOf("call_expr");
        callTpl.add("name", node.getID());


        List<ST> args = new ArrayList<>(); 
        if (node.getArgs() != null) {
            for (Exp arg : node.getArgs()) {
                arg.accept(this); 
                args.add(this.exp);
            }
        } 
        callTpl.add("args", args);   
        ST callExp = callTpl;
        if (node.getReturns().isEmpty()) { // chamadas simples
            ST callStmtTpl = groupTemplate.getInstanceOf("call_stmt");
            callStmtTpl.add("call_expr", callExp);
            this.cmd = callStmtTpl;
        }
        else { // multíplos retornos
            ST multiReturnTpl = groupTemplate.getInstanceOf("multi_return_call");
            String tempListVar = "_result_" + node.getID(); 

            multiReturnTpl.add("temp_list_name", tempListVar);
            multiReturnTpl.add("call_expr", callExp);
                 
            List<ST> assignments = new ArrayList<>();
            for (int i = 0; i < node.getReturns().size(); i ++) {
                String varName = ((Var) node.getReturns().get(i)).getName();
                String varType = mapTypeToJava(currentEnv.get(varName)); 
                ST assignTpl;
                if (!declaredInScope.contains(varName)) {
                    declaredInScope.add(varName);
                    assignTpl = groupTemplate.getInstanceOf("unpack_declare_item");
                    assignTpl.add("type", varType);
                } else {
                    assignTpl = groupTemplate.getInstanceOf("unpack_assign_item");
                    assignTpl.add("type", varType);
                }
                assignTpl.add("var_name", varName);
                assignTpl.add("temp_list_name", tempListVar);
                assignTpl.add("index", i);
                assignments.add(assignTpl);
            }
                multiReturnTpl.add("assignments", assignments);
                this.cmd = multiReturnTpl;
            }
    }
    
    @Override
    public void visit (Call node){
        ST call_expr = groupTemplate.getInstanceOf("call_expr");
        call_expr.add("name", node.getFuncName());
        ArrayList<ST> args = new ArrayList<>();
        if(node.getArgs() != null){
            for(Exp arg_exp : node.getArgs()){
                arg_exp.accept(this); 
                args.add(this.exp);
            }
        }
        call_expr.add("args", args);
        if (node.getIndex() != null) { 
            ST indexAccess = groupTemplate.getInstanceOf("call_index_access");
            indexAccess.add("call", call_expr);
            indexAccess.add("index", node.getIndex());
            this.exp = indexAccess;
        } else {
            this.exp = call_expr;
        }
    }

    @Override
    public void visit (Data node){
        ST data_def = groupTemplate.getInstanceOf("data_def");
        data_def.add("name", node.getName());
        
        ArrayList<ST> fields = new ArrayList<>();
        for(Decl d : node.getDecls()) {
             ST field = groupTemplate.getInstanceOf("field_def");
             String dataType = generateTypeString(d.getType());
             field.add("type", dataType);
             field.add("name", d.getName());
             fields.add(field);
        }
        data_def.add("fields", fields);
        datas.add(data_def);
    }

    @Override public void visit(Add node) { visitBinaryExpr("add_expr", node.getLeft(), node.getRight()); }
    @Override public void visit(Sub node) { visitBinaryExpr("sub_expr", node.getLeft(), node.getRight()); }
    @Override public void visit(Mul node) { visitBinaryExpr("mul_expr", node.getLeft(), node.getRight()); }
    @Override public void visit(Div node) { visitBinaryExpr("div_expr", node.getLeft(), node.getRight()); }
    @Override public void visit(Mod node) { visitBinaryExpr("mod_expr", node.getLeft(), node.getRight()); }
    @Override public void visit(Eq node)  { visitBinaryExpr("equals_expr", node.getLeft(), node.getRight()); }
    @Override public void visit(Neq node) { visitBinaryExpr("neq_expr", node.getLeft(), node.getRight()); }
    @Override public void visit(Lt node)  { visitBinaryExpr("lt_expr", node.getLeft(), node.getRight()); }
    @Override public void visit(And node) { visitBinaryExpr("and_expr", node.getLeft(), node.getRight()); }

    @Override
    public void visit(Neg node){
        ST negTpl = groupTemplate.getInstanceOf("neg_expr");
        node.getExp().accept(this); 
        negTpl.add("expr", this.exp);
        this.exp = negTpl;
    }

    @Override
    public void visit(Var node){
        ST var = groupTemplate.getInstanceOf("var_expr");
        var.add("name", node.getName());
        this.exp = var;
    }


    @Override
    public void visit(NBool node){
        ST literal = groupTemplate.getInstanceOf("literal_expr");
        literal.add("value", node.getValue());
        this.exp = literal;
    }

    @Override
    public void visit(NChar node){
        ST literal = groupTemplate.getInstanceOf("literal_expr");
        literal.add("value", "'" + node.getValue() + "'"); 
        this.exp = literal;
    }

    @Override
    public void visit(NFloat node){
        ST literal = groupTemplate.getInstanceOf("literal_expr");
        literal.add("value", node.getValue() + "f");
        this.exp = literal;
    }

    @Override
    public void visit(NInt node){
        ST literal = groupTemplate.getInstanceOf("literal_expr");
        literal.add("value", node.getValue());
        this.exp = literal;
    }

    @Override
    public void visit(NNot node){}



    @Override
    public void visit(Print node){
        node.getExpr().accept(this); 
        ST print_cmd = groupTemplate.getInstanceOf("print_stmt");
        print_cmd.add("expr", this.exp);
        this.cmd = print_cmd;
    }
       
    @Override
    public void visit(If node){
        node.getCondition().accept(this); 
        ST condition = this.exp;

        node.getThenCmd().accept(this);
        ST then_cmd = this.cmd;

        ST else_cmd = null;
        if(node.getElseCmd() != null){
            node.getElseCmd().accept(this);
            else_cmd = this.cmd;
        }

        ST if_cmd = groupTemplate.getInstanceOf("if");
        if_cmd.add("expr", condition);
        if_cmd.add("thn", then_cmd);
        if(else_cmd != null){
            if_cmd.add("els", else_cmd);
        }
        this.cmd = if_cmd;
    }



    @Override
    public void visit(Cmd node){}

    @Override
    public void visit (ItCondId node){}

    @Override
    public void visit (ItCond node){}

    @Override
    public void visit (ItCondExp node){
        node.getCond().accept(this);
    }

    @Override
    public void visit (Iterate node){
        node.getCondition().accept(this); 
        ST condition = this.exp;
        
        node.getBody().accept(this); 
        ST body = this.cmd; 
        
        ST while_cmd = groupTemplate.getInstanceOf("while");
        while_cmd.add("expr", condition);
        while_cmd.add("stmt", body);
        this.cmd = while_cmd;
    }



    

    @Override
    public void visit (ArrayAccess node){
        node.getArray().accept(this);
        ST base = this.exp;

        node.getIndex().accept(this);
        ST index = this.exp;

        ST arrayAccess = groupTemplate.getInstanceOf("array_access");
        arrayAccess.add("base", base);
        arrayAccess.add("index", index);
        this.exp = arrayAccess;
    }



    @Override
    public void visit (Null node){
        this.exp = groupTemplate.getInstanceOf("null_literal");
    }

    @Override
    public void visit (New node){
        String type = generateTypeString(node.getType());
        if (node.getDimensions() != null && !node.getDimensions().isEmpty()) {
            ST new_array = groupTemplate.getInstanceOf("new_array_expr");
            new_array.add("type", type.replace("[]", "")); // passa o tipo base

            StringBuilder sizes = new StringBuilder();
            for(Exp dimExp : node.getDimensions()){ // multiplas dimensões
                dimExp.accept(this); 
                sizes.append("[").append(this.exp.render()).append("]");
            }
            new_array.add("sizes", sizes.toString());


            this.exp = new_array;
        } else {
            ST new_obj = groupTemplate.getInstanceOf("new_obj_expr");
            new_obj.add("type", type);
            this.exp = new_obj;
        }
    }

    @Override
    public void visit (CmdList node){}

    @Override
    public void visit (Decl node){}



    @Override
    public void visit (ExpList node){}

    @Override
    public void visit(TyInt node) {}

    @Override
    public void visit(TyFloat node) { }

    @Override
    public void visit(TyChar node) { }

    @Override
    public void visit(TyBool node) { }

    @Override
    public void visit(TyId node) { }

    @Override
    public void visit(TTypeArray node){
        node.getBase().accept(this);
        ST aux = groupTemplate.getInstanceOf("array_type");
        aux.add("type", type);
        type = aux;
    }

    @Override
    public void visit(Dot node){
        node.getBase().accept(this);
        ST base_expr = this.exp;

        String field_name = node.getField();
        ST dot_expr = groupTemplate.getInstanceOf("dot_expr");

        dot_expr.add("base", base_expr);
        dot_expr.add("field", field_name);
  
        this.exp = dot_expr;
    }

    // função auxiliar para o read: descobrir o tipo de Lvalue
    private SType getTypeOfLValue(LValue lval) {
        if (lval instanceof Var) {
            String varName = ((Var) lval).getName(); 
            return currentEnv.get(varName);
        }
        if (lval instanceof ArrayAccess) {
            ArrayAccess access = (ArrayAccess) lval;
            SType arrayType = getTypeOfLValue((LValue) access.getArray()); 
            if (arrayType instanceof STyArr) {
                return ((STyArr) arrayType).getType();
            }
        }
        if (lval instanceof Dot) {
            Dot dot = (Dot) lval;
            SType baseType = getTypeOfLValue((LValue) dot.getBase());
            if (baseType instanceof STyData) {
                String dataTypeName = ((STyData) baseType).getID();
                LocalEnv<SType> dataEnv = envs.get(dataTypeName);
                if (dataEnv != null) {
                    return dataEnv.get(dot.getField());
                }
            }
        }
        return STyError.iniSTyError();
    }
    @Override
    public void visit(Read node){
        node.getLValue().accept(this);
        ST lhs = this.exp;

        SType targetType = getTypeOfLValue(node.getLValue());

        String readMethod = "next";
         if (targetType instanceof STyInt) {
            readMethod = "nextInt";
        } else if (targetType instanceof STyFloat) {
            readMethod = "nextFloat";
        } else if (targetType instanceof STyBool) {
            readMethod = "nextBoolean";
        }
        
        ST readTpl = groupTemplate.getInstanceOf("read_stmt");
        readTpl.add("lhs", lhs);
        readTpl.add("read_method", readMethod);
        
        this.cmd = readTpl;
    }

    public void saveProgram() {
        try {
            Path currentDir = Paths.get("").toAbsolutePath();
            Path outputFile = currentDir.resolve("LangProgram.java");

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile.toFile()))) {
                ;
                for (String line : javaProgram) {
                    // System.out.println(line);
                    writer.write(line);
                    writer.newLine();
                }
            }
            System.out.println("Programa Java criado com sucesso!");
            System.out.println("Programa salvo em: " + outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
