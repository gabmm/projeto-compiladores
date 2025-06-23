package visitors;
import ast.*;

public abstract class Visitor { //deve ter um método abstrato de visit para cada classe concreta da ast
    public abstract void visit(Prog e);

    public abstract void visit(Mul e);
    public abstract void visit(Div e);
    public abstract void visit(Sub e);
    public abstract void visit(Add e);

    public abstract void visit(Eq e);
    public abstract void visit(Or e);
    public abstract void visit(And e);

    public abstract void visit(Var e);
    public abstract void visit(Attr e);
    public abstract void visit(Fun e);
    public abstract void visit(NInt e);
    public abstract void visit(Param e);
    public abstract void visit(Print e);
}
