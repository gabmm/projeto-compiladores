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

    private Object findVariableAux(String varName){
        for (int i = env.size() - 1; i >= 0; i--) {
            if (env.get(i).containsKey(varName)) {
                return env.get(i).get(varName);
            }
        }
        return null;
    }
 
    private Object findVariable(String varName) { // encontra valor atual da variável
        Object returnValue = findVariableAux(varName);

        if (returnValue != null){
            return returnValue;
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

    private void addOrUpdateVariable(String varName, Object value){
        Object returnValue = findVariableAux(varName);
        if (returnValue != null){
            updateVariable(varName, value);
        } else {
            addVariable(varName, value);
        }
    }

    private boolean isInteger(Object number) {
        return number instanceof Integer;
    }

    @Override
    public void visit(Prog node) {
        System.out.println("Visitando prog...");
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
        System.out.println("Visitando fun...");
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
        System.out.println("Visitando block...");
        for (Cmd cmd : node.getCmds()) {
            cmd.accept(this);
        }
    }
    
    @Override
    public void visit(CmdList node) {
        System.out.println("Visitando cmdlist...");
        for (Cmd cmd : node.getCommands()) {
            cmd.accept(this);
        }
    }
    
    @Override
    public void visit(Assign node) {
        System.out.println("Visitando Assign...");
        node.getRhs().accept(this);
        LValue lv = node.getLhs();
        String varName = "";
        if (lv instanceof Var){
            Var var = (Var) lv;
            varName = var.getName();
        } else if (lv instanceof ArrayAccess) {
            ArrayAccess arrAcc = (ArrayAccess) lv;
            varName = arrAcc.toString();
        } else if (lv instanceof Dot) {
            Dot dot = (Dot) lv;
            varName = dot.toString();
        }
        addOrUpdateVariable(varName, operands.pop());
    }
    
    @Override
    public void visit(Return node) {
        System.out.println("Visitando Return...");
        for (Exp ret : node.getExps()) {
            ret.accept(this);
        }
        returnMode = true;
    }

    @Override
    public void visit(If node) {
        System.out.println("Visitando If...");
        node.getCondition().accept(this);
        if ((Boolean) operands.pop()) {
            node.getThenCmd().accept(this);
        } else if (node.getElseCmd() != null) {
            node.getElseCmd().accept(this);
        }
    }

    @Override
    public void visit(CallStmt node) {
        System.out.println("Visitando CallStmt...");
        Fun fun = funcs.get(node.getID()); // acho a funcao
        if (fun != null) {
            for (Exp arg : node.getArgs()) {
                arg.accept(this); // empilho os argumentos
            }
            fun.accept(this); // rodo a funcao, que empilha os retornos

            /*
             * tenho os retornos no topo da pilha, preciso agora dar pop() na pilha e mapear
             * o retorno as variaveis
             */
            for (LValue ret : node.getReturns()) {
                ret.accept(this); // esse é o nome das variaveis
                addVariable((String) operands.pop(), operands.pop());
                // env.peek().put((String)operands.pop(), operands.pop());
            }
        }
    }
    
    @Override
    public void visit(Add node) {
        System.out.println("Visitando Add...");
        node.getLeft().accept(this);
        node.getRight().accept(this);
        Number left, right;
        right = (Number) operands.pop();
        left = (Number) operands.pop();
        if (isInteger(right) && isInteger(left)) {
            operands.push(Integer.valueOf(left.intValue() + right.intValue()));
        } else {
            operands.push(Float.valueOf(left.floatValue() + right.floatValue()));
        }
    }

    @Override
    public void visit(And node) {
        System.out.println("Visitando And...");
        node.getLeft().accept(this);
        node.getRight().accept(this);
        Boolean left, right;
        right = (Boolean) operands.pop();
        left = (Boolean) operands.pop();
        operands.push(Boolean.valueOf(right && left));
    }

    @Override
    public void visit(Or node) {
        System.out.println("Visitando Or... (nem existe...)");
        node.getLeft().accept(this);
        boolean left = (Boolean) operands.pop();
        if (left) { 
            operands.push(true);
            return;
        }
        node.getRight().accept(this);
        operands.push(operands.pop());
    }

    @Override
    public void visit(Eq node) {
        System.out.println("Visitando EQ...");
        node.getLeft().accept(this);
        node.getRight().accept(this);
        operands.push(Boolean.valueOf(operands.pop().equals(operands.pop())));
    }

    @Override
    public void visit(Neq node) {
        System.out.println("Visitando NEQ...");
        node.getRight().accept(this);
        node.getLeft().accept(this);
        operands.push(!Boolean.valueOf(operands.pop().equals(operands.pop())));
    }
    
    @Override
    public void visit(Var node) {
        System.out.println("Visitando VAR...");
        // Object value = env.peek().get(node.getName());
        Object value = findVariable(node.getName());
        operands.push(value);
    }
    
    @Override
    public void visit(NInt node) {
        System.out.println("Visitando NINT...");
        operands.push(Integer.valueOf(node.getValue()));
    }

    @Override
    public void visit(NFloat node) {
        System.out.println("Visitando NFLOAT...");
        operands.push(Float.valueOf(node.getValue()));
    }

    @Override
    public void visit(NBool node) {
        System.out.println("Visitando NBOOL...");
        operands.push(Boolean.valueOf(node.getValue()));
    }

    @Override
    public void visit(NChar node) {
        System.out.println("Visitando NCHAR...");
        operands.push(Character.valueOf(node.getValue()));
    }

    @Override
    public void visit(Null node) {
        System.out.println("Visitando Null...");
        operands.push(null); 
    }

    @Override
    public void visit(New node) {
        System.out.println("Visitando New...");
      
            if (node.getType() instanceof TyId) {
            String dataName = ((TyId) node.getType()).getName();
            Data dataDef = dataDefs.get(dataName);
            HashMap<String, Object> newInstance = new HashMap<>();
            for (Decl field : dataDef.getDecls()) {
                newInstance.put(field.getName(), null);
            }
            operands.push(newInstance);
        } else if (node.getType() instanceof TTypeArray) {
            node.getDimensions().get(0).accept(this);
            int size = (Integer) operands.pop();
            operands.push(new Object[size]);
        }
       
    }

    @Override
    public void visit(ArrayAccess node) {
        System.out.println("Visitando ArrayAccess...");
        node.getArray().accept(this);
        node.getIndex().accept(this);
        int index = (Integer) operands.pop();
        Object array = operands.pop();
        if (array instanceof List) {
            operands.push(((List) array).get(index));
        } else if (array.getClass().isArray()) {
            operands.push(java.lang.reflect.Array.get(array, index)); // reflect.array retorna um Objeto genérico
        }                                                             // pois não há como saber se o vetor é de inteios, booleanos...
    }

    @Override
    public void visit(Dot node) {
        System.out.println("Visitando Dot...");
        node.getBase().accept(this);
        HashMap<String, Object> object = (HashMap<String, Object>) operands.pop();
        operands.push(object.get(node.getField()));
    }
    
    @Override
    public void visit(Data node) {
        if (!dataDefs.containsKey(node.getName())) { // armazena decl
            dataDefs.put(node.getName(), node);
        }
        List<Fun> func = node.getFuns();
        if (func != null && !func.isEmpty()) { // Abstract data - armazena  fun 
            String name = node.getName();
            for (Fun f : func) {
                String funcDotName = name + "." + f.getName(); // para metodos função.nome()
                funcs.put(funcDotName, f);
            }
    }
    }
    @Override 
    public void visit(ItCondExp node) {
        node.getCond().accept(this);
     }
    @Override 
    public void visit(ItCondId node) {
        node.getCond().accept(this);
     }

    @Override
    public void visit(Attr node) { // acho que não precisa dessa, seria igual o assign 
        
    }
    @Override 
     public void visit(FieldAccess node) { // acho que não precisa dessa, seria mesma coisa que o DOT
        node.getTarget().accept(this);
        HashMap<String, Object> ob = ( HashMap<String, Object>) operands.pop();
        operands.push(ob.get(node.getField()));
    }

    @Override
    public void visit(Sub node) {
        System.out.println("Visitando Sub...");
        node.getLeft().accept(this);
        node.getRight().accept(this);
        Number left, right;
        right = (Number) operands.pop();
        left = (Number) operands.pop();
        if (isInteger(right) && isInteger(left)) {
            operands.push(Integer.valueOf(left.intValue() - right.intValue()));
        } else {
            operands.push(Float.valueOf(left.floatValue() - right.floatValue()));
        }
    }

    @Override
    public void visit(Mul node) {
        System.out.println("Visitando Mul...");
        node.getLeft().accept(this);
        node.getRight().accept(this);
        Number left, right;
        right = (Number) operands.pop();
        left = (Number) operands.pop();
        if (isInteger(right) && isInteger(left)) {
            operands.push(Integer.valueOf(left.intValue() * right.intValue()));
        } else {
            operands.push(Float.valueOf(left.floatValue() * right.floatValue()));
        }
    }

    @Override
    public void visit(Div node) {
        System.out.println("Visitando Div...");
        node.getLeft().accept(this);
        node.getRight().accept(this);
        Number left, right;
        right = (Number) operands.pop();
        left = (Number) operands.pop();
        if (isInteger(right) && isInteger(left)) {
            operands.push(Integer.valueOf(left.intValue() / right.intValue()));
        } else {
            operands.push(Float.valueOf(left.floatValue() / right.floatValue()));
        }
    }

    @Override
    public void visit(Mod node) {
        System.out.println("Visitando Add...");
        node.getLeft().accept(this);
        node.getRight().accept(this);
        int left, right;
        right = (Integer) operands.pop();
        left = (Integer) operands.pop();
        operands.push(Integer.valueOf(left % right));
    }

    @Override
    public void visit(Lt node) {
        try {
            node.getLeft().accept(this);
            node.getRight().accept(this);
            Number right = (Number) operands.pop();
            Number left = (Number) operands.pop();
            operands.push(left.floatValue() < right.floatValue());
        } catch (Exception e) {
            throw new RuntimeException( " (" + node.getLine() + ", " + node.getCol() + ") " + e.getMessage() );
        }
    }

    @Override
    public void visit(NNot node) {
        node.getExp().accept(this);
        operands.push(!(Boolean) operands.pop());
    }

    @Override
    public void visit(Neg node) {
        node.getExp().accept(this);
        Number exp = (Number) operands.pop();
        if (isInteger(exp)) {
            operands.push(Integer.valueOf(exp.intValue()));
        } else {
            operands.push(Float.valueOf(exp.floatValue()));
        }
    }


    @Override
    public void visit(Decl node) {
        addVariable(node.getName(), null);
    }

    @Override
    public void visit(Print node) {
        System.out.println("Visitando print...");
        node.getExpr().accept(this);
        System.out.println(operands.pop().toString());
    }

    @Override
    public void visit(Iterate node) {
        ItCond cond = node.getCondition(); 
        if (cond instanceof ItCondExp) { // se o loop for quantidade
            ItCondExp itExp = (ItCondExp) cond;
            itExp.getCond().accept(this); 
            int times = (Integer) operands.pop(); // Pega qtd de vezes
            for (int i = 0; i < times; i++) { // executa a qtd de vezes necessária
                node.getBody().accept(this); 
            }
        } else if (cond instanceof ItCondId) { // se for item: vetor 
            ItCondId itId = (ItCondId) cond;
            itId.getCond().accept(this);
            Object collection = operands.pop();
            String name = itId.getID(); 

            if (collection instanceof List) { // função com multiplos valores // iterate (item : divmod(10, 3)) {
                                                                              //      print item;
                                                                              //  }
                env.push(new HashMap<>()); 
                addVariable(name, null); 
                for (Object item : (List) collection) {
                    updateVariable(name, item);
                    node.getBody().accept(this);
                }
                env.pop();
            } else if (collection != null && collection.getClass().isArray()) { // quando array é criado pelo new
                    env.push(new HashMap<>());                                  // vet = new Int[3];
                    addVariable(name, null);                               // iterate (n : vet) {
                    int length = java.lang.reflect.Array.getLength(collection);  //     print n;
                    for (int i = 0; i < length; i++) {                           // }                                 
                    Object item = java.lang.reflect.Array.get(collection, i);
                    updateVariable(name, item); 
                    node.getBody().accept(this);
                }
                env.pop();
            }
        }
    }

    
    @Override
    public void visit(Call node) {   
        try {
            Fun fun = funcs.get(node.getFuncName());
            for (Exp exp: node.getArgs()) {
                exp.accept(this);
            }
            
            fun.accept(this);
            Object returnValue = operands.pop(); // pode ser um único objeto ou uma lista
            node.getIndex().accept(this);
            int index = (Integer) operands.pop();

            if(returnValue instanceof List){ // se for multiplus retornos
                operands.push(((List) returnValue).get(index));
            }
            else if (index == 0){ // se for um único returno
                operands.push(returnValue);
            } else {
                throw new RuntimeException("indice de retorno fora dos limites para função com retorno único.");
            }
                
        } catch (Exception e) {
            throw new RuntimeException( " (" + node.getLine() + ", " + node.getCol() + ") " + e.getMessage() );
        }
    }

    
    @Override public void visit(TyInt node) {}
    @Override public void visit(TyChar node) {}
    @Override public void visit(TyBool node) {}
    @Override public void visit(TyFloat node) {}
    @Override public void visit(TyId node) {}
    @Override public void visit(TTypeArray node) {}
    @Override public void visit(ExpList node) {}
    @Override public void visit(Cmd node) { }
    @Override public void visit(ItCond node) {}
    @Override public void visit(Param node) {}
    
}