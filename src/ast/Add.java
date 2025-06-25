package ast;
import visitors.Visitor;

public class Add extends BinOP {

    public Add(Exp l, Exp r){
        super(l, r);
    }

    @Override
    public String toString(){
        //TO DO
        return "(" + getLeft().toString() + " + " + getRight().toString() + ")";
    }

    public void accept(Visitor v){v.visit(this);}  //todas classes concretas precisam do accept

}
