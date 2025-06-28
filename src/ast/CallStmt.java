package ast;

import java.util.List;
import visitors.Visitor;

public class CallStmt extends Cmd {
    private final String id;
    private final List<Exp> args;
    private final List<LValue> returns; 

    public CallStmt(String id, List<Exp> args, List<LValue> returns) {
        this.id = id;
        this.args = args;
        this.returns = returns;
    }

    public String getID() {
        return id;
    }

    public List<Exp> getArgs() {
        return args;
    }

    public List<LValue> getReturns() {
        return returns;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }

    @Override
    public String toString() {
        return id + "(" + args + ")" + (returns.isEmpty() ? "" : " -> <" + returns + ">");
    }
}
