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

public class If extends Cmd {
    private Exp condition;
    private Cmd thenCmd;
    private Cmd elseCmd; 

    public If(Exp condition, Cmd thenCmd, Cmd elseCmd) {
        this.condition = condition;
        this.thenCmd = thenCmd;
        this.elseCmd = elseCmd;
    }

    public Exp getCondition() {
        return condition;
    }

    public Cmd getThenCmd() {
        return thenCmd;
    }

    public Cmd getElseCmd() {
        return elseCmd;
    }


    public String toString() {
        if (elseCmd != null) {
            return "if (" + condition + ") " + thenCmd + " else " + elseCmd;
        } else {
            return "if (" + condition + ") " + thenCmd;
        }
    }

    public void accept(Visitor v) {
        v.visit(this);
    }
}
