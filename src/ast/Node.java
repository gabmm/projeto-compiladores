package ast;

import visitors.Visitor;

public abstract class Node extends beaver.Symbol {

    public Node(){}

    public int getLine() { return super.getLine(getStart()); }
    public int getCol() { return super.getColumn(getStart()); }
    public abstract void accept(Visitor v);
}
