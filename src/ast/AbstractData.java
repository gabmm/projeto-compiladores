package ast;
import java.util.List;
import visitors.Visitor;

public class AbstractData extends Node {
    private String name;
    private List<Def> body; 

    public AbstractData(String name, List<Def> body){
        this.name = name;
        this.body = body;
    }

    public String getName() { return name; }
    public List<Def> getBody() { return body; }

    @Override
    public void accept(Visitor v) { v.visit(this); }

    @Override
    public String toString() {
        return "AbstractData " + name + " { " + body + " }";
    }
}