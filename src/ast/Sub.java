package ast;
import visitors.Visitor;

public class Sub extends BinOP {

    public Sub(Exp l, Exp r){
        super(l, r);
    }

    public String toString(){
        //TO DO
        return "Sub toString to be implemented";
    }

    public void accept(Visitor v){v.visit(this);}  //todas classes concretas precisam do accept

}
