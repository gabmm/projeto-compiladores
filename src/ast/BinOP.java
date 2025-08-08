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

public abstract class BinOP extends Exp {

    private Exp l;
    private Exp r;

    public BinOP(Exp l, Exp r){
        super();
        this.l = l;
        this.r = r;
    }
    public BinOP(short id, Symbol start, Symbol end, Exp left, Exp right) {
            super(id, start, end);
            this.l = left;
            this.r = right;
    }
    public void setLeft(Exp n){
        this.l = n;
    }

    public void setRight(Exp n){
        this.r = n;
    }

    public Exp getLeft(){
        return this.l;
    }

    public Exp getRight(){
        return this.r;
    }

}
