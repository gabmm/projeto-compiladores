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
import java.util.List;

public class Return extends Cmd {  

    private List<Exp> exps;  

    public Return(List<Exp> exps) {
        this.exps = exps;
    }

    public List<Exp> getExps() {
        return exps;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }
}
