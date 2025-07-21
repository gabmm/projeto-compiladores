package langUtil;

public class STyChar extends SType{
    private static STyChar st = new STyChar();

    private STyChar(){}

    public static STyChar initSTyChar(){
        return st;
    }

    public boolean match(SType t){
        return t instanceof STyChar;
    }
}
