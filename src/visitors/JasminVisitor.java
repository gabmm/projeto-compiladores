package visitors;

import ast.*;

public class JasminVisitor extends Visitor {

    public JasminVisitor(){

    }

    @Override
    public void visit(Prog node){
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
    public void visit(Fun node){}

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
    public void visit(Block node){}

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
