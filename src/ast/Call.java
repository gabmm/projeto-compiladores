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
// usada como expressão: x = f(5);
public class Call extends Exp {
    private String funcName;
    private List<Exp> args;
    private Exp index;

    public Call(String funcName, List<Exp> args, Exp index) {
        this.funcName = funcName;
        this.args = args;
        this.index = index; 
    }

    public String getFuncName() {
        return funcName;
    }

    public List<Exp> getArgs() {
        return args;
    }

    public Exp getIndex() {
        return index;
    }


    public void accept(Visitor v) {
        v.visit(this);
    }


    public String toString() {
        String str = funcName + "(";
        for (int i = 0; i < args.size(); i++) {
            str += args.get(i);
            if (i < args.size() - 1) str += ", ";
        }
        str += ")";
        if (index != null) {
            str += "[" + index + "]";
        }
        return str;
    }
}
