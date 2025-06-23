package ast;

public abstract class Node extends beaver.Symbol {

    public Node(){}

    public int getLine() { return super.getLine(getStart()); }
    public int getCol() { return super.getColumn(getStart()); }
    
}
