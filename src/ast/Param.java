package ast;

import visitors.Visitor;

public class Param extends Node {

    private String id;
    private TType type;

    public Param(String id, TType type){
        this.id = id;
        this.type = type;
    }

    public String getID(){
        return this.id;
    }

    public TType getType(){
        return this.type;
    }

    public String toString(){
        //TODO
        return "Param toString to be implemented";
    }

    public void accept(Visitor v){v.visit(this);}  //todas classes concretas precisam do accept
}
