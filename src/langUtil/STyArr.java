package langUtil;

public class STyArr extends SType {
    private SType type;

    public STyArr(SType type){
        this.type = type;
    }

    public SType getType(){
        return type;
    }

    //tipo comparado tem que ser da classe array com a variavel do tipo igual
    public boolean match(SType t){
        return (t instanceof STyArr) &&  this.type.match(((STyArr)t).getType());
    }
}

