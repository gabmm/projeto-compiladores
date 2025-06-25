package ast;

import visitors.Visitor;

public abstract class Cmd extends Node {
    public abstract void accept(Visitor v);
}
