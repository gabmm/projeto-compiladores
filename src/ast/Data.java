/*
 * UFJF - Universidade Federal de Juiz de Fora
 * Departamento de Ciência da Computação
 * Trabalho de compiladores - Linguagem Lang
 * Desenvolvido pelos alunos:
 *  - Gabriel Martins da Costa Medeiros - 201935032
 *  - Matheus Peron Resende Corrêa - 201965089C
 */

package ast;

import java.util.List;
import visitors.Visitor;

public class Data extends Def {
    private boolean isAbstract;
    private List<Decl> decls; 
    private List<Fun> funs;   

    public Data(String name, boolean isAbstract, List<Decl> decls, List<Fun> funs) {
        super(name);
        this.isAbstract = isAbstract;
        this.decls = decls;
        this.funs = funs;
    }

    public Data(String name, List<Decl> decls) {
        super(name);
        this.isAbstract = false;
        this.decls = decls;
        this.funs = List.of();
    }

    public boolean isAbstract() {
        return isAbstract;
    }

    public List<Decl> getDecls() {
        return decls;
    }

    public List<Fun> getFuns() {
        return funs;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }

}
