package ast;

import visitors.Visitor;

public class Decl extends Def{
    private TType type;

    public Decl(String id, TType type) {
        super (id);
        this.type = type;
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
