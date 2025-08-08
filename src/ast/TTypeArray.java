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

public class TTypeArray extends TType {
    private TType base;

    public TTypeArray(TType baseType) {
        super(sym.TYPE_LIST, null, null); 
        this.base = baseType;
    }
    public TTypeArray(Symbol start, Symbol end,TType base) {
        super(sym.TYPE_LIST, start, end);
        this.base = base;
    }

    public TType getBase() {
        return base;
    }

    @Override
    public boolean match(TType t) {
        return t instanceof TTypeArray && base.match(((TTypeArray) t).base);
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }

    @Override
    public String toString() {
        return base.toString() + "[]";
    }
}
