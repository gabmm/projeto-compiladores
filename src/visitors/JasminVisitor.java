package visitors;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import ast.*;
import langUtil.LocalEnv;
import langUtil.STyArr;
import langUtil.STyBool;
import langUtil.STyChar;
import langUtil.STyData;
import langUtil.STyFloat;
import langUtil.STyFun;
import langUtil.STyInt;
import langUtil.SType;
import langUtil.TyEnv;

public class JasminVisitor extends Visitor {

    private STGroup groupTemplate;
    private ST type, cmd, exp;
    private List<ST> funs, params, datas;
    private ST block;
    private TyEnv<LocalEnv<SType>> envs;
    private LocalEnv<SType> localType;
    private LocalEnv<Integer> localMap;
    private ArrayList<String> jasminProgram;
    private TyEnv<LocalEnv<Integer>> jEnvs;
    private TypeCheckerVisitor typeChecker;
    private int ltLabel = 0, eqLabel = 0, neqLabel = 0, itLabel = 0, ifLabel = 0;

    public JasminVisitor(TypeCheckerVisitor tc, TyEnv<LocalEnv<SType>> envs) {
        this.typeChecker = tc;
        this.envs = envs;
        this.groupTemplate = new STGroupFile("./template/jasmin.stg");
        this.jasminProgram = new ArrayList<String>();

        // mapeia variáveis pro Jasmin
        this.jEnvs = new TyEnv<LocalEnv<Integer>>();
        int varNum;
        for (Map.Entry<String, LocalEnv<SType>> entry : envs.getEnv().entrySet()) {
            varNum = 0;
            LocalEnv<Integer> env = new LocalEnv<Integer>(entry.getKey(), entry.getValue().getType());
            for (String varName : entry.getValue().getEnv().keySet()) {
                env.put(varName, Integer.valueOf(varNum++));
            }
            jEnvs.put(entry.getKey(), env);
        }

        System.out.println("\nVariaveis mapeadas: ");
        jEnvs.printMap();
    }

    public void printProg() {
        for (String s : jasminProgram) {
            System.out.println(s);
        }
    }

    private void updateLocalEnv(String fName) {
        this.localMap = jEnvs.get(fName);
        this.localType = envs.get(fName);
    }

    @Override
    public void visit(Prog node){
        ST prog = groupTemplate.getInstanceOf("program");

        funs = new ArrayList<ST>();
        datas = new ArrayList<ST>();

        for (Def def : node.getDefs()) {
            if (def instanceof Data) {
                def.accept(this);
            }
            else if (def instanceof Fun){
                def.accept(this);
            }
            
        }

        prog.add("funs", funs);
        prog.add("datas", datas);
        jasminProgram.add(prog.render());
    }

    @Override
    public void visit(Fun node) {
        ST fun = groupTemplate.getInstanceOf("fun");
        fun.add("name", node.getName());

        updateLocalEnv(node.getName());
        LinkedHashMap<String, SType> envVars = this.localType.getEnv();

        fun.add("stack_max", "10");

        int locals = envVars.size();

        if (node.getType().size() > 0) {
            fun.add("return", "[Ljava/lang/Object;"); // aqui será array de Objects
            locals += 2; // duas variáveis auxiliares pro vetor de objetos e pro iterador
        } else {
            fun.add("return", "V");
        }

        fun.add("env_vars_qtd", locals);

        params = new ArrayList<ST>();
        for (Param p : node.getParams()) {
            p.getType().accept(this);
            params.add(type);
        }

        fun.add("params", params);

        node.getBody().accept(this);

        fun.add("block", block);

        funs.add(fun);
    }
     @Override
    public void visit (Data node){
        ST data = groupTemplate.getInstanceOf("data");
        String className = "ProgramaLang$" + node.getName();
        data.add("name", className);
        STyData dataType = this.typeChecker.getDataDefs().get(node.getName());
        for (String fieldName : dataType.getFields().keySet()) {
            ST fieldTemplate = groupTemplate.getInstanceOf("field_def");
            SType fieldType = dataType.getFields().get(fieldName);
            
            fieldTemplate.add("name", fieldName);
            fieldTemplate.add("type_descriptor", mapTypeToDescriptor(fieldType));
            
            data.add("fields", fieldTemplate.render());
        }
        datas.add(data);
    }
    private String mapTypeToDescriptor(SType type) {
        if (type instanceof langUtil.STyInt) return "I";
        if (type instanceof langUtil.STyFloat) return "F";
        if (type instanceof langUtil.STyBool) return "Z";
        if (type instanceof langUtil.STyChar) return "C";
        if (type instanceof langUtil.STyData) return "LProgramaLang$" + ((langUtil.STyData) type).getID() + ";";
        if (type instanceof langUtil.STyArr) return "[" + mapTypeToDescriptor(((langUtil.STyArr) type).getType());
        return "Ljava/lang/Object;";
    }

    @Override
    public void visit(Dot node) {

        STyData baseType = (STyData) typeChecker.typeOf(node.getBase());
        SType fieldType = baseType.getFieldType(node.getField());
        ST getFieldTpl = groupTemplate.getInstanceOf("get_field");
        
        getFieldTpl.add("class_name", "ProgramaLang$" + baseType.getID());
        getFieldTpl.add("field_name", node.getField());
        getFieldTpl.add("type_descriptor", mapTypeToDescriptor(fieldType));
        
        node.getBase().accept(this);
        getFieldTpl.add("base_obj", exp);

        this.exp = getFieldTpl;
    }
     
    @Override
    public void visit (ArrayAccess node){
        STyArr arrayType = (STyArr) typeChecker.typeOf(node.getArray());
        SType elementType = arrayType.getType();
        ST arrayLoadTpl = groupTemplate.getInstanceOf("array_load");
        if (elementType instanceof STyInt || elementType instanceof STyBool || elementType instanceof STyChar) {
                arrayLoadTpl.add("instruction", "iaload"); 
            } else if (elementType instanceof STyFloat) {
                arrayLoadTpl.add("instruction", "faload"); 
            } else { 
                arrayLoadTpl.add("instruction", "aaload"); 
            }
            node.getArray().accept(this);
            arrayLoadTpl.add("array_ref", exp);
            node.getIndex().accept(this);
            arrayLoadTpl.add("index_exp", exp);
            this.exp = arrayLoadTpl;
    }
    private void visitBinaryExpr(String op, Exp left, Exp right) {
        ST binExp = groupTemplate.getInstanceOf("binary_exp");
        binExp.add("op", op);
        left.accept(this);
        binExp.add("left_exp", exp);
        right.accept(this);
        binExp.add("right_exp", exp);
        this.exp = binExp;
    }

    private String checkType(Exp node) {
        SType t = typeChecker.typeOf(node);
        if (t instanceof STyInt) {
            return "i";
        } else {
            return "f";
        }
    }

    @Override
    public void visit(Mul node) {
        visitBinaryExpr(checkType(node) + "mul", node.getLeft(), node.getRight());
    }

    @Override
    public void visit(Div node) {
        visitBinaryExpr(checkType(node) + "mod", node.getLeft(), node.getRight());
    }

    @Override
    public void visit(Sub node) {
        visitBinaryExpr(checkType(node) + "sub", node.getLeft(), node.getRight());
    }

    @Override
    public void visit(Add node) {
        visitBinaryExpr(checkType(node) + "add", node.getLeft(), node.getRight());
    }

    @Override
    public void visit(Eq node) {
        ST eqExp = groupTemplate.getInstanceOf("eq_exp");
        eqExp.add("label", "eq" + eqLabel++);
        node.getLeft().accept(this);
        eqExp.add("left_exp", exp);
        node.getRight().accept(this);
        eqExp.add("right_exp", exp);
        this.exp = eqExp;
    }

    @Override
    public void visit(Neq node) {
        ST neqExp = groupTemplate.getInstanceOf("neq_exp");
        neqExp.add("label", "neq" + neqLabel++);
        node.getLeft().accept(this);
        neqExp.add("left_exp", exp);
        node.getRight().accept(this);
        neqExp.add("right_exp", exp);
        this.exp = neqExp;
    }

    @Override
    public void visit(And node) {
        visitBinaryExpr("iand", node.getLeft(), node.getRight());
    }

    @Override
    public void visit(Neg node) {
        ST negExp = groupTemplate.getInstanceOf("neg_exp");
        node.getExp().accept(this);
        negExp.add("exp", exp);
        this.exp = negExp;
    }

    @Override
    public void visit(Var node) {
        ST varExp = groupTemplate.getInstanceOf("var_access");
        String varName = node.getName();
        int varNum = this.localMap.get(varName);
        varExp.add("num", varNum);
        SType varType = this.localType.get(varName);
        String instruction;
        if (varType instanceof STyInt || varType instanceof STyBool || varType instanceof STyChar) {
            instruction = "iload"; 
        } else if (varType instanceof STyFloat) {
            instruction = "fload"; 
        } else { 
            instruction = "aload"; 
        }
        varExp.add("instruction", instruction);
            this.exp = varExp;
    }

    

    @Override
    public void visit(NBool node) {
        ST boolExp;
        if (node.getValue()) {
            boolExp = groupTemplate.getInstanceOf("boolean_true");
        } else {
            boolExp = groupTemplate.getInstanceOf("boolean_false");
        }
        this.exp = boolExp;
    }

    @Override
    public void visit(NChar node) {
        ST charExp = groupTemplate.getInstanceOf("int_exp");
        // converter Char pra Int e adicionar
        charExp.add("value", node.getValue());
        this.exp = charExp;
    }

    @Override
    public void visit(NFloat node) {
        ST floatExp = groupTemplate.getInstanceOf("float_exp");
        floatExp.add("value", node.getValue());
        this.exp = floatExp;
    }

    @Override
    public void visit(NInt node) {
        ST intExp = groupTemplate.getInstanceOf("int_exp");
        intExp.add("value", node.getValue());
        this.exp = intExp;
    }

    @Override
    public void visit(NNot node) {
        ST notExp = groupTemplate.getInstanceOf("not_exp");
        node.getExp().accept(this);
        notExp.add("exp", this.exp);
        this.exp = notExp;
    }

    @Override
    public void visit(Param node){}

    @Override
    public void visit(Print node) {
        ST printCmd = groupTemplate.getInstanceOf("iprint");
        node.getExpr().accept(this);
        printCmd.add("exp", this.exp);
        this.cmd = printCmd;
    }

    @Override
    public void visit(If node) {
        ST ifCmd = groupTemplate.getInstanceOf("if_cmd");
        ifCmd.add("label", "if" + ifLabel++);
        node.getCondition().accept(this);
        ifCmd.add("exp", this.exp);
        node.getThenCmd().accept(this);
        if (node.getThenCmd() instanceof Block) {
            ifCmd.add("then_cmd", this.block);
        } else {
            ifCmd.add("then_cmd", this.cmd);
        }
        if (node.getElseCmd() != null) {
            node.getElseCmd().accept(this);
            if (node.getElseCmd() instanceof Block) {
                ifCmd.add("else_cmd", this.block);
            } else {
                ifCmd.add("else_cmd", this.cmd);
            }
        }
        this.cmd = ifCmd;
    }

    @Override
    public void visit(Block node) {
        ST blockTemplate = groupTemplate.getInstanceOf("block");
        for (Cmd cmd : node.getCmds()) {
            cmd.accept(this); 
            blockTemplate.add("commands", this.cmd); 
        }
        this.block = blockTemplate;
    }

    @Override
    public void visit(Cmd node){}

    @Override
    public void visit(ItCondId node) {
        node.getCond().accept(this);
    }

    @Override
    public void visit(ItCond node) {
    }

    @Override
    public void visit(ItCondExp node) {
        node.getCond().accept(this);
    }

    @Override
    public void visit(Iterate node) {
        LocalEnv<Integer> aux = localMap.cloneEnv();

        ST iterateCmd;
        int varNum = localMap.getEnv().size();
        node.getCondition().accept(this);

        if (node.getCondition() instanceof ItCondExp) {
            iterateCmd = groupTemplate.getInstanceOf("iterate_cond_int");
            iterateCmd.add("id", this.exp);
            iterateCmd.add("itr", varNum);
        } else {
            String idName = ((ItCondId) node.getCondition()).getID();
            if (localMap.hasKey(idName)) {
                varNum = localMap.get(idName);
            }

            // se vai iterar sobre o length de um array
            if (((ItCondId) node.getCondition()).getCond() instanceof LValue) {
                System.out.println("opaopa");
                iterateCmd = groupTemplate.getInstanceOf("iterate_cond_array");
                iterateCmd.add("id", this.exp);
                iterateCmd.add("itr", varNum);

                // se vai iterar sobre um inteiro
            } else {
                iterateCmd = groupTemplate.getInstanceOf("iterate_cond_int");
                iterateCmd.add("id", this.exp);
                iterateCmd.add("itr", varNum);
            }
        }

        iterateCmd.add("label", "it" + itLabel++);

        node.getBody().accept(this); // block tbm é igual pra todo mundo, vai estar no this.block ou no this.cmd
        if (node.getBody() instanceof Block) {
            iterateCmd.add("body", this.block);
        } else {
            iterateCmd.add("body", this.cmd);
        }

        this.cmd = iterateCmd;

        // reestabelece mapa anterior
        localMap = aux;
        jEnvs.put(localMap.getFunctionID(), aux);
    }

    @Override
    public void visit(Return node) {

        /*
         * 1. Cria um array de objetos, necessita var auxiliar 2. Cria um iterador,
         * necessita outra var auxiliar 3. Inicializa array com size dos retornos 4.
         * Inicializa iterador com 0 5. Para cada retorno: 5.1 Load no array 5.2 Load no
         * iterador 5.3 Expressão 5.4 Cast se for do tipo primitivo 5.5 Salva no vetor
         * na posicao do iterador 5.6 Incrementa iterador 6. Ao final: Load no array
         * retorna array
         */

        ST returnCmd = groupTemplate.getInstanceOf("return_cmd");
        int retSize = node.getExps().size();
        returnCmd.add("size", retSize);
        int varAuxArr = localMap.getEnv().size();
        int varAuxItr = localMap.getEnv().size() + 1;
        returnCmd.add("itr", varAuxItr);
        returnCmd.add("aux", varAuxArr);
        SType[] rTypes = ((STyFun) localType.getType()).getReturns();
        ArrayList<ST> retAuxs = new ArrayList<ST>();
        List<Exp> retExps = node.getExps();
        for (int i = 0; i < rTypes.length; i++) {
            ST retAux = groupTemplate.getInstanceOf("return_cmd_aux");
            retExps.get(i).accept(this);
            retAux.add("itr", varAuxItr);
            retAux.add("aux", varAuxArr);
            retAux.add("exp", this.exp);
            if (rTypes[i] instanceof STyInt || rTypes[i] instanceof STyChar) {
                retAux.add("cast", "invokestatic java/lang/Integer/valueOf(I)Ljava/lang/Integer;");
            } else if (rTypes[i] instanceof STyBool) {
                retAux.add("cast", "invokestatic java/lang/Boolean/valueOf(Z)Ljava/lang/Boolean;");
            } else if (rTypes[i] instanceof STyFloat) {
                retAux.add("cast", "invokestatic java/lang/Float/valueOf(F)Ljava/lang/Float;");
            }
            retAuxs.add(retAux);
        }
        returnCmd.add("rets", retAuxs);
        this.cmd = returnCmd;
    }

    @Override
    public void visit(Assign node) {
        // por enquanto, apenas atribuição pra variável comum

        /*
         * Lembre-se: não existe variavel não declarada! o lado esquerdo sempre estará
         * no localMap so preciso buscar o Int mapeado pro nome da var
         */
        LValue lhs = node.getLhs();
        if(lhs instanceof Var){
            ST assignCmd = groupTemplate.getInstanceOf("assign_cmd");
            String varName = ((Var) lhs).getName();
            int varNum = this.localMap.get(varName);
            assignCmd.add("var_num", varNum);
            SType varType = this.localType.get(varName);
            String instruction;
            if (varType instanceof STyInt || varType instanceof STyBool || varType instanceof STyChar) {
                instruction = "istore"; 
            } else if (varType instanceof STyFloat) {
                instruction = "fstore"; 
            } else { 
                instruction = "astore"; 
            }
            assignCmd.add("instruction", instruction);
            node.getRhs().accept(this);
            assignCmd.add("exp", exp);
            this.cmd = assignCmd;
        } else if(lhs instanceof Dot){
            Dot dot = (Dot) lhs;
            ST field = groupTemplate.getInstanceOf("put_field");
            STyData baseType = (STyData) typeChecker.typeOf(dot.getBase());
            SType fieldType = baseType.getFieldType(dot.getField());
            field.add("class_name", "ProgramaLang$" + baseType.getID());
            field.add("field_name", dot.getField());
            field.add("type_descriptor", mapTypeToDescriptor(fieldType));
            dot.getBase().accept(this);
            field.add("base_obj", exp);
            node.getRhs().accept(this);
            field.add("value_exp", exp);
            this.cmd = field;
        }else if (lhs instanceof ArrayAccess) {
            ArrayAccess accessNode = (ArrayAccess) lhs;
            accessNode.getArray().accept(typeChecker);
            STyArr arrayType = (STyArr) typeChecker.getOperands().pop();
            SType elementType = arrayType.getType();

            ST arrayStoreTpl = groupTemplate.getInstanceOf("array_store");

            if (elementType instanceof STyInt || elementType instanceof STyBool || elementType instanceof STyChar) {
                arrayStoreTpl.add("instruction", "iastore");
            } else if (elementType instanceof STyFloat) {
                arrayStoreTpl.add("instruction", "fastore");
            } else {
                arrayStoreTpl.add("instruction", "aastore");
            }

            accessNode.getArray().accept(this);
            arrayStoreTpl.add("array_ref", exp);

            accessNode.getIndex().accept(this);
            arrayStoreTpl.add("index_exp", exp);
            
            node.getRhs().accept(this);
            arrayStoreTpl.add("value_exp", exp);

            this.cmd = arrayStoreTpl;
        }
        
    }

    @Override
    public void visit(Call node) {
        ST callExp = groupTemplate.getInstanceOf("call");

        // nome da funcao
        callExp.add("name", localType.getFunctionID());

        // parâmetros
        ArrayList<ST> args = new ArrayList<ST>();
        String argTypes = "";
        for (Exp e : node.getArgs()) {
            e.accept(this);
            args.add(this.exp);
            argTypes += mapTypeToDescriptor(typeChecker.typeOf(e));
        }

        callExp.add("args", args);
        callExp.add("arg_types", argTypes);

        // assinatura do retorno
        if (((STyFun) localType.getType()).getReturns().length > 0) {
            callExp.add("return", "[Ljava/lang/Object;");
        } else {
            callExp.add("return", "V");
            return;
        }

        /*
         * Até esse ponto, Call e CallStmt são iguais. O retorno é um vetor e está no
         * topo da pilha Para call, devemos montar um acesso a vetor usando o índice do
         * nó call Para call Stmt, devemos iterar o vetor do topo da pilha e ir
         * atribuindo os valores do acesso ao vetor as variáveis descritas no nó
         */

        node.getIndex().accept(this);
        ST singleRet = groupTemplate.getInstanceOf("return_single_access");
        singleRet.add("exp", this.exp);
        callExp.add("return", singleRet);
        this.exp = callExp;
    }

    @Override
    public void visit(CallStmt node) {
        ST callCmd = groupTemplate.getInstanceOf("call_cmd");

        // nome da funcao
        callCmd.add("name", localType.getFunctionID());

        // parâmetros
        ArrayList<ST> args = new ArrayList<ST>();
        String argTypes = "";
        for (Exp e : node.getArgs()) {
            e.accept(this);
            args.add(this.exp);
            argTypes += mapTypeToDescriptor(typeChecker.typeOf(e));
        }

        callCmd.add("args", args);
        callCmd.add("arg_types", argTypes);

        // assinatura do retorno
        if (((STyFun) localType.getType()).getReturns().length > 0) {
            callCmd.add("return", "[Ljava/lang/Object;");
        } else {
            callCmd.add("return", "V");
            return;
        }

        /*
         * Até esse ponto, Call e CallStmt são iguais. O retorno é um vetor e está no
         * topo da pilha Para call, devemos montar um acesso a vetor usando o índice do
         * nó call Para call Stmt, devemos iterar o vetor do topo da pilha e ir
         * atribuindo os valores do acesso ao vetor as variáveis descritas no nó
         */

        ST multiRet = groupTemplate.getInstanceOf("return_multi_access");
        int varAuxArr = localMap.getEnv().size();
        int varAuxItr = localMap.getEnv().size() + 1;
        multiRet.add("aux", varAuxArr);
        multiRet.add("itr", varAuxItr);
        ArrayList<ST> multiRets = new ArrayList<ST>();
        for (LValue l : node.getReturns()) {
            int varNum = localMap.get(((Var) l).getName()); // por enquanto só variavel simples
            ST multiRetAux = groupTemplate.getInstanceOf("return_multi_aux");
            multiRetAux.add("aux", varAuxArr);
            multiRetAux.add("itr", varAuxItr);
            ST varAccess = groupTemplate.getInstanceOf("var_access");
            // POR ENQUANTO SÓ INTEIRO
            SType varType = localType.get(((Var) l).getName());
            if (varType instanceof STyInt) {
                varAccess.add("instruction", "istore");
            }
            varAccess.add("num", varNum);
            multiRetAux.add("var_access", varAccess);
            multiRets.add(multiRetAux);
        }
        multiRet.add("attr", multiRets);
        this.cmd = multiRet;
    }

    @Override
    public void visit(Null node) {
        ST nullExp = groupTemplate.getInstanceOf("null_exp");
        this.exp = nullExp;
    }

    @Override
    public void visit (New node){
        SType creationType = typeChecker.typeOf(node);

        if (creationType instanceof STyData) {
            STyData dataType = (STyData) creationType;
            ST newObjectTpl = groupTemplate.getInstanceOf("new_object");
            
            String className = "ProgramaLang$" + dataType.getID();
            newObjectTpl.add("class_name", className);
            
            this.exp = newObjectTpl;
        }
        else if (creationType instanceof STyArr) {
            STyArr arrayType = (STyArr) creationType;
            SType elementType = arrayType.getType(); 
            
            ST newArrayTpl = groupTemplate.getInstanceOf("new_array");

            node.getDimensions().get(0).accept(this);
            newArrayTpl.add("size_exp", this.exp);

            if (elementType instanceof STyInt || elementType instanceof STyBool || elementType instanceof STyChar) {
                newArrayTpl.add("array_instruction", "newarray int");
            } else if (elementType instanceof STyFloat) {
                newArrayTpl.add("array_instruction", "newarray float");
            } else {
                newArrayTpl.add("array_instruction", "anewarray " + mapTypeToDescriptor(elementType));
            }
            
            this.exp = newArrayTpl;
        }
    }

    @Override
    public void visit (CmdList node){}

    @Override
    public void visit (Decl node){}

    @Override
    public void visit (ExpList node){}

    @Override
    public void visit(TyInt node) {
        ST intType = groupTemplate.getInstanceOf("int_type");
        this.type = intType;
    }

    @Override
    public void visit(TyChar node) {
        ST intType = groupTemplate.getInstanceOf("int_type");
        this.type = intType;
    }

    @Override
    public void visit(TyBool node) {
        ST boolType = groupTemplate.getInstanceOf("boolean_type");
        this.type = boolType;
    }

    @Override
    public void visit(TyFloat node) {
        ST floatType = groupTemplate.getInstanceOf("float_type");
        this.type = floatType;
    }

    @Override
    public void visit(TyId node){}

    @Override
    public void visit(TTypeArray node){}

    @Override
    public void visit(Lt node) {
        ST ltExp = groupTemplate.getInstanceOf("lt_exp");
        ltExp.add("label", "lt" + ltLabel++);
        node.getRight().accept(this);
        ltExp.add("right_exp", exp);
        node.getLeft().accept(this);
        ltExp.add("left_exp", exp);
        this.exp = ltExp;
    }

    @Override
    public void visit(Mod node) {
        visitBinaryExpr("irem", node.getLeft(), node.getRight());
    }



    @Override
    public void visit(Read node){}

}
