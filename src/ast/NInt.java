package ast;

import visitors.Visitor;

public class NInt extends Exp {

    private int value;

    public NInt(int value){
        this.value = value;
    }

    public int getValue(){
        return this.value;
    }

    public String toString(){
        return Integer.toString(value);
    }

    public void accept(Visitor v){v.visit(this);}  //todas classes concretas precisam do accept
}
