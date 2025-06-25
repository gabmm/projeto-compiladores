package ast;

import visitors.Visitor;

public class If extends Cmd {
    private Exp condition;
    private Cmd thenCmd;
    private Cmd elseCmd; 

    public If(Exp condition, Cmd thenCmd, Cmd elseCmd) {
        this.condition = condition;
        this.thenCmd = thenCmd;
        this.elseCmd = elseCmd;
    }

    public Exp getCondition() {
        return condition;
    }

    public Cmd getThenCmd() {
        return thenCmd;
    }

    public Cmd getElseCmd() {
        return elseCmd;
    }


    public String toString() {
        if (elseCmd != null) {
            return "if (" + condition + ") " + thenCmd + " else " + elseCmd;
        } else {
            return "if (" + condition + ") " + thenCmd;
        }
    }

    public void accept(Visitor v) {
        v.visit(this);
    }
}
