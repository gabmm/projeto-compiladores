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

public class New extends Exp {
    private TType type;
    private List<Exp> dimensions;

    public New(TType type, List<Exp> dimensions) {
        this.type = type;
        this.dimensions = dimensions;
    }

    public TType getType() {
        return type;
    }

    public List<Exp> getDimensions() {
        return dimensions;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }

    public String toString() {
        return "new " + type + dimensions;
    }
}
