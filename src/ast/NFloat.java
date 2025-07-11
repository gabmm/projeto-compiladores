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

public class NFloat extends Exp{
    private float value;

    public NFloat ( float value){
        this.value = value;
    }

    public float getValue(){
        return value;
    }
    public String toString(){
        return Float.toString(value);
    }
    public void accept(Visitor v){v.visit(this);}
}
