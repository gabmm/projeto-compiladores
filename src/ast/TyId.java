package ast;

import visitors.Visitor;

public class TyId extends TType {
    private String name;

    public TyId(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean match(TType t) {
        return t instanceof TyId && name.equals(((TyId) t).name);
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }

    @Override
    public String toString() {
        return name;
    }
}
