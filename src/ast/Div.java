package ast;
import visitors.Visitor;

public class Div extends BinOP {

    public Div(Exp l, Exp r){
        super(l, r);
    }

    public String toString(){
        return "(" + getLeft().toString() + " / " + getRight().toString() + ")";
    }

    public void accept(Visitor v){v.visit(this);}  //todas classes concretas precisam do accept

}
