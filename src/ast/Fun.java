package ast;

import visitors.Visitor;
import java.util.List;

public class Fun extends Def {

    private List<TType> type; 
    private List<Param> params;
    private Block body;

    public Fun(String id, List<TType> type, List<Param> params, Block body){
        super(id);
        this.type = type;
        this.params = params;
        this.body = body;
    }

    public List<TType> getType() { return type; }
    public List<Param> getParams() { return params; }
    public Block getBody() { return body; }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Fun ").append(id);
        if (type != null)
            sb.append(" : ").append(type.toString());
        sb.append(" (");
        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i));
            if (i < params.size() - 1) sb.append(", ");
        }
        sb.append(") {\n");
        sb.append(body.toString()).append("\n");
        sb.append("}");
        return sb.toString();
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
