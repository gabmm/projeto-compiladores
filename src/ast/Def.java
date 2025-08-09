/*
 * UFJF - Universidade Federal de Juiz de Fora
 * Departamento de Ciência da Computação
 * Trabalho de compiladores - Linguagem Lang
 * Desenvolvido pelos alunos:
 *  - Gabriel Martins da Costa Medeiros - 201935032
 *  - Matheus Peron Resende Corrêa - 201965089C
 */

package ast;
import beaver.Symbol;

public abstract class Def extends Node {
    protected String name;

    public Def(short id, Symbol start, Symbol end, String name) {
        super(id, start, end);
        this.name = name;
    }
    public Def(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
