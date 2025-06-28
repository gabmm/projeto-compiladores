package ast;

import visitors.Visitor;

public abstract class ItCond extends Cmd {
    public abstract void accept(Visitor v);
}


