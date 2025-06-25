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
         StringBuilder sb = new StringBuilder();
         sb.append("Fun ").append(id).append(" : ").append(type.toString()).append(" (");
         for (int i = 0; i < params.length; i++) {
            sb.append(params[i].toString());
            if (i < params.length - 1) sb.append(", ");
         }
        sb.append(") {\n");
        sb.append(body.toString()).append("\n");
        sb.append("}");
        return sb.toString();
    }

    public void accept(Visitor v){v.visit(this);}  //todas classes concretas precisam do accept
}
