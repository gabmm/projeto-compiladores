package ast;

import visitors.Visitor;

public class TyFloat extends TType {
    @Override
    public boolean match(TType t) {
        return t instanceof TyFloat;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }

    @Override
    public String toString() {
        return "Float";
    }
}
