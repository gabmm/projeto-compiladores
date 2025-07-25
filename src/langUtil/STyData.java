package langUtil;

import java.util.LinkedHashMap;

public class STyData extends SType{

    private String id;
    private LinkedHashMap<String, SType> fields;

    public STyData(String id, LinkedHashMap<String, SType> fields) {
        this.id = id;
        this.fields = fields;
    }

    public Boolean hasField(String fieldName) {
        return this.fields.containsKey(fieldName);
    }

    public SType getFieldType(String fieldName) {
        return this.fields.get(fieldName); // caso não exista, retorna null
    }

    public LinkedHashMap<String, SType> getFields() {
        return this.fields;
    }

    public String getID(){
        return this.id;
    }

    public boolean match(SType t){
        // é do tipo Data e tem o mesmo nome? acho q nem dá pra ter nome diferente
        if (!(t instanceof STyData) && this.id.equals(((STyData) t).getID())) {
            return false;
        }

        // tem o mesmo número de campos?
        STyData otherData = (STyData) t;
        if (otherData.getFields().size() != this.fields.size()) {
            return false;
        }

        // os campos tem o mesmo tipo?
        for (String key : this.fields.keySet()) {
            if (!otherData.hasField(key) || this.getFieldType(key).match(otherData.getFieldType(key))) {
                return false;
            }
        }

        return true;
    }

    public String toString() {
        String dataString = "Data Type: " + this.id + "\n";
        for (String key : this.fields.keySet()) {
            String fieldType = this.getFieldType(key) instanceof STyData ? ((STyData) this.getFieldType(key)).getID()
                    : this.getFieldType(key).toString();
            dataString += "[ FIELD: " + key + " TYPE: " + fieldType + " ]\n";
        }
        return dataString;
    }

}
