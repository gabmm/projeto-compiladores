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
import visitors.Visitor;

public class CmdList extends Cmd {
    private List<Cmd> commands;

    public CmdList(List<Cmd> commands) {
        this.commands = commands;
    }

    public List<Cmd> getCommands() {
        return commands;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }

    public String toString() {
        return commands.toString();
    }
}
