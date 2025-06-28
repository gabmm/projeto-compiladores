package ast;

import visitors.Visitor;

public class ItCondExp extends ItCond {
    private Exp cond;

    public ItCondExp(Exp cond) {
        this.cond = cond;
    }

    public Exp getCond() {
        return cond;
    }

    @Override
    public String toString() {
        return cond.toString();
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
