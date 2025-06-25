package ast;

import visitors.Visitor;

public class ItCondId extends ItCond {
    private String id;
    private Exp exp;

    public ItCondId(String id, Exp exp) {
        this.id = id;
        this.exp = exp;
    }

    public String getId() { return id; }
    public Exp getExp() { return exp; }

    public void accept(Visitor v) {
        v.visit(this);
    }

    public String toString() {
        return id + ": " + exp;
    }
}
