package ast;

import visitors.Visitor;

public class Mod extends BinOP {
    public Mod(Exp left, Exp right) {
        super(left, right);
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }

    @Override
    public String toString() {
        return getLeft() + " % " + getRight();
    }
}
