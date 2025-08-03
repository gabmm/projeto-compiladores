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
import java.util.TreeMap;

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
    public void visit(Or node){}

    @Override
    public void visit(And node){}

    @Override
    public void visit(Neg node){}

    @Override
    public void visit(Var node){
        ST var = groupTemplate.getInstanceOf("var");
        var.add("name", node.getName()); //Pq ta saindo Var(var)??
        exp = var;
    }

    @Override
    public void visit(Attr node){}

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
        TreeMap<String, SType> vars = local.getEnv();
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
            fun.add("ret_aux", "ArrayList<Object> _returns = new ArrayList<Object>;");
        } else {
            fun.add("ret_type", "void");
        }

        node.getBody().accept(this);
        fun.add("body", block);
        
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
    public void visit(Param node){
        node.getType().accept(this);
        ST param = groupTemplate.getInstanceOf("param_def");
        param.add("type", type);
        param.add("name", node.getID());
        params.add(param);
    }

    @Override
    public void visit(Print node){}

    @Override
    public void visit(If node){}

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
    public void visit (ItCondExp node){}

    @Override
    public void visit (Iterate node){}

    @Override
    public void visit (Return node){
        ArrayList<ST> retExps = new ArrayList<ST>();
        ST retExp;
        for (Exp exp : node.getExps()){

            exp.accept(this);
            retExp = groupTemplate.getInstanceOf("return_exp");
            retExp.add("expr", exp);
            retExps.add(retExp);
        }
        ST retCmd = groupTemplate.getInstanceOf("return");
        retCmd.add("return_exps", retExps);
        cmd = retCmd;
    }

    @Override
    public void visit (Assign node){
        //por enquanto Lvalue é somente VAR
    }

    @Override
    public void visit (ArrayAccess node){}

    @Override
    public void visit (FieldAccess node){}

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
    public void visit(Lt node){}

    @Override
    public void visit(Mod node){}

    @Override
    public void visit(Dot node){}

    @Override
    public void visit(Read node){}
}
