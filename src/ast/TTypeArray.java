package ast;

import visitors.Visitor;

public class TTypeArray extends TType {
    private TType base;

    public TTypeArray(TType base) {
        this.base = base;
    }

    public TType getBase() {
        return base;
    }

    @Override
    public boolean match(TType t) {
        return t instanceof TTypeArray && base.match(((TTypeArray) t).base);
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }

    @Override
    public String toString() {
        return base.toString() + "[]";
    }
}
