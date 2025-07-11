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

public class Param extends Node {

    private String id;
    private TType type;

    public Param(String id, TType type){
        this.id = id;
        this.type = type;
    }

    public String getID(){
        return this.id;
    }

    public TType getType(){
        return this.type;
    }

    @Override
    public String toString(){
        return "Param(" + id + " : " + type.toString() + ")";
    }

    @Override
    public void accept(Visitor v){v.visit(this);}  //todas classes concretas precisam do accept
}
