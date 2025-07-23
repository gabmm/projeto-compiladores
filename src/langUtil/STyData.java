package langUtil;

public class STyData extends SType{

    private String id;

    public STyData(String id){
        this.id = id;
    }

    public String getID(){
        return this.id;
    }

    public boolean match(SType t){
        return (t instanceof STyData) && this.id.equals(((STyData)t).getID());
    }

    public String toString() {
        return this.id.toUpperCase();
    }

}
