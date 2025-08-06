package visitors;

import ast.*;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import langUtil.*;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Set;
import java.util.LinkedHashMap;

public class JavaVisitor extends Visitor {

    private STGroup groupTemplate;
    private ST type, cmd, exp;
    private List<ST> funs, params, datas, block;
    private TyEnv<LocalEnv<SType>> envs;
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

    private void setType(SType t){
        if(t instanceof STyInt){
            type = groupTemplate.getInstanceOf("int_type");
        } else if(t instanceof STyBool) {
            type = groupTemplate.getInstanceOf("boolean_type");
        } else if(t instanceof STyFloat) {
            type = groupTemplate.getInstanceOf("float_type");
        } else if(t instanceof STyChar){
            type = groupTemplate.getInstanceOf("char_type");
        } else if(t instanceof STyArr) {

        } else if (t instanceof STyData){

        }
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
    public void visit(Mul node){
        visitBinaryExpr("mul_expr", node.getLeft(), node.getRight());
    }

    @Override
    public void visit(Div node){
        visitBinaryExpr("div_expr", node.getLeft(), node.getRight());
    }

    @Override
    public void visit(Sub node){
        visitBinaryExpr("sub_expr", node.getLeft(), node.getRight());
    }

    @Override
    public void visit(Add node){
        visitBinaryExpr("add_expr", node.getLeft(), node.getRight());
    }

    @Override
    public void visit(Eq node){
        visitBinaryExpr("equals_expr", node.getLeft(), node.getRight());
    }

    @Override
    public void visit(Neq node){
        
    }

    @Override
    public void visit(And node){
        visitBinaryExpr("and_expr", node.getLeft(), node.getRight());
    }

    @Override
    public void visit(Neg node){
        
    }

    @Override
    public void visit(Var node){
        ST var = groupTemplate.getInstanceOf("var");
        var.add("name", node.getName());
        this.exp = var;
    }

    @Override
    public void visit(Fun node){
        ST fun = groupTemplate.getInstanceOf("fun_def");

        if (node.getName().equals("main")){
            fun.add("name", "_main");
        } else {
            fun.add("name", node.getName());
        }

        params = new ArrayList<ST>();
        for (Param p : node.getParams()){
            p.accept(this);
        }

        int paramCounter = params.size();
        fun.add("params", params);

        //adicionar variáveis do ambiente;
        ArrayList<ST> envVars = new ArrayList<ST>();
        LocalEnv<SType> local = envs.get(node.getName());
        LinkedHashMap<String, SType> vars = local.getEnv();
        int counter = 0;

        for (Map.Entry<String, SType> var : vars.entrySet()){
            if (counter++ < paramCounter) continue; //pular parâmetros
            ST instVar = groupTemplate.getInstanceOf("var_inst");
            instVar.add("name", var.getKey());
            setType(var.getValue());
            instVar.add("type", type);
            envVars.add(instVar);
        }

        fun.add("env_vars", envVars);

        if(((STyFun)local.getType()).getReturns().length > 0){
            fun.add("ret_type", "ArrayList<Object>");
            fun.add("ret_aux", "ArrayList<Object> _returns = new ArrayList<Object>();");
        } else {
            fun.add("ret_type", "void");
        }

        node.getBody().accept(this);
        fun.add("body", block);
        
        funs.add(fun);
    }

    @Override
    public void visit(NBool node){
        ST literal = groupTemplate.getInstanceOf("boolean_expr");
        literal.add("value", node.getValue());
        this.exp = literal;
    }

    @Override
    public void visit(NChar node){
        this.exp = groupTemplate.getInstanceOf("var").add("name", "'" + node.getValue() + "'");
    }

    @Override
    public void visit(NFloat node){
        ST literal = groupTemplate.getInstanceOf("float_expr");
        literal.add("value", node.getValue() + "f");
        this.exp = literal;
    }

    @Override
    public void visit(NInt node){
        ST literal = groupTemplate.getInstanceOf("int_expr");
        literal.add("value", node.getValue());
        this.exp = literal;
    }

    @Override
    public void visit(NNot node){}

    @Override
    public void visit(Param node){
        node.getType().accept(this);
        ST param = groupTemplate.getInstanceOf("param_def");
        param.add("type", type);
        param.add("name", node.getID());
        params.add(param);
    }

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
    public void visit(Block node){
        ArrayList<ST> cmds = new ArrayList<ST>();
        for (Cmd cmd : node.getCmds()){
            cmd.accept(this);
            cmds.add(this.cmd);
        }
        block = cmds;
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
    public void visit (Return node){
        ArrayList<ST> retExps = new ArrayList<ST>();
        ST retExp;
        for (Exp exp : node.getExps()){

            exp.accept(this);
            retExp = groupTemplate.getInstanceOf("return_exp");
            retExp.add("expr", this.exp);
            retExps.add(retExp);
        }
        ST retCmd = groupTemplate.getInstanceOf("return");
        retCmd.add("return_exps", retExps);
        cmd = retCmd;
    }

    @Override
    public void visit (Assign node){
        node.getRhs().accept(this);
        ST rhs = this.exp;

        node.getLhs().accept(this); 
        ST lhs = this.exp;

        ST assign_cmd = groupTemplate.getInstanceOf("assign_stmt");
        assign_cmd.add("lhs", lhs);
        assign_cmd.add("rhs", rhs);
        this.cmd = assign_cmd;
    }

    @Override
    public void visit (ArrayAccess node){}

    @Override
    public void visit (Call node){
        ST call_expr = groupTemplate.getInstanceOf("call");
        call_expr.add("name", node.getFuncName());
        ArrayList<ST> args = new ArrayList<>();
        if(node.getArgs() != null){
            for(Exp arg_exp : node.getArgs()){
                arg_exp.accept(this); 
                args.add(this.exp);
            }
        }
        call_expr.add("args", args);

        this.exp = call_expr;
    }

    @Override
    public void visit (CallStmt node){}

    @Override
    public void visit (Null node){}

    @Override
    public void visit (New node){
        node.getType().accept(this);
        ST type_st = this.type;
        if (node.getDimensions() != null && !node.getDimensions().isEmpty()) {
            // É um array: new Int[10]
            // Visita a expressão do tamanho do array
            node.getDimensions().get(0).accept(this);
            ST size_st = this.exp;

            ST new_array = groupTemplate.getInstanceOf("new_array_expr");
            new_array.add("type", type_st);
            new_array.add("size", size_st);
            this.exp = new_array;
        } else {
            ST new_obj = groupTemplate.getInstanceOf("new_obj_expr");
            new_obj.add("type", type_st);
            this.exp = new_obj;
        }
    }

    @Override
    public void visit (CmdList node){}

    @Override
    public void visit (Decl node){}

    @Override
    public void visit (Data node){
        ST data_def = groupTemplate.getInstanceOf("data_def");
        data_def.add("name", node.getName());
        
        ArrayList<ST> fields = new ArrayList<>();
        for(Decl d : node.getDecls()) {
             d.getType().accept(this); 
             ST field = groupTemplate.getInstanceOf("field_def");
             field.add("type", this.type);
             field.add("name", d.getName());
             fields.add(field);
        }
        data_def.add("fields", fields);
        datas.add(data_def);
    }

    @Override
    public void visit (ExpList node){}

    @Override
    public void visit(TyInt node){
        type = groupTemplate.getInstanceOf("int_type");
    }

    @Override
    public void visit(TyChar node){
        type = groupTemplate.getInstanceOf("char_type");
    }

    @Override
    public void visit(TyBool node){
        type = groupTemplate.getInstanceOf("boolean_type");
    }

    @Override
    public void visit(TyFloat node){
        type = groupTemplate.getInstanceOf("float_type");
    }

    @Override
    public void visit(TyId node){
        ST aux = groupTemplate.getInstanceOf("data_type");
        aux.add("data_id", node.getName());
        type = aux;
    }

    @Override
    public void visit(TTypeArray node){
        node.getBase().accept(this);
        ST aux = groupTemplate.getInstanceOf("array_type");
        aux.add("type", type);
        type = aux;
    }

    @Override
    public void visit(Lt node){
        visitBinaryExpr("lt_expr",  node.getLeft(), node.getRight());
    }

    @Override
    public void visit(Mod node){
        visitBinaryExpr("mod_expr", node.getLeft(), node.getRight());
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

    @Override
    public void visit(Read node){}
}
