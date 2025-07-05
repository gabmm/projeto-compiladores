package visitors;

import ast.*;
import java.util.HashMap;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class DotVisitor extends Visitor {

    private ArrayList<String> graph;
    private Stack<String> nodes;
    private int counter;

    public DotVisitor(){
        graph = new ArrayList<String>();
        graph.add("digraph AST {");
        nodes = new Stack<String>();
        counter = 0;
    }

    public void saveToFile(String s){
        try{
            BufferedWriter out = new BufferedWriter(new FileWriter(s));
            for(String line : graph){
                out.write(line, 0, line.length());
                out.newLine();
            }
            out.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private String createNode(String label){
        String n = "Node" + (counter++);
        String s = n + " [label=\"" + label + "\"];\n";
        graph.add(s);
        return n;
    }

    private void createEdge(String from, String to){
        String s = from + " -> " + to + ";";
        graph.add(s);
    }
    
    public void visit(Prog e){
        System.out.println("Debugger: PROG");
        String prog = createNode("prog");
        for (Def def : e.getDefs()) {
            if (def instanceof Data){
                Data data = (Data) def;
                data.accept(this);
                createEdge(prog, nodes.pop());
            } else if (def instanceof Fun){
                Fun fun = (Fun) def;
                fun.accept(this);
                createEdge(prog, nodes.pop());
            }
        }
        graph.add("}");
    }

    public void visit(Mul e){
        System.out.println("Debugger: MUL");
        String mul = createNode("*");
        e.getLeft().accept(this);
        createEdge(mul, nodes.pop());
        e.getRight().accept(this);
        createEdge(mul, nodes.pop());
        nodes.push(mul);
    }

    public void visit(Div e){
        System.out.println("Debugger: DIV");
        String div = createNode("/");
        e.getLeft().accept(this);
        createEdge(div, nodes.pop());
        e.getRight().accept(this);
        createEdge(div, nodes.pop());
        nodes.push(div);
    }

    public void visit(Sub e){
        System.out.println("Debugger: SUB");
        String sub = createNode("-");
        e.getLeft().accept(this);
        createEdge(sub, nodes.pop());
        e.getRight().accept(this);
        createEdge(sub, nodes.pop());
        nodes.push(sub);
    }

    public void visit(Add e){
        System.out.println("Debugger: ADD");
        String add = createNode("+");
        e.getLeft().accept(this);
        createEdge(add, nodes.pop());
        e.getRight().accept(this);
        createEdge(add, nodes.pop());
        nodes.push(add);
    }

    public void visit(Eq e){
        System.out.println("Debugger: EQ");
        String eq = createNode("==");
        e.getLeft().accept(this);
        createEdge(eq, nodes.pop());
        e.getRight().accept(this);
        createEdge(eq, nodes.pop());
        nodes.push(eq);
    }

    public void visit(Neq e){
        System.out.println("Debugger: NEQ");
        String neq = createNode("!=");
        e.getLeft().accept(this);
        createEdge(neq, nodes.pop());
        e.getRight().accept(this);
        createEdge(neq, nodes.pop());
        nodes.push(neq);
    }

    public void visit(Or e){} //nem existe or

    public void visit(And e){
        System.out.println("Debugger: AND");
        String and = createNode("&&");
        e.getLeft().accept(this);
        createEdge(and, nodes.pop());
        e.getRight().accept(this);
        createEdge(and, nodes.pop());
        nodes.push(and);
    }
    public void visit(Neg e){
        System.out.println("Debugger: NEG");
        String neg = createNode("-");
        e.getExp().accept(this);
        createEdge(neg, nodes.pop());
        nodes.push(neg);
    }

    public void visit(Var e){ 
        System.out.println("Debugger: VAR");
        nodes.push(createNode(e.toString())); 
    }

    public void visit(Attr e){} //nem existe ATTR

    public void visit(Fun e){
        System.out.println("Debugger: FUN");
        String fun = createNode("fun " + e.getName());
        String types = createNode("types");
        String params = createNode("params");
        for (TType t : e.getType()){
            t.accept(this);
            createEdge(types, nodes.pop());
        }
        createEdge(fun, types);
        for (Param p : e.getParams()){
            p.accept(this);
            createEdge(params, nodes.pop());
        }
        createEdge(fun, params);
        e.getBody().accept(this);
        createEdge(fun, nodes.pop());
        nodes.push(fun);
    }

    public void visit(NBool e){ 
        System.out.println("Debugger: NBOOL");
        nodes.push(createNode(e.toString())); 
    }

    public void visit(NChar e){ 
        System.out.println("Debugger: NCHAR");
        nodes.push(createNode(e.toString())); 
    }

    public void visit(NFloat e){ 
        System.out.println("Debugger: NFLOAT");
        nodes.push(createNode(e.toString())); 
    }

    public void visit(NInt e){ 
        System.out.println("Debugger: NINT");
        nodes.push(createNode(e.toString())); 
    }

    public void visit(NNot e){
        System.out.println("Debugger: NNOT");
        String nnot = createNode("!");
        e.getExp().accept(this);
        createEdge(nnot, nodes.pop());
        nodes.push(nnot);
    }

    public void visit(Read e) {

    }

    public void visit(Param e){
        System.out.println("Debugger: PARAM");
        String param = createNode(e.getID());
        e.getType().accept(this);
        createEdge(param, nodes.pop());
        nodes.push(param);
    }

    public void visit(Print e){
        System.out.println("Debugger: PRINT");
        String print = createNode("print");
        e.getExpr().accept(this);
        createEdge(print, nodes.pop());
        nodes.push(print);
    }

    public void visit(If e){
        System.out.println("Debugger: PRINT");
        String ifstr = createNode("if");
        String ifcond = createNode("cond");
        e.getCondition().accept(this);
        createEdge(ifcond, nodes.pop());
        createEdge(ifstr, ifcond);
        e.getThenCmd().accept(this);
        String then = createNode("then");
        createEdge(then, nodes.pop());
        createEdge(ifstr, then);
        if (e.getElseCmd() != null){
            String elseb = createNode("else");
            e.getElseCmd().accept(this);
            createEdge(elseb, nodes.pop());
            createEdge(ifstr, elseb);
        }
        nodes.push(ifstr);
    }

    public void visit(Block e){
        System.out.println("Debugger: BLOCK");
        String block = createNode("block");
        for (Cmd cmd : e.getCmds()){
            cmd.accept(this);
            createEdge(block, nodes.pop());
        }
        nodes.push(block);
    }

    public void visit(Cmd e){} //cmd é abstrato... falta é read

    public void visit (ItCondId e){
        System.out.println("Debugger: ITCONDID");
        String itcondid = createNode(e.getID() + " :");
        e.getCond().accept(this);
        createEdge(itcondid, nodes.pop());
        nodes.push(itcondid);
    }

    public void visit (ItCond e){} //itcond é abstrato

    public void visit (ItCondExp e){
        System.out.println("Debugger: ITCONDEXP");
        String itcondexp = createNode("itcond");
        e.getCond().accept(this);
        createEdge(itcondexp, nodes.pop());
        nodes.push(itcondexp);
    }

    public void visit (Iterate e){
        System.out.println("Debugger: ITERATE");
        String iterate = createNode("iterate");
        e.getCondition().accept(this);
        createEdge(iterate, nodes.pop());
        e.getBody().accept(this);
        createEdge(iterate, nodes.pop());
        nodes.push(iterate);
    }

    public void visit (Return e){
        System.out.println("Debugger: RETURNS");
        String returns = createNode("returns");
        for (Exp exp : e.getExps()){
            exp.accept(this);
            createEdge(returns, nodes.pop());
        }
        nodes.push(returns);
    }
    public void visit (Assign e){
        System.out.println("Debugger: ASSIGN");
        String assign = createNode("=");
        e.getLhs().accept(this);
        createEdge(assign, nodes.pop());
        e.getRhs().accept(this);
        createEdge(assign, nodes.pop());
        nodes.push(assign);
    }

    public void visit (ArrayAccess e){
        System.out.println("Debugger: ARRAYACESS");
        String arrayAccess = createNode("[]");
        e.getArray().accept(this);
        createEdge(arrayAccess, nodes.pop());
        e.getIndex().accept(this);
        createEdge(arrayAccess, nodes.pop());
        nodes.push(arrayAccess);
    }

    public void visit (FieldAccess e){} //nem existe

    public void visit (Call e){
        System.out.println("Debugger: CALL");
        String call = createNode("call " + e.getFuncName());
        String args = createNode("()");
        String returnTo = createNode("[]");
        for (Exp arg : e.getArgs()){
            arg.accept(this);
            createEdge(args, nodes.pop());
        }
        createEdge(call, args);
        e.getIndex().accept(this);
        createEdge(returnTo, nodes.pop());
        createEdge(call, returnTo);
        nodes.push(call);
    }

    public void visit (CallStmt e){
        System.out.println("Debugger: CALLSTMT");
        String callstmt = createNode("callstmt " + e.getID());
        String args = createNode("()");
        String returns = createNode("<>");
        for (Exp arg : e.getArgs()){
            arg.accept(this);
            createEdge(args, nodes.pop());
        }
        createEdge(callstmt, args);
        for (LValue ret : e.getReturns()){
            ret.accept(this);
            createEdge(returns, nodes.pop());
        }
        createEdge(callstmt, returns);
        nodes.push(callstmt);
    }

    public void visit (Null e){
        System.out.println("Debugger: NULL");
        nodes.push(createNode("null"));
    }

    public void visit (New e){
        System.out.println("Debugger: NEW");
        String newstr = createNode("New");
        e.getType().accept(this);
        createEdge(newstr, nodes.pop());
        for (Exp d : e.getDimensions()){
            d.accept(this);
            createEdge(newstr, nodes.pop());
        }
        nodes.push(newstr);
    }

    public void visit (CmdList e){
        System.out.println("Debugger: CMDLIST");
        String cmdlist = createNode("cmdlist");
        for (Cmd cmd : e.getCommands()){
            cmd.accept(this);
            createEdge(cmdlist, nodes.pop());
        }
        nodes.push(cmdlist);
    }

    public void visit (Decl e){
        System.out.println("Debugger: DECL");
        String decl = createNode("decl " + e.getId());
        e.getType().accept(this);
        createEdge(decl, nodes.pop());
        nodes.push(decl);
    }

    public void visit (Data e){
        System.out.println("Debugger: DATA");
        String data = e.isAbstract() == true ? "Abstract " : "";
        data = createNode(data + "data " + e.getName());

        String decls = createNode("decls");
        for (Decl d : e.getDecls()){
            d.accept(this);
            createEdge(decls, nodes.pop());
        }
        createEdge(data, decls);

        String funs = createNode("funs");
        for (Fun f : e.getFuns()){
            f.accept(this);
            createEdge(funs, nodes.pop());
        }
        createEdge(data, funs);

        nodes.push(data);
    }

    public void visit (ExpList e){
        System.out.println("Debugger: EXPLIST");
        String explist = createNode("explist");
        for (Exp exp : e.getExpressions()){
            exp.accept(this);
            createEdge(explist, nodes.pop());
        }
        nodes.push(explist);
    }

    public void visit(TyInt e){
        System.out.println("Debugger: INT");
        nodes.push(createNode("Int"));
    }

    public void visit(TyChar e){
        System.out.println("Debugger: CHAR");
        nodes.push(createNode("Char"));
    }

    public void visit(TyBool e){
        System.out.println("Debugger: BOOL");
        nodes.push(createNode("Bool"));
    }

    public void visit(TyFloat e){
        System.out.println("Debugger: Float");
        nodes.push(createNode("Float"));
    }

    public void visit(TyId e){
        System.out.println("Debugger: TYID");
        nodes.push(createNode("tyid " + e.getName()));
    }

    public void visit(TTypeArray e){
        System.out.println("Debugger: TTYPEARRAY");
        String typeArray = createNode("[]");
        e.getBase().accept(this);
        createEdge(typeArray, nodes.pop());
        nodes.push(typeArray);
    }

    public void visit(Lt e){
        System.out.println("Debugger: LT");
        String lt = createNode("<");
        e.getLeft().accept(this);
        createEdge(lt, nodes.pop());
        e.getRight().accept(this);
        createEdge(lt, nodes.pop());
        nodes.push(lt);
    }

    public void visit(Mod e){
        System.out.println("Debugger: MOD");
        String mod = createNode("%");
        e.getLeft().accept(this);
        createEdge(mod, nodes.pop());
        e.getRight().accept(this);
        createEdge(mod, nodes.pop());
        nodes.push(mod);
    }

    public void visit(Dot e){
        System.out.println("Debugger: DOT");
        String dot = createNode(".");
        e.getBase().accept(this);
        createEdge(dot, nodes.pop());
        String field = createNode(e.getField());
        createEdge(dot, field);
        nodes.push(dot);
    }


}
