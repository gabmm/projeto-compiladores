package ast;

import visitors.Visitor;

public class Attr extends Node{
    private Var id;
    private Exp e;

    public Attr(Var id, Exp e){
        this.id = id;
        this.e = e;
    }

    public Exp getExp(){
        return this.e;
    }

    public Var getID(){
        return this.id;
    }

    public String toString(){
        return id.toString() + " = " + e.toString();
    }

    public void accept(Visitor v){v.visit(this);}  //todas classes concretas precisam do accept

}
