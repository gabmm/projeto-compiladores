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
import langUtil.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.Scanner;

public class TypeCheckerVisitor extends Visitor {


    //inicializar variáveis de SType **singleton pattern**
    // serve para otimizar desempenho

    private STyInt tyInt = STyInt.initSTyInt();
    private STyFloat tyFloat = STyFloat.initSTyFloat();
    private STyChar tyChar = STyChar.initSTyChar();
    private STyBool tyBool = STyBool.initSTyBool();

    private ArrayList<String> log;

    // mapeamento de nome de funcao -> ambiente;
    // essa classe nada mais que manipula um TreeMap
    private TyEnv<LocalEnv<SType>> envs;

    // ambiente que estou agora:
    // ex.: estou em uma atribuicao qualquer, onde vou salvar?
    // r. no map da funcao que estou, a current
    private LocalEnv<SType> current;

    private LinkedHashMap<String, STyData> dataDefs;

    private operandControler<SType> operands;
    private boolean hasReturn;

    public String getColAndLine(Node node) {
        return node.getLine() + ", " + node.getCol() + ": ";
    }

    public void printEnvs() {
        envs.printMap();
    }

    public void printData() {
        for (STyData data : dataDefs.values()) {
            System.out.println(data.toStringFull());
        }
    }

    public void printErrors() {
        for (String err : log) {
            System.out.println(err);
        }
    }

    public TypeCheckerVisitor() {
        operands = new operandControler<SType>(false);
        envs = new TyEnv<LocalEnv<SType>>();
        log = new ArrayList<String>();
        dataDefs = new LinkedHashMap<String, STyData>();
    }

    // private Object findVariableAux(String varName){
    //     for (int i = env.size() - 1; i >= 0; i--) {
    //         if (env.get(i).containsKey(varName)) {
    //             return env.get(i).get(varName);
    //         }
    //     }
    //     return null;
    // }
 
    // private Object findVariable(String varName) { // encontra valor atual da variável
    //     Object returnValue = findVariableAux(varName);

    //     //if (returnValue != null){
    //         return returnValue;
    //     //}

    //     //throw new RuntimeException("Erro: Variavel '" + varName + "' nao declarada.");
    // }

    private void addVariable(String varName, Object value) { // adiciona a variável criada pela primeira vez no escopo
        
        //     if (env.peek().containsKey(varName)) {
        //         throw new RuntimeException("Erro Semantico: Redefinicao ilegal da variavel '" + varName + "'.");
        //     }
        // env.peek().put(varName, value);
    }
    
    private void updateVariable(String varName, Object value) { // altera valor da variavel que já tenha sido inicializada
        // for (int i = env.size() - 1; i >= 0; i--) {
        //     if (env.get(i).containsKey(varName)) {
        //         env.get(i).put(varName, value);
        //         return;
        //     }
        // }
        // //throw new RuntimeException("Erro: Variavel '" + varName + "' nao declarada para atribuicao.");
    }

    private void addOrUpdateVariable(String varName, Object value){
        // Object returnValue = findVariableAux(varName);
        // if (returnValue != null){
        //     updateVariable(varName, value);
        // } else {
        //     addVariable(varName, value);
        // }
    }

    private boolean isInteger(Object number) {
        return number instanceof Integer;
    }

    @Override
    public void visit(Prog node) {
        for (Def def : node.getDefs()) {
            def.accept(this);
        }

        for (Def def : node.getDefs()) {
            if (def instanceof Fun) {
                def.accept(this);
            }
        }

        // for (Def def : node.getDefs()) {
        //     def.accept(this);
        // }  
        // Fun mainFunc = funcs.get("main");
        // env.push(new HashMap<>()); 
        // mainFunc.getBody().accept(this);
        // env.pop(); 
    }

   @Override
    public void visit(Fun node) {
        if (!envs.hasKey(node.getName())) {
            STyFun funType;

            SType[] fparams = new SType[node.getParams().size()];
            for (int i = 0; i < node.getParams().size(); i++) {
                node.getParams().get(i).getType().accept(this);
                fparams[i] = operands.pop();
            }

            SType[] frets = new SType[node.getType().size()];
            for (int i = 0; i < node.getType().size(); i++) {
                node.getType().get(i).accept(this);
                frets[i] = operands.pop();
            }

            funType = new STyFun(fparams, frets);
            envs.put(node.getName(), new LocalEnv<SType>(node.getName(), funType));
            return;
        }

        // hasReturn = false;
        current = envs.get(node.getName());
        for (Param p : node.getParams()) {
            p.getType().accept(this);
            current.put(p.getID(), operands.pop());
        }
        node.getBody().accept(this);
        // if (!returnMode) {operands.push(null);}
        // returnMode = false;
    }

   
    @Override
    public void visit(Block node) {
        // if (returnMode) return;
        // env.push(new HashMap<>());
        for (Cmd cmd : node.getCmds()) {
            cmd.accept(this);
            // if (returnMode) break;
        }
        // env.pop();
    }
    
    @Override
    public void visit(CmdList node) {
        // for (Cmd cmd : node.getCommands()) {
        //     if(returnMode){return;}
        //     cmd.accept(this);
        // }
    }
    // private Object evaluateLValue(LValue lvalue) {
    //    // System.out.println("lvalue: " + lvalue);
    //     if (lvalue instanceof Var) {
    //       //  System.out.println("lvalue: " + lvalue);
    //         return findVariable(((Var) lvalue).getName());
    //     }
        
    //     if (lvalue instanceof Dot) {
    //         Dot dot = (Dot) lvalue;
    //         HashMap<String, Object> aux = (HashMap<String, Object>) evaluateLValue(dot.getBase());
    //         return aux.get(dot.getField());
    //     }
        
    //     if (lvalue instanceof ArrayAccess) {
    //         ArrayAccess accessNode = (ArrayAccess) lvalue;
    //         Object collection = evaluateLValue(accessNode.getArray());
    //         accessNode.getIndex().accept(this);
    //         int index = (Integer) operands.pop();
    //         if (collection instanceof List) {
    //             return ((List) collection).get(index);
    //         }
    //     }
        
    //     throw new RuntimeException("Tipo de LValue desconhecido para avaliacao.");
    // }

    private int countArrayAccesses(LValue arr) {
        int counter = 0;

        if (arr instanceof ArrayAccess) {
            counter++;
        } else {
            return 0;
        }

        LValue aux = ((ArrayAccess) arr).getArray();

        while (aux instanceof ArrayAccess) {
            counter++;
            aux = ((ArrayAccess) aux).getArray();
        }

        return counter;
    }

    @Override
    public void visit(Assign node) {
        node.getRhs().accept(this);
        if (node.getLhs() instanceof Var) {
            Var left = (Var) node.getLhs();
            SType right = operands.pop();
            current.put(left.getName(), right);
            // log.add(getColAndLine(node) + "Nova variável " + left.getName() + " do tipo "
            // + right.toString());
        } else {
            node.getLhs().accept(this);
            SType left = operands.pop();
            SType right = operands.pop();
            int narray = countArrayAccesses(node.getLhs());

            System.out.println(narray);
            for (int i = 0; i < narray; i++) {
                left = ((STyArr) left).getType();
            }

            // log.add(getColAndLine(node) + "Atribuicao de " + left.toString() + " para " +
            // right.toString());

            if (!left.match(right)) {
                log.add(getColAndLine(node) + "Atribuicao de " + left.toString() + " para " + right.toString());
            }
        }
    }

    @Override
    public void visit(Return node) {
        // if (returnMode) return;
        // if (node.getExps() == null || node.getExps().isEmpty()) {
        //     operands.push(null); 
        // } else if (node.getExps().size() == 1) { // só um retorno
        //     node.getExps().get(0).accept(this); 
        // } else { // vários retornos         
        //     ArrayList<Object> returnValues = new ArrayList<>();
        //     for (Exp exp : node.getExps()) {
        //         exp.accept(this);
        //         // returnValues.add(0, operands.pop());
        //         returnValues.add(operands.pop());
        //     }
        //     operands.push(returnValues);
        // }
        // returnMode = true;
    }

    @Override
    public void visit(If node) {
        // if (returnMode) return;
        // node.getCondition().accept(this);
        // if ((Boolean) operands.pop()) {
        //     node.getThenCmd().accept(this);
        // } else if (node.getElseCmd() != null) {
        //     node.getElseCmd().accept(this);
        // }
    }

    @Override
    public void visit(CallStmt node) {
    //    // System.out.println("INICIANDO CALLSTMT");
    //     try {
    //         Fun fun = funcs.get(node.getID()); // acho a funcao
    //     if (fun == null) throw new RuntimeException("Função não encontrada: " + node.getID());
    //     List<Param> funcParams = fun.getParams();
    //     if (fun != null) {
    //         for (Exp arg : node.getArgs()) {
    //             arg.accept(this); // empilho os argumentos
    //         }
            
    //         env.push(new HashMap<String, Object>());

    //         for (int i = funcParams.size() - 1; i >= 0; i--) {
    //             env.peek().put(funcParams.get(i).getID(), operands.pop());
    //         }
    //         if (fun.getBody() == null) {
    //                 throw new RuntimeException("Erro: A funcao '" + fun.getName() + "' nao tem um corpo para ser executado.");
    //             }
    //         fun.getBody().accept(this);

    //         Object returnValue = operands.pop();

    //         env.pop();
    //         returnMode = false;
    //         List<LValue> returnLValues = node.getReturns();
    //         if (returnLValues != null && !returnLValues.isEmpty()) {
    //             // Se a função retornou múltiplos valores (uma Lista)
    //             if (returnValue instanceof List) {
    //                 List<Object> returns = (List<Object>) returnValue;
    //                 for (int i = 0; i < returnLValues.size(); i++) {
    //                     String varName = ((Var) returnLValues.get(i)).getName();
    //                     updateVariable(varName, returns.get(i));
    //                 }
    //             } else { // Se a função retornou um único valor
    //                 String varName = ((Var) returnLValues.get(0)).getName();
    //                 updateVariable(varName, returnValue);
    //             }
    //         }
    //     }
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //         throw new RuntimeException("Erro durante a execução da função '" + node.getID() + "'.", e);
    //     }
        
    }
    
    @Override
    public void visit(Add node) {
        // node.getLeft().accept(this);
        // node.getRight().accept(this);
        // Number left, right;
        // right = (Number) operands.pop();
        // left = (Number) operands.pop();
        // if (isInteger(right) && isInteger(left)) {
        //     operands.push(Integer.valueOf(left.intValue() + right.intValue()));
        // } else {
        //     operands.push(Float.valueOf(left.floatValue() + right.floatValue()));
        // }
    }

    @Override
    public void visit(And node) {
        // node.getLeft().accept(this);
        // node.getRight().accept(this);
        // Boolean left, right;
        // right = (Boolean) operands.pop();
        // left = (Boolean) operands.pop();
        // operands.push(Boolean.valueOf(right && left));
    }

    @Override
    public void visit(Or node) {
        // node.getLeft().accept(this);
        // boolean left = (Boolean) operands.pop();
        // if (left) { 
        //     operands.push(true);
        //     return;
        // }
        // node.getRight().accept(this);
        // operands.push(operands.pop());
    }

    @Override
    public void visit(Eq node) {
        // node.getLeft().accept(this);
        // node.getRight().accept(this);
        // operands.push(Boolean.valueOf(operands.pop().equals(operands.pop())));
    }

    @Override
    public void visit(Neq node) {
        // node.getRight().accept(this);
        // node.getLeft().accept(this);
        // operands.push(!Boolean.valueOf(operands.pop().equals(operands.pop())));
    }
    
    @Override
    public void visit(Var node) {
        SType varType = current.get(node.getName());
        if (varType == null) {
            log.add(getColAndLine(node) + "Variável " + node.getName() + "não declarada");
        } else {
            operands.push(varType);
        }

        // // Object value = env.peek().get(node.getName());
        // Object value = findVariable(node.getName());
        // operands.push(value);
    }
    
    @Override
    public void visit(NInt node) {
        operands.push(tyInt);
        // operands.push(Integer.valueOf(node.getValue()));
    }

    @Override
    public void visit(NFloat node) {
        operands.push(tyFloat);
        // operands.push(Float.valueOf(node.getValue()));
    }

    @Override
    public void visit(NBool node) {
        operands.push(tyBool);
        // operands.push(Boolean.valueOf(node.getValue()));
    }

    @Override
    public void visit(NChar node) {
        operands.push(tyChar);
        // operands.push(Character.valueOf(node.getValue()));
    }

    @Override
    public void visit(Null node) {
        // operands.push(null); 
    }

    // private ArrayList<Object> createMultiArray(TType typeNode, List<Exp> dimensions) { // recursivo
    //     if (!(typeNode instanceof TTypeArray)) { 
    //         return null; 
    //     }
        
    //     TTypeArray arrayType = (TTypeArray) typeNode;
        
    //     if (dimensions == null || dimensions.isEmpty()) {
    //         return null; 
    //     }
        
    //     dimensions.get(0).accept(this);
    //     int size = (Integer) operands.pop(); 
        
    //     List<Exp> dimensionAux = dimensions.subList(1, dimensions.size());
    //     ArrayList<Object> newArray = new ArrayList<>(); 
        
    //     for (int i = 0; i < size; i++) { 
    //        Object subVet = createMultiArray(arrayType.getBase(), dimensionAux);
    //         newArray.add(subVet); 
    //     }
    //     return newArray;
    // }

    @Override
    public void visit(New node) {
        node.getType().accept(this);

    //     //System.out.println("Type: " + node.getType());
    //     TType type = node.getType();
    //     List<Exp> dimensions = node.getDimensions();
    //    // TType t2 = t.getBase();

    //    if (dimensions.isEmpty()){
    //         if (type instanceof TyId) {
    //            // System.out.println("eh TyId: " + node.getType());
    //             String dataName = ((TyId) node.getType()).getName();
    //             Data dataDef = dataDefs.get(dataName);
    //             HashMap<String, Object> newInstance = new HashMap<>();
    //             for (Decl field : dataDef.getDecls()) {
    //                 newInstance.put(field.getName(), null);
    //             }
    //             operands.push(newInstance);
    //         } 
    //    }
    //    else{
    //        // System.out.println("Eh array: " + type);
    //         TType typeAux = type;
    //         for(int i = 0; i < dimensions.size(); i++){ 
    //             typeAux = new TTypeArray(typeAux);
    //         }    
    //         ArrayList<Object> newArray = createMultiArray(typeAux, dimensions);
    //        // System.out.println("novo array: " + newArray);
    //         operands.push(newArray);        
    //    }
        
    }

    @Override
    public void visit(ArrayAccess node) {
        node.getIndex().accept(this);
        SType index = operands.pop();
        if (!index.match(tyInt)) {
            log.add(getColAndLine(node) + "Tentativa de acessar array com tipo " + index.toString()
                    + ". Espera-se INT");
        }
        node.getArray().accept(this);
    }

    /*
     * Estratégia do DOT aninhado: Vai voltando até chegar a var quando encontra
     * var, faz o caminho em direção ao fim enquanto faz esse caminho vai colocando
     * o type do field e pronto! temos o tipo do field no final
     * meuTriangulo.ladoA.inicio.x = 2;
     * 
     * CHAMADA DOT 1 base = meuTriangulo.ladoA.inicio field = x pop no STYDATA do
     * inicio -> Ponto confere pra ver se Ponto tem field x; se tem push no STYPE de
     * x;
     * 
     * CHAMADA DOT 2 base = meuTriangulo.ladoA field = inicio pop no STYDATA ladoA
     * -> linha confere pra ver se linha tem field inicio se tem push no STYDATA do
     * inicio
     * 
     * CHAMADA DOT 3 base = meuTriangulo field = ladoA dá pop do SYDATA meuTriangulo
     * -> Triangulo confere se Triangulo tem field ladoA se tem push no STYDATA
     * ladoA
     * 
     * 
     * CHAMADA VAR base = Var(meuTriangulo) => retorna o STyData do meuTriangulo pra
     * chamada dot acima
     */

    @Override
    public void visit(Dot node) {
        node.getBase().accept(this);

        SType dotBase = operands.pop();

        if (dotBase instanceof STyArr) {
            dotBase = ((STyArr) dotBase).getType();
        }

        STyData dotBaseData = (STyData) dotBase;
        if (!dotBaseData.hasField(node.getField())) {
            System.err.println("NAO TEM FIELD");
        } else {
            operands.push(dotBaseData.getFieldType(node.getField()));
        }

        // node.getBase().accept(this);
        // HashMap<String, Object> object = (HashMap<String, Object>) operands.pop();
        // operands.push(object.get(node.getField()));
    }
    
    @Override
    public void visit(Data node) {
        if (!dataDefs.containsKey(node.getName())) {
            LinkedHashMap<String, SType> dataFields = new LinkedHashMap<String, SType>();
            for (Decl decl : node.getDecls()) {
                decl.getType().accept(this); // alterar para entrar no do DECL
                dataFields.put(decl.getName(), operands.pop());
            }
            STyData data = new STyData(node.getName(), dataFields);
            dataDefs.put(node.getName(), data);
        }

        if (node.getFuns().size() != 0) {
            for (Fun fun : node.getFuns()) {
                fun.accept(this);
            }
        }

    //     if (!dataDefs.containsKey(node.getName())) { // armazena decl
    //         dataDefs.put(node.getName(), node);
    //     }
    //     List<Fun> func = node.getFuns();
    //     if (func != null && !func.isEmpty()) { // Abstract data - armazena  fun 
    //         String name = node.getName();
    //         for (Fun f : func) {
    //             String funcDotName = name + "." + f.getName(); // para metodos função.nome()
    //             funcs.put(funcDotName, f);
    //         }
    // }
    }
    @Override 
    public void visit(ItCondExp node) {
        // node.getCond().accept(this);
     }
    @Override 
    public void visit(ItCondId node) {
        // node.getCond().accept(this);
     }

    @Override
    public void visit(Attr node) { // acho que não precisa dessa, seria igual o assign 
        
    }
    @Override 
     public void visit(FieldAccess node) { // acho que não precisa dessa, seria mesma coisa que o DOT
        // node.getTarget().accept(this);
        // HashMap<String, Object> ob = ( HashMap<String, Object>) operands.pop();
        // operands.push(ob.get(node.getField()));
    }

    @Override
    public void visit(Sub node) {
        // node.getLeft().accept(this);
        // node.getRight().accept(this);
        // Number left, right;
        // right = (Number) operands.pop();
        // left = (Number) operands.pop();
        // if (isInteger(right) && isInteger(left)) {
        //     operands.push(Integer.valueOf(left.intValue() - right.intValue()));
        // } else {
        //     operands.push(Float.valueOf(left.floatValue() - right.floatValue()));
        // }
    }

    @Override
    public void visit(Mul node) {
        // node.getLeft().accept(this);
        // node.getRight().accept(this);
        // Number left, right;
        // right = (Number) operands.pop();
        // left = (Number) operands.pop();
        // if (isInteger(right) && isInteger(left)) {
        //     operands.push(Integer.valueOf(left.intValue() * right.intValue()));
        // } else {
        //     operands.push(Float.valueOf(left.floatValue() * right.floatValue()));
        // }
    }

    @Override
    public void visit(Div node) {
        // node.getLeft().accept(this);
        // node.getRight().accept(this);
        // Number left, right;
        // right = (Number) operands.pop();
        // left = (Number) operands.pop();
        // if (isInteger(right) && isInteger(left)) {
        //     operands.push(Integer.valueOf(left.intValue() / right.intValue()));
        // } else {
        //     operands.push(Float.valueOf(left.floatValue() / right.floatValue()));
        // }
    }

    @Override
    public void visit(Mod node) {
        // node.getLeft().accept(this);
        // node.getRight().accept(this);
        // int left, right;
        // right = (Integer) operands.pop();
        // left = (Integer) operands.pop();
        // operands.push(Integer.valueOf(left % right));
    }

    @Override
    public void visit(Lt node) {
        // try {
        //     node.getLeft().accept(this);
        //     node.getRight().accept(this);
        //     Number right = (Number) operands.pop();
        //     Number left = (Number) operands.pop();
        //     operands.push(left.floatValue() < right.floatValue());
        // } catch (Exception e) {
        //      e.printStackTrace();
        //     throw new RuntimeException( " (" + node.getLine() + ", " + node.getCol() + ") " + e.getMessage() );
        // }
    }

    @Override
    public void visit(NNot node) {
        // node.getExp().accept(this);
        // operands.push(!(Boolean) operands.pop());
    }

    @Override
    public void visit(Neg node) {
        // node.getExp().accept(this);
        // Number exp = (Number) operands.pop();
        // if (isInteger(exp)) {
        //     operands.push(-1 * Integer.valueOf(exp.intValue()));
        // } else {
        //     operands.push(-1 * Float.valueOf(exp.floatValue()));
        // }
    }


    @Override
    public void visit(Decl node) {
        // addVariable(node.getName(), null);
    }

    @Override
    public void visit(Print node) {
        // if (returnMode) return;
        // node.getExpr().accept(this);
        // System.out.println(operands.pop());
    }

    @Override
public void visit(Iterate node) {
    // if (returnMode) return;

    // ItCond cond = node.getCondition();
    // if (cond instanceof ItCondExp) {
    //     ItCondExp itExp = (ItCondExp) cond;
    //     itExp.getCond().accept(this);
    //     Object result = operands.pop();
    //     if (!(result instanceof Integer)) {
    //         throw new RuntimeException("Erro: iterate(N) espera uma expressão inteira, mas recebeu " + result.getClass().getName());
    //     }

    //     int times = (Integer) result;
    //     for (int i = 0; i < times; i++) {
    //         node.getBody().accept(this);
    //         if (returnMode) {
    //             break;
    //         }
    //     }
    // }
    // else if (cond instanceof ItCondId) {
    //     ItCondId itId = (ItCondId) cond;
    //     String varName = itId.getID(); 

    //     itId.getCond().accept(this);
    //     Object collectionOrNumber = operands.pop();
    //     if (collectionOrNumber instanceof List) {
    //         List<?> collection = (List<?>) collectionOrNumber;
    //         env.push(new HashMap<>());
    //         addVariable(varName, null);

    //         for (Object item : collection) {
    //             updateVariable(varName, item); 
    //             node.getBody().accept(this);   
    //             if (returnMode) break;
    //         }

    //         env.pop(); 
    //     }
    //     else if (collectionOrNumber instanceof Integer) {
    //         int limit = (Integer) collectionOrNumber;

    //         env.push(new HashMap<>());
    //         addVariable(varName, null);

    //         for (int i = 0; i < limit; i++) {
    //             updateVariable(varName, i); 
    //             node.getBody().accept(this);
    //             if (returnMode) break;
    //         }

    //         env.pop();
    //     }
    //     else {
    //         String typeName = (collectionOrNumber != null) ? collectionOrNumber.getClass().getName() : "null";
    //         throw new RuntimeException("Erro: A expressão em iterate(id: expr) deve ser um array ou um inteiro, mas foi " + typeName);
    //     }
    // }
}

    
    @Override
    public void visit(Call node) {   

        // //System.out.println("INICIANDO CALL");
        // try {
            
        //     Fun fun = funcs.get(node.getFuncName());
        //     if (fun == null) throw new RuntimeException("Função não encontrada: " + node.getFuncName());
        //     for (Exp exp: node.getArgs()) {
        //         exp.accept(this);
        //        // System.out.println("getArgs: " + exp);
        //     }
        //     env.push(new HashMap<>());
        //     //HashMap<String, Object> aux = new HashMap<>();
        //     for (int i = fun.getParams().size() - 1; i >= 0; i--) {
        //         env.peek().put(fun.getParams().get(i).getID(), operands.pop());
        //     }
           
        //     if (fun.getBody() == null) {
        //         throw new RuntimeException("Erro: A função '" + fun.getName() + "' não tem um corpo para ser executado.");
        //     }
        //    // System.out.println("Body: " + fun.getBody());
        //     fun.getBody().accept(this);
        //   //   System.out.println("aaqui");
        //     Object returnValue = operands.pop(); // pode ser um único objeto ou uma lista
        //     env.pop();
        //     returnMode = false;
            
        //     node.getIndex().accept(this);
        //     int index = (Integer) operands.pop();
        //    // System.out.println("index: " + index);
        //     List<TType> returnTypes = fun.getType();
        //    // System.out.println("returnTypes: " + returnTypes);
        //     if (returnTypes != null && returnTypes.size() > 1) {
        //         if (!(returnValue instanceof List)) {
        //             throw new RuntimeException("Erro: Função com multiplos retornos não retornou uma lista.");
        //         }
        //         operands.push(((List) returnValue).get(index));
        //         } else {

        //             if (index != 0) {
        //                 throw new RuntimeException("Erro");
        //             }
        //           //  System.out.println("returnValue: " + returnValue);
        //             operands.push(returnValue);
        //           //  System.out.println("returnValue: " + returnValue);
        //         }       
        // } catch (Exception e) {
        //     e.printStackTrace();
        //     throw new RuntimeException( " (" + node.getLine() + ", " + node.getCol() + ") " + e.getMessage() );
        // }
    }
    @Override
        public void visit(Read node) {
            // if (returnMode) return;
            
    
            // int value = inputReader.nextInt();

          
            // LValue lvalue = node.getLValue();
            // if (lvalue instanceof Var) {
            //     String varName = ((Var) lvalue).getName();
            //     addOrUpdateVariable(varName, Integer.valueOf(value));
            // } else {
            //     throw new RuntimeException("Erro: 'read' só pode ser usado com variaveis simples.");
            // }
        }
    
        @Override
        public void visit(TyInt node) {
            operands.push(tyInt);
        }

        @Override
        public void visit(TyChar node) {
            operands.push(tyChar);
        }

        @Override
        public void visit(TyBool node) {
            operands.push(tyBool);
        }

        @Override
        public void visit(TyFloat node) {
            operands.push(tyFloat);
        }

        @Override
        public void visit(TyId node) {
            if (dataDefs.containsKey(node.getName())) {
                operands.push(dataDefs.get(node.getName()));
            } else {
                System.err.println("ERRO - DATA NAO DECLARADA!");
            }
        }

        @Override
        public void visit(TTypeArray node) {
            node.getBase().accept(this);
            operands.push(new STyArr(operands.pop()));
        }

    @Override public void visit(ExpList node) {}
    @Override public void visit(Cmd node) { }
    @Override public void visit(ItCond node) {}

    @Override
    public void visit(Param node) {

    }
    
}