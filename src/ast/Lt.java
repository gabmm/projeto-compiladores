package ast;

import visitors.Visitor;

public class Lt extends BinOP {
    public Lt(Exp left, Exp right) {
        super(left, right);
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }

    @Override
    public String toString() {
        return getLeft() + " < " + getRight();
    }
}
