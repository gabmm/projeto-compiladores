/*
 * UFJF - Universidade Federal de Juiz de Fora
 * Departamento de Ciência da Computação
 * Trabalho de compiladores - Linguagem Lang
 * Desenvolvido pelos alunos:
 *  - Gabriel Martins da Costa Medeiros - 201935032
 *  - Matheus Peron Resende Corrêa - 201965089C
 */

package visitors;
import ast.*;

public abstract class Visitor { //deve ter um método abstrato de visit para cada classe concreta da ast
    public abstract void visit(Prog e);

    public abstract void visit(Mul e);
    public abstract void visit(Div e);
    public abstract void visit(Sub e);
    public abstract void visit(Add e);
    public abstract void visit(Eq e);
    public abstract void visit(Neq e);
    public abstract void visit(And e);
    public abstract void visit(Neg e);
    public abstract void visit(Var e);
    public abstract void visit(Fun e);
    public abstract void visit(NBool e);
    public abstract void visit(NChar e);
    public abstract void visit(NFloat e);
    public abstract void visit(NInt e);
    public abstract void visit(NNot e);
    public abstract void visit(Param e);
    public abstract void visit(Print e);
    public abstract void visit(If e);
    public abstract void visit(Block e);
    public abstract void visit(Cmd e);
    public abstract void visit (ItCondId e);
    public abstract void visit (ItCond e);
    public abstract void visit (ItCondExp e);
    public abstract void visit (Iterate e);
    public abstract void visit (Return e);
    public abstract void visit (Assign e);
    public abstract void visit (ArrayAccess e);
    public abstract void visit (Call e);
    public abstract void visit (CallStmt e);
    public abstract void visit (Null e);
    public abstract void visit (New e);
    public abstract void visit (CmdList e);
    public abstract void visit (Decl e);
    public abstract void visit (Data e);
    public abstract void visit (ExpList e);
    public abstract void visit(TyInt t);
    public abstract void visit(TyChar t);
    public abstract void visit(TyBool t);
    public abstract void visit(TyFloat t);
    public abstract void visit(TyId t);
    public abstract void visit(TTypeArray t);
    public abstract void visit(Lt t);
    public abstract void visit(Mod t);
    public abstract void visit(Dot t);
    public abstract void visit(Read t);

}
