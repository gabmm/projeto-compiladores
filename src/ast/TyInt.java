package ast;

import visitors.Visitor;

public class TyInt extends TType {
    @Override
    public boolean match(TType t) {
        return t instanceof TyInt;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }

    @Override
    public String toString() {
        return "Int";
    }
}
