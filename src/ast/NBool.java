package ast;

import visitors.Visitor;

public class NBool extends Exp {
    private boolean value;

    public NBool(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    public String toString() {
        return Boolean.toString(value);
    }

    public void accept(Visitor v) {
        v.visit(this);
    }
}
