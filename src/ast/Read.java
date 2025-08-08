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
import visitors.Visitor;
import parser.sym;
public class Read extends Cmd {
    private final LValue lvalue;

    public Read(Symbol start, Symbol end, LValue lvalue) {
        super(sym.READ, start, end);
        this.lvalue = lvalue;
    }

    public LValue getLValue() {
        return lvalue;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }

    @Override
    public String toString() {
        return "read " + lvalue.toString();
    }
}
