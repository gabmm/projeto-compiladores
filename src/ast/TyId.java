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

public class TyId extends TType {
    private String name;

    public TyId(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean match(TType t) {
        return t instanceof TyId && name.equals(((TyId) t).name);
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }

    @Override
    public String toString() {
        return name;
    }
}
