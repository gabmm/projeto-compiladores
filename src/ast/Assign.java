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

public class Assign extends Cmd {
    private LValue lhs; 
    private Exp rhs;    

    public Assign(Symbol start, Symbol end, LValue lhs, Exp rhs) {
        super(sym.ASSIGN, start, end); 
        this.lhs = lhs;
        this.rhs = rhs;
    }

    public LValue getLhs() {
        return lhs;
    }

    public Exp getRhs() {
        return rhs;
    }

    public String toString() {
        return lhs.toString() + " = " + rhs.toString() + ";";
    }


    public void accept(Visitor v) {
        v.visit(this);
    }
}
