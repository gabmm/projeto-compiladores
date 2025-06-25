package ast;

import visitors.Visitor;

public class NChar extends Exp{
    private char value;

    public NChar ( char value){
        this.value = value;
    }

    public char getValue(){
        return value;
    }
    public String toString() {
        return "'" + value + "'";
    }
    public void accept(Visitor v){v.visit(this);}
}
