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
// acessa o vetor
public class ArrayAccess extends LValue {
    private LValue array;  
    private Exp index;   

    public ArrayAccess(LValue array, Exp index) {
        this.array = array;
        this.index = index;
    }

    public LValue getArray() {
        return array;
    }

    public Exp getIndex() {
        return index;
    }

    public String toString() {
        return array.toString() + "[" + index.toString() + "]";
    }


    public void accept(Visitor v) {
        v.visit(this);
    }
}
