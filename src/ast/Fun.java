package ast;

import visitors.Visitor;

public class Fun extends Node {

    private String id; //nome da funcao
    private TType type; //tipo de retorno da funcao
    private Param[] params; //parametros da funcao
    private Node body; //bloco da funcao

    public Fun(String id, TType type, Param[] params, Node body){
        this.id = id;
        this.type = type;
        this.params = params;
        this.body = body;
    }

    public String getID(){
        return this.id;
    }

    public TType getType(){
        return this.type;
    }

    public Param[] getParams(){
        return this.params;
    }

    public Node getBody(){
        return this.body;
    }

    public String toString(){
        //TO DO
        return "Fun toString to be implemented";
    }

    public void accept(Visitor v){v.visit(this);}  //todas classes concretas precisam do accept
}
