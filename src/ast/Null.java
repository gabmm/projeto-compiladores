package ast;

import visitors.Visitor;

public class Null extends Exp {
    public void accept(Visitor v) {
        v.visit(this);
    }

    public String toString() {
        return "null";
    }
}
