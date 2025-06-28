package ast;

import visitors.Visitor;

public class Read extends Cmd {
    private final LValue lvalue;

    public Read(LValue lvalue) {
        this.lvalue = lvalue;
    }

    public LValue getLValue() {
        return lvalue;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }

    @Override
    public String toString() {
        return "read " + lvalue.toString();
    }
}
