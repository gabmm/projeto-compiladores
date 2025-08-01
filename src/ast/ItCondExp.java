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

public class ItCondExp extends ItCond {
    private Exp cond;

    public ItCondExp(Exp cond) {
        this.cond = cond;
    }

    public Exp getCond() {
        return cond;
    }

    @Override
    public String toString() {
        return cond.toString();
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
