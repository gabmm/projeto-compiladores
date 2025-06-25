package ast;

import visitors.Visitor;

public class Iterate extends Cmd {
    private ItCond condition;  
    private Cmd body;          

    public Iterate(ItCond condition, Cmd body) {
        this.condition = condition;
        this.body = body;
    }

    public ItCond getCondition() {
        return condition;
    }

    public Cmd getBody() {
        return body;
    }


    public void accept(Visitor v) {
        v.visit(this);
    }


    public String toString() {
        return "iterate (" + condition + ") " + body;
    }
}
