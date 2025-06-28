package ast;

public abstract class Def extends Node {
    protected String name;

    public Def(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
