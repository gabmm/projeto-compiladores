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
import parser.sym;
import visitors.Visitor;

public class NNot extends Exp {
    private Exp exp;

    public NNot(Symbol start, Symbol end,Exp exp) {
        super(sym.NNOT, start, end);
        this.exp = exp;
    }

    public Exp getExp() {
        return exp;
    }

    public String toString() {
        return "!(" + exp.toString() + ")";
    }

    public void accept(Visitor v) {
        v.visit(this);
    }
}
