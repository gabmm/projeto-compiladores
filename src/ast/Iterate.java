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

public class Iterate extends Cmd {
    private ItCond condition;  
    private Cmd body;          

    public Iterate(Symbol start, Symbol end, ItCond condition, Cmd body) {
        super(sym.ITERATE, start, end);
        this.condition = condition;
        this.body = body;
    }

    public ItCond getCondition() {
        return condition;
    }

    public Cmd getBody() {
        return body;
    }


    public void accept(Visitor v) {
        v.visit(this);
    }


    public String toString() {
        return "iterate (" + condition + ") " + body;
    }
}
