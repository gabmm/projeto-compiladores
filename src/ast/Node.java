package ast;

import visitors.Visitable;
import visitors.Visitor;

public abstract class Node extends beaver.Symbol implements Visitable {

    public Node(){}

    public int getLine() { return super.getLine(getStart()); }
    public int getCol() { return super.getColumn(getStart()); }
    public abstract void accept(Visitor v);
}
