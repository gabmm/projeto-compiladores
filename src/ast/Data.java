package ast;

import java.util.List;
import visitors.Visitor;

public class Data extends Node {
    private String name;
    private List<Node> body;

    public Data(String name, List<Node> body) {
        this.name = name;
        this.body = body;
    }

    public String getName() {
        return name;
    }

    public List<Node> getBody() {
        return body;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }

    public String toString() {
        return "data " + name + " { " + body + " }";
    }
}
