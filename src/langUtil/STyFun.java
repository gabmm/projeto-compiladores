package langUtil;

public class STyFun extends SType{

    private SType paramTypes[];
    private SType retTypes[];

    public STyFun(SType paramTypes[], SType retTypes[]){
        this.paramTypes = paramTypes;
        this.retTypes = retTypes;
    }

    public SType[] getParams(){
        return this.paramTypes;
    }

    public SType[] getReturns(){
        return this.retTypes;
    }

    public boolean match(SType otherFun){
        if (!(otherFun instanceof STyFun)) return false;

        SType[] otherParams = ((STyFun) otherFun).getParams();
        if (otherParams.length != this.getParams().length) return false;

        SType[] otherRets = ((STyFun) otherFun).getReturns();
        if (otherRets.length != this.getReturns().length) return false;

        for (int i = 0; i < otherParams.length; i++) {
            if (!this.paramTypes[i].match(otherParams[i])) {
                return false;
            }
        }

        for (int i = 0; i < otherRets.length; i++){
            if (!this.retTypes[i].match(otherParams[i])){
                return false;
            }
        }

        return true;
    }

    public String toString(){
        String fString = "PARAMS: ";
        
        for (int i = 0; i < this.paramTypes.length; i++){
            fString += this.paramTypes.toString() + " ";
        }

        fString += "RETURNS: ";

        if (this.retTypes.length == 0){
            fString += "VOID";
        }
        for (int i = 0; i < this.retTypes.length; i++){
            fString += this.paramTypes.toString() + " ";
        }

        return fString;
    }

}
