package ast;

public abstract class BinOP extends Exp {

    private Exp l;
    private Exp r;

    public BinOP(Exp l, Exp r){
        this.l = l;
        this.r = r;
    }

    public void setLeft(Exp n){
        this.l = n;
    }

    public void setRight(Exp n){
        this.r = n;
    }

    public Exp getLeft(){
        return this.l;
    }

    public Exp getRight(){
        return this.r;
    }

}
