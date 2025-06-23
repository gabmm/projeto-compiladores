package ast;
import visitors.Visitor;

public class Or extends BinOP {

    public Or(Exp l, Exp r){
        super(l, r);
    }

    public String toString(){
        //TO DO
        return "Or toString to be implemented";
    }

    public void accept(Visitor v){v.visit(this);}  //todas classes concretas precisam do accept

}
