package ast;

import visitors.Visitor;

public class TyChar extends TType {
    @Override
    public boolean match(TType t) {
        return t instanceof TyChar;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }

    @Override
    public String toString() {
        return "Char";
    }
}
