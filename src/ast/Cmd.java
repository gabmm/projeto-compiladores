/*
 * UFJF - Universidade Federal de Juiz de Fora
 * Departamento de Ciência da Computação
 * Trabalho de compiladores - Linguagem Lang
 * Desenvolvido pelos alunos:
 *  - Gabriel Martins da Costa Medeiros - 201935032
 *  - Matheus Peron Resende Corrêa - 201965089C
 */

package ast;

import visitors.Visitor;
import beaver.Symbol;
public abstract class Cmd extends Node {
    public Cmd(short id, Symbol start, Symbol end) {
        super(id, start, end);
    }
    public abstract void accept(Visitor v);
}
