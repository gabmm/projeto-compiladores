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
//acessa o vetor.campo - Ex: v[3].x[0]
public class FieldAccess extends LValue {
    private LValue target;
    private String field;

    public FieldAccess(LValue target, String field) {
        this.target = target;
        this.field = field;
    }

    public LValue getTarget() {
        return target;
    }

    public String getField() {
        return field;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
