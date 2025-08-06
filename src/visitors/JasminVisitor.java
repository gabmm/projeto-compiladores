package visitors;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;

import ast.*;
import langUtil.LocalEnv;
import langUtil.SType;
import langUtil.TyEnv;

public class JasminVisitor extends Visitor {

    private STGroup groupTemplate;
    private ST type, cmd, exp;
    private List<ST> funs, params, datas, block;
    private TyEnv<LocalEnv<SType>> envs;
    private ArrayList<String> jasminProgram;
    private TyEnv<LocalEnv<Integer>> jEnvs;

    public JasminVisitor(TyEnv<LocalEnv<SType>> envs) {
        this.envs = envs;
        jasminProgram = new ArrayList<String>();

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

    @Override
    public void visit(Mul node){}

    @Override
    public void visit(Div node){}

    @Override
    public void visit(Sub node){}

    @Override
    public void visit(Add node){}

    @Override
    public void visit(Eq node){}

    @Override
    public void visit(Neq node){}

    @Override
    public void visit(And node){}

    @Override
    public void visit(Neg node){}

    @Override
    public void visit(Var node){}

    @Override
    public void visit(Fun node) {
        ST fun = groupTemplate.getInstanceOf("fun");
        fun.add("name", node.getName());

        LocalEnv<SType> local = envs.get(node.getName());
        LinkedHashMap<String, SType> envVars = local.getEnv();

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
    public void visit(NBool node){}

    @Override
    public void visit(NChar node){}

    @Override
    public void visit(NFloat node){}

    @Override
    public void visit(NInt node){}

    @Override
    public void visit(NNot node){}

    @Override
    public void visit(Param node){}

    @Override
    public void visit(Print node){}

    @Override
    public void visit(If node){}

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
    public void visit (Iterate node){}

    @Override
    public void visit (Return node){}

    @Override
    public void visit (Assign node){}

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
    public void visit(TyInt node){}

    @Override
    public void visit(TyChar node){}

    @Override
    public void visit(TyBool node){}

    @Override
    public void visit(TyFloat node){}

    @Override
    public void visit(TyId node){}

    @Override
    public void visit(TTypeArray node){}

    @Override
    public void visit(Lt node){}

    @Override
    public void visit(Mod node){}

    @Override
    public void visit(Dot node){}

    @Override
    public void visit(Read node){}

}
