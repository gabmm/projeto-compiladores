package ast;
import visitors.Visitor;
 
public class Print extends Node {

    private Exp e; 

    public Print(Exp e){
        this.e  = e;
    }

    public Exp getExpr(){
        return e;
    }

    public String toString(){
        //TODO
        return "Print toString to be implemented"; 
    }

    public void accept(Visitor v){v.visit(this);}  //todas classes concretas precisam do accept
}