package ast;

import visitors.Visitor;

public class Prog extends Node {
    private Fun[] fs;

    public Prog(Fun[] fs){
        this.fs = fs;
    }

    public Fun[] getFuctions(){
        return this.fs;
    }

    public String toString(){
         StringBuilder sb = new StringBuilder();
         sb.append("Prog{\n");
         for (Fun f : fs) {
             sb.append(f.toString()).append("\n");
         }
         sb.append("}");
         return sb.toString();
    }

    public void accept(Visitor v){v.visit(this);}  //todas classes concretas precisam do accept

}
