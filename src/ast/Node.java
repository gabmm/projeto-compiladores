/*
 * UFJF - Universidade Federal de Juiz de Fora
 * Departamento de Ciência da Computação
 * Trabalho de compiladores - Linguagem Lang
 * Desenvolvido pelos alunos:
 *  - Gabriel Martins da Costa Medeiros - 201935032
 *  - Matheus Peron Resende Corrêa - 201965089C
 */

package ast;

import visitors.Visitable;
import visitors.Visitor;

public abstract class Node extends beaver.Symbol implements Visitable {

    public Node(){}

    public int getLine() { return super.getLine(getStart()); }
    public int getCol() { return super.getColumn(getStart()); }
    public abstract void accept(Visitor v);
}
