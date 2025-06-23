package ast;
import visitors.Visitor;

public class Div extends BinOP {

    public Div(Exp l, Exp r){
        super(l, r);
    }

    public String toString(){
        //TO DO
        return "Div toString to be implemented";
    }

    public void accept(Visitor v){v.visit(this);}  //todas classes concretas precisam do accept

}
