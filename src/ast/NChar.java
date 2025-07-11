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

public class NChar extends Exp{
    private char value;

    public NChar ( char value){
        this.value = value;
    }

    public char getValue(){
        return value;
    }
    public String toString() {
        return "'" + value + "'";
    }
    public void accept(Visitor v){v.visit(this);}
}
