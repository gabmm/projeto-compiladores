package langUtil;
import java.util.ArrayList;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;


public class LocalEnv<A> extends TyEnv<A>{
    /*
     * Essa classe representa o ambiente
     * local de uma funcao
     * ela possui o nome e o array de retornos
     * e como extende a TyEnv, o mapeamento dos variáveis locais
     */

    private String id;
    private SType funType;

    public LocalEnv(String id, SType funType) {
        this.id = id;
        this.funType = funType;
    }

    public String getFunctionID(){
        return this.id;
    }

    public SType getType() {
        return this.funType;
    }

    public LocalEnv<A> cloneEnv() {
        LocalEnv<A> copy = new LocalEnv<>(this.id, this.funType);
        copy.getEnv().putAll(super.clonEnv().getEnv());
        return copy;
    }

    public String toString(){
        String env = "\n";
        env += "Function " + this.id;
        env += funType.toString() + "\n";
        for (Map.Entry<String, A> entry : super.getEnv().entrySet()) {
            if (!(entry.getValue() instanceof LocalEnv)) {
                if (entry.getValue() instanceof STyData) {
                    env += "[ ID: " + entry.getKey() + " TYPE: " + ((STyData) entry.getValue()).getID() + " ]\n";
                } else {
                    env += "[ ID: " + entry.getKey() + " TYPE: " + entry.getValue().toString() + " ]\n";
                }

            }
        }
        // env += super.toString() + "\n";
        return env;
    }

    public void printFunctionEnv(){
        System.out.println(this.toString());
    }

}
