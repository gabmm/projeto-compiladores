package langUtil;

public class STyInt extends SType {

    private static STyInt st = new STyInt();

    private STyInt(){}

    public static STyInt initSTyInt(){
        return st;
    }

    public boolean match(SType t){
        return t instanceof STyInt;
    }

    public String toString() {
        return "INT";
    }

}
