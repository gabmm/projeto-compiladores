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

public class Decl extends Def{
    private TType type;
    private String id;
    public Decl(Symbol start, Symbol end, String id, TType type) {
        super(sym.DECL, start, end, id);
        this.id = id;
        this.type = type;
    }


    public TType getType() {
        return type;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }

    public String toString() {
        return id + " :: " + type;
    }
}
