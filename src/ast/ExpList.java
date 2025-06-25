package ast;

import java.util.List;
import visitors.Visitor;

public class ExpList extends Node {
    private List<Exp> expressions;

    public ExpList(List<Exp> expressions) {
        this.expressions = expressions;
    }

    public List<Exp> getExpressions() {
        return expressions;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }

    public String toString() {
        return expressions.toString();
    }
}
