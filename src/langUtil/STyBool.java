package langUtil;

public class STyBool extends SType {
    private static STyBool st = new STyBool();

    private STyBool(){}

    public static STyBool initSTyBool(){
        return st;
    }

    public boolean match(SType t){
        return t instanceof STyBool;
    }
}
