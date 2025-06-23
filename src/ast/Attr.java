package ast;

import visitors.Visitor;

public class Attr {
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
        //TO DO
        return "Attr toString to be implemented";
    }

    public void accept(Visitor v){v.visit(this);}  //todas classes concretas precisam do accept

}
