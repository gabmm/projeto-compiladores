package ast;

import visitors.Visitor;

public class Neq extends Exp {
    private Exp left;
    private Exp right;

    public Neq(Exp left, Exp right) {
        this.left = left;
        this.right = right;
    }

    public Exp getLeft() {
        return left;
    }

    public Exp getRight() {
        return right;
    }

    public String toString() {
        return "(" + left.toString() + " != " + right.toString() + ")";
    }

    public void accept(Visitor v) {
        v.visit(this);
    }
}
