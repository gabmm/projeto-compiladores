package ast;
import visitors.Visitor;

public class Var extends Exp {

    private String name;

    public Var(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public String toString(){
        return "Var(" + name + ")";
    }

    public void accept(Visitor v){v.visit(this);}  //todas classes concretas precisam do accept

}
