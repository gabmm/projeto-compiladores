package ast;

import visitors.Visitor;

public class ItCondId extends ItCond {
    private String id;
    private Exp cond;

    public ItCondId(String id, Exp cond) {
        this.id = id;
        this.cond = cond;
    }

    public String getID() {
        return id;
    }

    public Exp getCond() {
        return cond;
    }

    @Override
    public String toString() {
        return id + " : " + cond;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
