package ast;
import visitors.Visitor;

public class Eq extends BinOP {

    public Eq(Exp l, Exp r){
        super(l, r);
    }

    public String toString(){
        //TO DO
        return "Eq toString to be implemented";
    }

    public void accept(Visitor v){v.visit(this);}  //todas classes concretas precisam do accept

}
