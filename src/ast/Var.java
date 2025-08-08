/*
 * UFJF - Universidade Federal de Juiz de Fora
 * Departamento de Ciência da Computação
 * Trabalho de compiladores - Linguagem Lang
 * Desenvolvido pelos alunos:
 *  - Gabriel Martins da Costa Medeiros - 201935032
 *  - Matheus Peron Resende Corrêa - 201965089C
 */

package ast;
import beaver.Symbol;
import parser.sym;
import visitors.Visitor;

public class Var extends LValue {

    private String name;

    public Var(Symbol start, Symbol end, String name){
        super(sym.VAR, start, end);
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public String toString(){
        return "Var(" + name + ")";
    }

    public void accept(Visitor v){v.visit(this);}  //todas classes concretas precisam do accept

}
