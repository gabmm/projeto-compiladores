package ast;

import visitors.Visitor;

public abstract class Exp extends Node{

    public Exp(){}
    public abstract void accept(Visitor v);
}
