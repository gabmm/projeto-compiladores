/*
 * UFJF - Universidade Federal de Juiz de Fora
 * Departamento de Ciência da Computação
 * Trabalho de compiladores - Linguagem Lang
 * Desenvolvido pelos alunos:
 *  - Gabriel Martins da Costa Medeiros - 201935032
 *  - Matheus Peron Resende Corrêa - 201965089C
 */

package ast;

import java.util.List;

import beaver.Symbol;
import visitors.Visitor;
import parser.sym;

public class Block extends Cmd {
    private List<Cmd> cmds;

    public Block(Symbol start, Symbol end, List<Cmd> cmds) {
        super(sym.BLOCK, start, end); 
        this.cmds = cmds;
    }

    public List<Cmd> getCmds() {
        return cmds;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }

    public String toString() {
        return "{ " + cmds.toString() + " }";
    }
}
