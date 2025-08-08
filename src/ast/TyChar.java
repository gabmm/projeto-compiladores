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

public class TyChar extends TType {
    @Override
    public boolean match(TType t) {
        return t instanceof TyChar;
    }
    public TyChar(Symbol start, Symbol end) {
            super(sym.TYCHAR, start, end);
    }
    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }

    @Override
    public String toString() {
        return "Char";
    }
}
