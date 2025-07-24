package langUtil;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;

public class TyEnv<A> {
    /*
     * O que faz essa classe?
     * ela manipula um TreeMap
     * que mapeia string para um tipo genérico
     * são os maps das funcoes
     * a classe LocalEnv extenderá a TyEnv
     * adicionando a ela o nome da funcao
     */

    private TreeMap<String, A> typeEnv;

    public TyEnv(){
        this.typeEnv = new TreeMap<String,A>();
    }

    public A get(String id){
        return this.typeEnv.get(id);
    }

    public void put(String id, A element){
        this.typeEnv.put(id, element);
    }

    public boolean hasKey(String key){
        return this.typeEnv.containsKey(key);
    }

    public String toString(){
        String map = "";
        for (Map.Entry<String, A> entry : this.typeEnv.entrySet()){
            map += "[ ID: " + entry.getKey() + " TYPE: " + entry.getValue().toString() + "]\n";
            // map += entry.getValue().toString() + "\n";

        }
        return map;
    }

    public void printMap(){

        System.out.println(this.toString());
    }

}
