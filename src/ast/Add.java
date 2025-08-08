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
import beaver.Symbol;
import parser.Terminals;

public class Add extends BinOP {

    public Add(Exp l, Exp r){
        super(l, r);
    }
    public Add(Symbol start, Symbol end, Exp l, Exp r) {
        super(Terminals.PLUS, start, end, l, r);
    }
    @Override
    public String toString(){
        return "(" + getLeft().toString() + " + " + getRight().toString() + ")";
    }

    public void accept(Visitor v){v.visit(this);} 
}
