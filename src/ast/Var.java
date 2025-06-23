package ast;
import visitors.Visitor;

public class Var extends Exp {
    //TODO: permitir que seja um array

    private String name;

    public Var(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public String toString(){
        //TODO
        return "Var toString to be implemented";
    }

    public void accept(Visitor v){v.visit(this);}  //todas classes concretas precisam do accept

}
