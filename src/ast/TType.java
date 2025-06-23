package ast;

public abstract class TType extends Node {
    
    public TType(){}

    public abstract boolean match(TType t);
}
