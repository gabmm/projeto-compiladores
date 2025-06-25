package ast;

import visitors.Visitor;

public abstract class LValue extends Exp {
    public abstract void accept(Visitor v);
}
