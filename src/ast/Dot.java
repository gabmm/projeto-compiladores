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

public class Dot extends LValue {
    private LValue base;
    private String field;

    public Dot(LValue base, String field) {
        this.base = base;
        this.field = field;
    }

    public LValue getBase() { return base; }
    public String getField() { return field; }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }

    @Override
    public String toString() {
        return base.toString() + "." + field;
    }
}
