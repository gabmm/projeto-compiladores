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

import beaver.Symbol;
import parser.sym;
import visitors.Visitor;

public class ExpList extends Node {
    private List<Exp> expressions;

    public ExpList(Symbol start, Symbol end,List<Exp> expressions) {
        super(sym.EXP_LIST, start, end);
        this.expressions = expressions;
    }

    public List<Exp> getExpressions() {
        return expressions;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }

    public String toString() {
        return expressions.toString();
    }
}
