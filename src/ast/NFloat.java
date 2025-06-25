package ast;

import visitors.Visitor;

public class NFloat extends Exp{
    private float value;

    public NFloat ( float value){
        this.value = value;
    }

    public float getValue(){
        return value;
    }
    public String toString(){
        return Float.toString(value);
    }
    public void accept(Visitor v){v.visit(this);}
}
