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

public class Null extends Exp {
    public void accept(Visitor v) {
        v.visit(this);
    }
    public Null(Symbol start, Symbol end){
        super(sym.NULL, start, end);

    }
    public String toString() {
        return "null";
    }
}
