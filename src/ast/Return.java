package ast;

import visitors.Visitor;
import java.util.List;

public class Return extends Cmd {  

    private List<Exp> exps;  

    public Return(List<Exp> exps) {
        this.exps = exps;
    }

    public List<Exp> getExps() {
        return exps;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }
}
