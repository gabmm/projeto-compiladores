package ast;

import visitors.Visitor;

public class ItCondExp extends ItCond {
    private Exp exp;

    public ItCondExp(Exp exp) {
        this.exp = exp;
    }

    public Exp getExp() { return exp; }

    
    public void accept(Visitor v) {
        v.visit(this);
    }


    public String toString() {
        return exp.toString();
    }
}
