package ast;
import visitors.Visitor;
 
public class Print extends Cmd {

    private Exp e; 

    public Print(Exp e){
        this.e  = e;
    }

    public Exp getExpr(){
        return e;
    }

    public String toString(){
        return "print(" + e.toString() + ")";
    }

    public void accept(Visitor v){v.visit(this);}  //todas classes concretas precisam do accept
}