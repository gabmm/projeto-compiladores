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
import visitors.Visitor;
import parser.sym;
public class Print extends Cmd {

    private Exp e; 

    public Print(Symbol start, Symbol end, Exp e){
        super(sym.PRINT, start, end);
        this.e  = e;
    }

    public Exp getExpr(){
        return e;
    }

    public String toString(){
        return "print(" + e.toString() + ")";
    }

    public void accept(Visitor v){v.visit(this);}  //todas classes concretas precisam do accept
}