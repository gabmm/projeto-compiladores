package ast;
import java.util.List;
import visitors.Visitor;

public class Prog extends Node {
    private final List<Def> defs;

    public Prog(List<Def> defs) {
        this.defs = defs;
    }

    public List<Def> getDefs() {
        return defs;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Prog {\n");
        for (Def def : defs) {
            sb.append(def.toString()).append("\n");
        }
        sb.append("}");
        return sb.toString();
    }

    @Override
    public void accept(Visitor v){v.visit(this);}  //todas classes concretas precisam do accept

}
