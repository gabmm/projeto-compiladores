package ast;

import java.util.List;
import visitors.Visitor;
// usada como comando: f(5);
public class CallStmt extends Cmd {
    private String funcName;
    private List<Exp> args;

    public CallStmt(String funcName, List<Exp> args) {
        this.funcName = funcName;
        this.args = args;
    }

    public String getFuncName() {
        return funcName;
    }

    public List<Exp> getArgs() {
        return args;
    }

   
    public void accept(Visitor v) {
        v.visit(this);
    }

   
    public String toString() {
        String s = funcName + "(";
        for (int i = 0; i < args.size(); i++) {
            s += args.get(i);
            if (i < args.size() - 1) s += ", ";
        }
        return s + ");";
    }
}
