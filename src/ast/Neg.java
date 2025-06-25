package ast;

import visitors.Visitor;

public class Neg extends Exp {
    private Exp exp;

    public Neg(Exp exp) {
        this.exp = exp;
    }

    public Exp getExp() {
        return exp;
    }

    public String toString() {
        return "(-" + exp.toString() + ")";
    }
    
    public void accept(Visitor v) {
        v.visit(this);
    }
}
