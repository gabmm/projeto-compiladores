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
import langUtil.SType;
import langUtil.TyEnv;

public class JasminVisitor extends Visitor {

    private STGroup groupTemplate;
    private ST type, cmd, exp;
    private List<ST> funs, params, datas, block;
    private TyEnv<LocalEnv<SType>> envs;
    private LocalEnv<SType> localType;
    private LocalEnv<Integer> localMap;
    private ArrayList<String> jasminProgram;
    private TyEnv<LocalEnv<Integer>> jEnvs;
    private int ltLabel = 0, eqLabel = 0, neqLabel = 0, itLabel = 0, ifLabel = 0;

    public JasminVisitor(TyEnv<LocalEnv<SType>> envs) {
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
        for (Def def : node.getDefs()) {
            def.accept(this);
        }

        prog.add("funs", funs);
        jasminProgram.add(prog.render());
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

    @Override
    public void visit(Mul node) {
        visitBinaryExpr("imul", node.getLeft(), node.getRight());
    }

    @Override
    public void visit(Div node) {
        visitBinaryExpr("imod", node.getLeft(), node.getRight());
    }

    @Override
    public void visit(Sub node) {
        visitBinaryExpr("isub", node.getLeft(), node.getRight());
    }

    @Override
    public void visit(Add node) {
        visitBinaryExpr("iadd", node.getLeft(), node.getRight());
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
        int varNum = this.localMap.get(node.getName());
        varExp.add("num", varNum);
        this.exp = varExp;
    }

    @Override
    public void visit(Fun node) {
        ST fun = groupTemplate.getInstanceOf("fun");
        fun.add("name", node.getName());

        updateLocalEnv(node.getName());
        LinkedHashMap<String, SType> envVars = this.localType.getEnv();

        fun.add("stack_max", "10");
        fun.add("env_vars_qtd", envVars.size());


        if (node.getType().size() > 0) { // aqui será array de Objects
            node.getType().get(0).accept(this);
            fun.add("return", type);
        } else {
            fun.add("return", "V");
        }

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
        ArrayList<ST> cmds = new ArrayList<ST>();
        for (Cmd cmd : node.getCmds()) {
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
    public void visit (ItCondExp node){}

    @Override
    public void visit(Iterate node) {
        ST iterateCmd;
        if (node.getCondition() instanceof ItCondExp) {
            iterateCmd = groupTemplate.getInstanceOf("iterate_cond_exp");
            iterateCmd.add("label", "it" + itLabel++);
            node.getCondition().accept(this);
            iterateCmd.add("exp", this.exp);
            if (node.getBody() instanceof Block) {
                node.getBody().accept(this);
                iterateCmd.add("body", this.block);
            } else {
                node.getBody().accept(this);
                iterateCmd.add("body", this.cmd);
            }
        } else {

        }
    }

    @Override
    public void visit(Return node) {
        ST returnCmd = groupTemplate.getInstanceOf("ireturn");
        node.getExps().get(0).accept(this); // por enquanto retorno single
        returnCmd.add("exp", exp);
        this.cmd = returnCmd;
    }

    @Override
    public void visit(Assign node) {
        // por enquanto, apenas atribuição pra variável comum

        /*
         * Lembre-se: não existe variavel não declarada! o lado esquerdo sempre estará
         * no localMap so preciso buscar o Int mapeado pro nome da var
         */

        ST assignCmd = groupTemplate.getInstanceOf("assign_cmd");
        int varNum = this.localMap.get(((Var) node.getLhs()).getName());
        assignCmd.add("var_num", varNum);
        node.getRhs().accept(this);
        assignCmd.add("exp", exp);
        this.cmd = assignCmd;
    }

    @Override
    public void visit (ArrayAccess node){}

    @Override
    public void visit (Call node){}

    @Override
    public void visit (CallStmt node){}

    @Override
    public void visit (Null node){}

    @Override
    public void visit (New node){}

    @Override
    public void visit (CmdList node){}

    @Override
    public void visit (Decl node){}

    @Override
    public void visit (Data node){}

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
    public void visit(Dot node){}

    @Override
    public void visit(Read node){}

}
