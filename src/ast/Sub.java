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

public class Sub extends BinOP {

    public Sub(Symbol start, Symbol end,Exp l, Exp r){
        super(sym.MINUS,start, end, l, r);
    }

    public String toString(){
        return "(" + getLeft().toString() + " - " + getRight().toString() + ")";
    }

    public void accept(Visitor v){v.visit(this);}  //todas classes concretas precisam do accept

}
