package ast;

import visitors.Visitor;

public abstract class TType extends Node {
    
    public TType(){}

    public abstract boolean match(TType t);
    public abstract void accept(Visitor v);
}
