package ast;

import visitors.Visitor;

public class TyBool extends TType {
    @Override
    public boolean match(TType t) {
        return t instanceof TyBool;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }

    @Override
    public String toString() {
        return "Bool";
    }
}
