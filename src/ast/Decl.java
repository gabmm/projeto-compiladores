package ast;

import visitors.Visitor;

public class Decl extends Node {
    private String id;
    private TType type;

    public Decl(String id, TType type) {
        this.id = id;
        this.type = type;
    }

    public String getid() {
        return id;
    }

    public TType getType() {
        return type;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }

    public String toString() {
        return id + " :: " + type;
    }
}
