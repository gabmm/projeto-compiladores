package langUtil;

public class STyArr extends SType {
    private SType type;
    private int dimensions;

    public STyArr(SType type){
        this.type = type;
        this.dimensions = 1;
    }

    public STyArr(SType type, int dimensions) {
        this.type = type;
        this.dimensions = dimensions;
    }

    public SType getType(){
        return this.type;
    }

    //tipo comparado tem que ser da classe array com a variavel do tipo igual
    public boolean match(SType t){
        return t instanceof STyNull || (t instanceof STyArr && this.type.match(((STyArr) t).getType()));
    }

    public String toString() {
        String baseType = this.type instanceof STyData ? ((STyData) this.type).getID() : this.type.toString();
        return baseType + "[]";
    }
}

