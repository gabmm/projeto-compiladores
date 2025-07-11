/*
 * UFJF - Universidade Federal de Juiz de Fora
 * Departamento de Ciência da Computação
 * Trabalho de compiladores - Linguagem Lang
 * Desenvolvido pelos alunos:
 *  - Gabriel Martins da Costa Medeiros - 201935032
 *  - Matheus Peron Resende Corrêa - 201965089C
 */

package ast;

import visitors.Visitor;

public class ItCondId extends ItCond {
    private String id;
    private Exp cond;

    public ItCondId(String id, Exp cond) {
        this.id = id;
        this.cond = cond;
    }

    public String getID() {
        return id;
    }

    public Exp getCond() {
        return cond;
    }

    @Override
    public String toString() {
        return id + " : " + cond;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
