package visitors;

import ast.*;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class InterpretVisitor extends Visitor {

    private final Stack<HashMap<String, Object>> env;
    private final HashMap<String, Fun> funcs;
    private final HashMap<String, Data> dataDefs;
    private final Stack<Object> operands;
    private boolean returnMode;

    public InterpretVisitor() {
        this.env = new Stack<>();
        this.funcs = new HashMap<>();
        this.dataDefs = new HashMap<>();
        this.operands = new Stack<>();
        this.returnMode = false;
    }
 
    private Object findVariable(String varName) { // encontra valor atual da variável
        for (int i = env.size() - 1; i >= 0; i--) {
            if (env.get(i).containsKey(varName)) {
                return env.get(i).get(varName);
            }
        }
        throw new RuntimeException("Erro: Variavel '" + varName + "' nao declarada.");
    }

    private void addVariable(String varName, Object value) { // adiciona a variável criada pela primeira vez no escopo
        for (HashMap<String, Object> scope : env) {
            if (scope.containsKey(varName)) {
                throw new RuntimeException("Erro Semantico: Redefinicao ilegal da variavel '" + varName + "'.");
            }
        }
        env.peek().put(varName, value);
    }
    
    private void updateVariable(String varName, Object value) { // altera valor da variavel que já tenha sido inicializada
        for (int i = env.size() - 1; i >= 0; i--) {
            if (env.get(i).containsKey(varName)) {
                env.get(i).put(varName, value);
                return;
            }
        }
        throw new RuntimeException("Erro: Variavel '" + varName + "' nao declarada para atribuicao.");
    }

    @Override
    public void visit(Prog node) {
        // percorre todas as definições:os nomes das funções e tipos de dados.
        for (Def def : node.getDefs()) {
            if (def instanceof Fun) {
                Fun fun = (Fun) def;
                funcs.put(fun.getName(), fun);
            } else if (def instanceof Data) {
                Data data = (Data) def;
                dataDefs.put(data.getName(), data);
            }
        }
        if (!funcs.containsKey("main")) {
            throw new RuntimeException("Erro de execução: Ponto de entrada 'main' não definido.");
        }
        
        Fun mainFunc = funcs.get("main");
        
        
        env.push(new HashMap<>()); // cria o ambiente global a cada execução do programa, onde tudo será armazenado
        
        mainFunc.accept(this); // começa a execução principal

        env.pop(); // encerra
    }
    
   @Override
    public void visit(Fun node) {
        HashMap<String, Object> localEnv = new HashMap<>();
        List<Param> params = node.getParams();
        for (int i = params.size() - 1; i >= 0; i--) {
                localEnv.put(params.get(i).getID(), operands.pop());
            }
        env.push(localEnv);
        node.getBody().accept(this);
        
        // Se a função terminou sem um return, o seu valor de retorno é (null).
        if (!returnMode) {
            operands.push(null);
        }
        
        env.pop();
        returnMode = false;
    }

   
    @Override
    public void visit(Block node) {

    }
    
    @Override
    public void visit(CmdList node) {

    }
    
    @Override
    public void visit(Assign node) {
     
    }
    
    @Override
    public void visit(Return node) {

    }

    @Override
    public void visit(If node) {

    }

    @Override
    public void visit(CallStmt node) {

    }
    
    @Override
    public void visit(Add node) {

    }

  

    @Override
    public void visit(And node) {

    }

    @Override
    public void visit(Or node) {

    }

    @Override
    public void visit(Eq node) {

    }

    @Override
    public void visit(Neq node) {

    }
    
    @Override
    public void visit(Var node) {
        
    }
    
    @Override
    public void visit(NInt node) {  }

    @Override
    public void visit(NFloat node) {  }

    @Override
    public void visit(NBool node) {  }

    @Override
    public void visit(NChar node) {  }

    @Override
    public void visit(Null node) {  }

    @Override
    public void visit(New node) {
      
    }

    @Override
    public void visit(ArrayAccess node) {

    }

    @Override
    public void visit(Dot node) {

    }
    
    @Override
    public void visit(Data node) {
        
    }
    @Override public void visit(TyInt node) {}
    @Override public void visit(TyChar node) {}
    @Override public void visit(TyBool node) {}
    @Override public void visit(TyFloat node) {}
    @Override public void visit(TyId node) {}
    @Override public void visit(TTypeArray node) {}
    @Override public void visit(ExpList node) {}
    @Override public void visit(Cmd node) {}
    @Override public void visit(ItCond node) {}
    @Override public void visit(ItCondExp node) { }
    @Override public void visit(ItCondId node) { }
    @Override public void visit(Param node) {}
    
    @Override
    public void visit(Attr node) {
        
    }
    @Override 
     public void visit(FieldAccess node) {

    }

    @Override
    public void visit(Sub node) {

    }

    @Override
    public void visit(Mul node) {

    }

    @Override
    public void visit(Div node) {

    }

    @Override
    public void visit(Mod node) {

    }

    @Override
    public void visit(Lt node) {
        
    }

    @Override
    public void visit(NNot node) {

    }

    @Override
    public void visit(Neg node) {

    }


    @Override
    public void visit(Decl node) {

    }

    @Override
    public void visit(Print node) {

    }

    @Override

    public void visit(Iterate node) {
       
    }

    
    @Override
    public void visit(Call node) {

}
}