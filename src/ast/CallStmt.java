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

public class CallStmt extends Cmd {
    private final String id;
    private final List<Exp> args;
    private final List<LValue> returns; 

    public CallStmt(String id, List<Exp> args, List<LValue> returns) {
        this.id = id;
        this.args = args;
        this.returns = returns;
    }

    public String getID() {
        return id;
    }

    public List<Exp> getArgs() {
        return args;
    }

    public List<LValue> getReturns() {
        return returns;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }

    @Override
    public String toString() {
        return id + "(" + args + ")" + (returns.isEmpty() ? "" : " -> <" + returns + ">");
    }
}
