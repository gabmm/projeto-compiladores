package langUtil;

public class STyError extends SType{

    private static STyError st = new STyError();

    private STyError(){

    }

    public static STyError iniSTyError(){
        return st;
    }

    public boolean match(SType t){
        return true;
    }

    public String toString() {
        return "ERROR";
    }


}
