package ast;

import java.util.List;
import visitors.Visitor;

public class Block extends Cmd {
    private List<Cmd> cmds;

    public Block(List<Cmd> cmds) {
        this.cmds = cmds;
    }

    public List<Cmd> getCmds() {
        return cmds;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }

    public String toString() {
        return "{ " + cmds.toString() + " }";
    }
}
