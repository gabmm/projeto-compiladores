package ast;

import visitors.Visitor;

public class Prog extends Node {
    private Fun[] fs;

    public Prog(Fun[] fs){
        this.fs = fs;
    }

    public Fun[] getFuctions(){
        return this.fs;
    }

    public String toString(){
        //TO DO
        return "Prog toString to be implemented";
    }

    public void accept(Visitor v){v.visit(this);}  //todas classes concretas precisam do accept

}
