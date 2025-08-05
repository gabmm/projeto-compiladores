package langUtil;

public class STyNull extends SType{
    private static STyNull st = new STyNull();

    private STyNull(){}

    public static STyNull initSTyNull(){
        return st;
    }

    public boolean match(SType t){
        return t instanceof STyNull || t instanceof STyData || t instanceof STyArr;
    }

    public String toString() {
        return "NULL";
    }
}
