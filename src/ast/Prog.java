/*
 * UFJF - Universidade Federal de Juiz de Fora
 * Departamento de Ciência da Computação
 * Trabalho de compiladores - Linguagem Lang
 * Desenvolvido pelos alunos:
 *  - Gabriel Martins da Costa Medeiros - 201935032
 *  - Matheus Peron Resende Corrêa - 201965089C
 */

package ast;
import java.util.List;
import visitors.Visitor;

public class Prog extends Node {
    private final List<Def> defs;

    public Prog(List<Def> defs) {
        this.defs = defs;
    }

    public List<Def> getDefs() {
        return defs;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Prog {\n");
        for (Def def : defs) {
            sb.append(def.toString()).append("\n");
        }
        sb.append("}");
        return sb.toString();
    }

    @Override
    public void accept(Visitor v){v.visit(this);}  //todas classes concretas precisam do accept

}
