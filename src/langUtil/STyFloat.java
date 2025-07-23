package langUtil;

public class STyFloat extends SType {
    private static STyFloat st = new STyFloat();

    private STyFloat(){}

    public static STyFloat initSTyFloat(){
        return st;
    }

    public boolean match(SType t){
        return t instanceof STyFloat;
    }

    public String toString() {
        return "FLOAT";
    }
}
