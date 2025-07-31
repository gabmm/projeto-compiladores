package langUtil;

import java.util.LinkedHashMap;

public class STyData extends SType{

    private String id;
    private LinkedHashMap<String, SType> fields;
    private String[] relatedFunctions;

    public STyData(String id, LinkedHashMap<String, SType> fields) {
        this.id = id;
        this.fields = fields;
    }

    public STyData(String id, LinkedHashMap<String, SType> fields, String[] relatedFunctions) {
        this.id = id;
        this.fields = fields;
        this.relatedFunctions = relatedFunctions;
    }

    public Boolean hasField(String fieldName) {
        return this.fields.containsKey(fieldName);
    }

    public SType getFieldType(String fieldName) {
        return this.fields.get(fieldName); // caso não exista, retorna null
    }

    public String[] getRelatedFunctions() {
        return this.relatedFunctions;
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
            if (!otherData.hasField(key) || !this.getFieldType(key).match(otherData.getFieldType(key))) {
                return false;
            }
        }

        return true;
    }

    public String toStringFull() {
        String dataString = "";

        if (relatedFunctions != null) {
            dataString += "Abstract Data Type: " + this.id + "\n";
            dataString += "[ FUNS: ";
            for (int i = 0; i < relatedFunctions.length; i++) {
                if (i < relatedFunctions.length - 1) {
                    dataString += this.relatedFunctions[i] + ", ";
                } else {
                    dataString += this.relatedFunctions[i];
                }
            }
            dataString += " ]\n";
        } else {
            dataString += "Data Type: " + this.id + "\n";
        }

        for (String key : this.fields.keySet()) {
            String fieldType = this.getFieldType(key) instanceof STyData ? ((STyData) this.getFieldType(key)).getID()
                    : this.getFieldType(key).toString();
            dataString += "[ FIELD: " + key + " TYPE: " + fieldType + " ]\n";
        }

        return dataString;
    }

    public String toString() {
        return this.id;
    }

}
