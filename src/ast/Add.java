package ast;
import visitors.Visitor;

public class Add extends BinOP {

    public Add(Exp l, Exp r){
        super(l, r);
    }

    public String toString(){
        //TO DO
        return "Add toString to be implemented";
    }

    public void accept(Visitor v){v.visit(this);}  //todas classes concretas precisam do accept

}
