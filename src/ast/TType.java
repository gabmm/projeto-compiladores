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

public abstract class TType extends Node {
    
    public TType(short id, Symbol start, Symbol end){
        super(id, start, end);
    }

    public abstract boolean match(TType t);
    public abstract void accept(Visitor v);
}
