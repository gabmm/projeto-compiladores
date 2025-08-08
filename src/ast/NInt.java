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

public class NInt extends Exp {

    private int value;

    public NInt(Symbol start, Symbol end,int value){
        super(sym.NINT, start, end);
        this.value = value;
    }

    public int getValue(){
        return this.value;
    }

    public String toString(){
        return Integer.toString(value);
    }

    public void accept(Visitor v){v.visit(this);}  //todas classes concretas precisam do accept
}
