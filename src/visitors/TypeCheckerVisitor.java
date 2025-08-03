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
    private STyError tyError = STyError.iniSTyError();

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
    private boolean hasReturnStmt;
    private boolean leftSided;
    private boolean isDeclarationPhase;

    public TyEnv<LocalEnv<SType>> getEnvs() {
        return this.envs;
    }

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
        hasReturnStmt = false;
        isDeclarationPhase = true;
    }

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
    public void visit(Prog node) {

        for (Def def : node.getDefs()) {
            def.accept(this);
        }

        if (!envs.hasKey("main")) {
            log.add(getColAndLine(node) + "Função main não definida");
        }

        isDeclarationPhase = false;

        for (Def def : node.getDefs()) {
            if (def instanceof Fun) {
                def.accept(this);
            }
        }
    }

   @Override
    public void visit(Fun node) {
        if (isDeclarationPhase) {

            if (envs.hasKey(node.getName())) {
                log.add(getColAndLine(node) + "Redeclaração da função " + node.getName());
                return;
            }

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

        hasReturnStmt = false;
        current = envs.get(node.getName());
        for (Param p : node.getParams()) {
            p.getType().accept(this);
            current.put(p.getID(), operands.pop());
        }
        node.getBody().accept(this);
        if (!hasReturnStmt && !node.getName().equals("main")) {
            log.add(getColAndLine(node) + "Função " + node.getName() + " não possui comando de retorno");
        }
    }

   
    @Override
    public void visit(Block node) {
        for (Cmd cmd : node.getCmds()) {
            cmd.accept(this);
        }
    }
    
    @Override
    public void visit(CmdList node) {
        for (Cmd cmd : node.getCommands()) {
            cmd.accept(this);
        }
    }

    @Override
    public void visit(Assign node) {
        node.getRhs().accept(this);

        /*
         * Se entra no IF, é atribuição para variável simples logo pode ser feita de
         * qualquer forma. Se entra no ELSE, então é array ou data Nesse caso a
         * atribuição deve ser do mesmo tipo
         */

        if (node.getLhs() instanceof Var) {
            Var left = (Var) node.getLhs();
            SType right = operands.pop();
            current.put(left.getName(), right);
        } else {
            leftSided = true;
            node.getLhs().accept(this);
            leftSided = false;
            SType left = operands.pop();
            SType right = operands.pop();

            int narray = countArrayAccesses(node.getLhs()); // dimensões do array
            for (int i = 0; i < narray; i++) {
                left = ((STyArr) left).getType();
            }

            if (!left.match(right)) {
                log.add(getColAndLine(node) + "Atribuição inválida de " + left.toString() + " para "
                        + right.toString());
            }
        }
    }

    @Override
    public void visit(Return node) {
        SType[] expectedReturns = ((STyFun) current.getType()).getReturns();

        if (expectedReturns.length != node.getExps().size()) {
            log.add(getColAndLine(node) + "Quantidade de retornos (" + node.getExps().size()
                    + ") encontrados para a função " + current.getFunctionID() + " incompatível. Esperado: "
                    + expectedReturns.length);
        } else {
            for (int i = 0; i < expectedReturns.length; i++) {
                node.getExps().get(i).accept(this);
                SType rType = operands.pop();
                if (!rType.match(expectedReturns[i])) {
                    log.add(getColAndLine(node) + "Retorno (" + i + ") da função " + current.getFunctionID()
                            + " incompatível. Tipo tentado: " + rType.toString() + " Esperado: "
                            + expectedReturns[i].toString());
                }
            }
        }

        hasReturnStmt = true;
    }

    @Override
    public void visit(If node) {
        boolean retOnThen = false;
        boolean retOnElse = true; // caso não houver else, não atrapalha

        node.getCondition().accept(this);
        SType condType = operands.pop();

        if (!condType.match(tyBool)) {
            log.add(getColAndLine(node) + "Condição do IF deve ser to tipo BOOL. Tipo encontrado: "
                    + condType.toString());
        }

        hasReturnStmt = false;
        node.getThenCmd().accept(this);
        retOnThen = hasReturnStmt;

        if (node.getElseCmd() != null) {
            hasReturnStmt = false;
            node.getElseCmd().accept(this);
            retOnElse = hasReturnStmt;
        }

        // atualiza valor do return
        hasReturnStmt = retOnElse && retOnThen;
    }

    @Override
    public void visit(CallStmt node) {
        if (!envs.hasKey(node.getID())) {
            log.add(getColAndLine(node) + "Função " + node.getID() + " não declarada");
            return;
        }

        STyFun funType = (STyFun) envs.get(node.getID()).getType();

        if (funType.getParams().length != node.getArgs().size()) {
            log.add(getColAndLine(node) + "Número de argumentos (" + node.getArgs().size()
                    + ") incompatível com a chamada da função " + node.getID() + ". Número esperado: "
                    + funType.getParams().length);
        } else {
            for (int i = 0; i < node.getArgs().size(); i++) {
                node.getArgs().get(i).accept(this);
                SType arg = operands.pop();
                SType param = funType.getParams()[i];
                if (!arg.match(param)) {
                    log.add(getColAndLine(node) + "Tipo do argumento " + i + " " + arg.toString()
                            + " incompatível com a função " + node.getID() + ". Tipo esperado: " + param.toString());
                }
            }
        }

        if (funType.getReturns().length != node.getReturns().size()) {
            log.add(getColAndLine(node) + "Número de retornos (" + node.getReturns().size()
                    + ") incompatível com a chamada da função " + node.getID() + ". Número esperado: "
                    + funType.getReturns().length);
        } else {
            for (int i = 0; i < node.getReturns().size(); i++) {
                LValue ret = node.getReturns().get(i);
                SType funRet = funType.getReturns()[i];
                if (ret instanceof Var) {
                    if (!current.hasKey(((Var) ret).getName())) {
                        log.add(getColAndLine(node) + "Varíavel de retorno " + ((Var) ret).getName()
                                + " não declarada durante chamada da função " + node.getID());
                    }
                    current.put(((Var) ret).getName(), funRet);
                } else {
                    ret.accept(this);
                    SType retType = operands.pop();
                    int narray = countArrayAccesses(ret);
                    for (int j = 0; j < narray; j++) {
                        retType = ((STyArr) retType).getType();
                    }
                    if (!retType.match(funRet)) {
                        log.add(getColAndLine(node) + "Tentativa de retornar (" + i + ") " + funRet.toString()
                                + " para variável do tipo " + retType.toString() + " durante chamada da função "
                                + node.getID());
                    }
                }
            }
        }
    }

    private void checkArithmeticBinOp(Node node, String op) {
        SType leftExp = operands.pop();
        SType rightExp = operands.pop();

        if (leftExp.match(tyInt) && leftExp.match(rightExp)) {
            operands.push(tyInt);
        } else if (!op.equals("MOD") && leftExp.match(tyFloat) && leftExp.match(rightExp)) {
            operands.push(tyFloat);
        } else {
            log.add(getColAndLine(node) + "Operação " + op + " não permitida para os tipos: " + leftExp.toString()
                    + " e " + rightExp.toString());
        }
    }

    private void checkBooleanBinOp(Node node, String op) {
        SType leftExp = operands.pop();
        SType rightExp = operands.pop();

        if (leftExp.match(rightExp) && (leftExp.match(tyInt) || leftExp.match(tyFloat) || leftExp.match(tyChar))) {
            operands.push(tyBool);
        } else {
            operands.push(tyError);
            log.add(getColAndLine(node) + "Operação " + op + " não permitida para os tipos: " + leftExp.toString()
                    + " e " + rightExp.toString());
        }
    }

    @Override
    public void visit(Add node) {
        node.getRight().accept(this);
        node.getLeft().accept(this);
        checkArithmeticBinOp(node, "ADD");
    }
    @Override
        public void visit(And node) {
            node.getRight().accept(this);
            node.getLeft().accept(this);
            checkBooleanBinOp(node, "AND");
        }
    @Override
    public void visit(Eq node) {
        node.getRight().accept(this);
        node.getLeft().accept(this);
        checkBooleanBinOp(node, "EQ");
    }

    @Override
    public void visit(Neq node) {
        node.getRight().accept(this);
        node.getLeft().accept(this);
        checkBooleanBinOp(node, "NEQ");
    }
    
    @Override
    public void visit(Var node) {
        SType varType = current.get(node.getName());
        if (varType == null) {
            log.add(getColAndLine(node) + "Variável " + node.getName() + "não declarada");
        } else {
            operands.push(varType);
        }
    }
    
    @Override
    public void visit(NInt node) {
        operands.push(tyInt);
    }

    @Override
    public void visit(NFloat node) {
        operands.push(tyFloat);
    }

    @Override
    public void visit(NBool node) {
        operands.push(tyBool);
    }

    @Override
    public void visit(NChar node) {
        operands.push(tyChar);
    }

    @Override
    public void visit(Null node) {
        // operands.push(null); 
    }

    @Override
    public void visit(New node) {
        node.getType().accept(this);
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

    @Override
    public void visit(Dot node) {
        node.getBase().accept(this);

        SType dotBase = operands.pop();

        if (dotBase instanceof STyArr) {
            dotBase = ((STyArr) dotBase).getType();
        }

        STyData dotBaseData = (STyData) dotBase;

        if (!dotBaseData.hasField(node.getField())) {
            log.add(getColAndLine(node) + "Campo " + node.getField() + " não encontrado para o tipo "
                    + dotBase.toString());
            operands.push(tyError);
        } else {
            operands.push(dotBaseData.getFieldType(node.getField()));
        }

        if (leftSided && dotBaseData.getRelatedFunctions() != null) { // é abstrato
            String[] relatedFunctions = dotBaseData.getRelatedFunctions();
            boolean canAccessFields = false;
            for (int i = 0; i < relatedFunctions.length; i++) {
                if (current.getFunctionID().equals(relatedFunctions[i])) {
                    canAccessFields = true;
                }
            }
            if (!canAccessFields) {
                log.add(getColAndLine(node) + "Não é possível alterar o valor do campo '" + node.getField()
                        + "' no contexto da função " + current.getFunctionID());
            }
        }
    }
    
    @Override
    public void visit(Data node) {

        if (isDeclarationPhase) {
            if (dataDefs.containsKey(node.getName())) {
                log.add(getColAndLine(node) + "Redeclaração do registro " + node.getName());
                return;
            }

            LinkedHashMap<String, SType> dataFields = new LinkedHashMap<String, SType>();
            for (Decl decl : node.getDecls()) {
                decl.accept(this);
                dataFields.put(decl.getName(), operands.pop());
            }
            STyData data;
            if (node.isAbstract()) {
                String[] relatedFuns = new String[node.getFuns().size()];
                for (int i = 0; i < node.getFuns().size(); i++) {
                    relatedFuns[i] = node.getFuns().get(i).getName();
                }
                data = new STyData(node.getName(), dataFields, relatedFuns);
            } else {
                data = new STyData(node.getName(), dataFields);
            }
            dataDefs.put(node.getName(), data);
        } else if (isDeclarationPhase) {
            log.add("DECLAR DE TIPO");
        }

        if (node.getFuns().size() != 0) {
            for (Fun fun : node.getFuns()) {
                fun.accept(this);
            }
        }
    }

    @Override 
    public void visit(ItCondExp node) {
        node.getCond().accept(this);
        SType condExp = operands.pop();

        if (!(condExp.match(tyInt) || condExp instanceof STyArr)) {
            log.add(getColAndLine(node) + "Condição para iteração é do tipo " + condExp.toString()
                    + ". Esperado: INT ou []");
        }
     }

    @Override 
    public void visit(ItCondId node) {
        /*
         * Caso Exp seja INT: ID tem que ser INT ou Não existir no contexto de tipos
         * 
         * Caso Exp seja Array: ID tem que ser do mesmo tipo do Array ou Nao existir no
         * contexto de tipos
         */

        node.getCond().accept(this);
        SType condExp = operands.pop();
        SType varType;

        if (condExp.match(tyInt)) {
            if (current.hasKey(node.getID())) {
                varType = current.get(node.getID());
                if (!varType.match(tyInt)) {
                    log.add(getColAndLine(node) + "Variável de iteração é do tipo " + varType.toString()
                            + "Esperado: INT");
                }
            } else {
                current.put(node.getID(), tyInt);
            }
        } else if (condExp instanceof STyArr) {
            if (current.hasKey(node.getID())) {
                varType = current.get(node.getID());
                if (!((STyArr) condExp).getType().match(varType)) {
                    log.add(getColAndLine(node) + "Variável de iteração é do tipo " + varType.toString() + "Esperado: "
                            + ((STyArr) condExp).getType().toString());
                } else {
                    current.put(node.getID(), ((STyArr) condExp).getType());
                }
            }
        } else {
            log.add(getColAndLine(node) + "Condição para iteração é do tipo " + condExp.toString()
                    + ". Esperado: INT ou []");
        }
     }

    @Override
    public void visit(Sub node) {
        node.getRight().accept(this);
        node.getLeft().accept(this);
        checkArithmeticBinOp(node, "SUB");
    }

    @Override
    public void visit(Mul node) {
        node.getRight().accept(this);
        node.getLeft().accept(this);
        checkArithmeticBinOp(node, "MUL");
    }

    @Override
    public void visit(Div node) {
        node.getRight().accept(this);
        node.getLeft().accept(this);
        checkArithmeticBinOp(node, "DIV");
    }

    @Override
    public void visit(Mod node) {
        node.getRight().accept(this);
        node.getLeft().accept(this);
        checkArithmeticBinOp(node, "MOD");
    }

    @Override
    public void visit(Lt node) {
        node.getRight().accept(this);
        node.getLeft().accept(this);
        checkBooleanBinOp(node, "LT");
    }

    @Override
    public void visit(NNot node) {
        node.getExp().accept(this);
        SType expType = operands.pop();
        if (expType.match(tyBool)) {
            operands.push(tyBool);
        } else {
            log.add(getColAndLine(node) + "Operacao NOT não permitida para o tipo: " + expType.toString());
            operands.push(tyError);
        }
    }

    @Override
    public void visit(Neg node) {
        node.getExp().accept(this);
        SType expType = operands.pop();
        if (expType.match(tyInt)) {
            operands.push(tyInt);
        } else if (expType.match(tyFloat)) {
            operands.push(tyFloat);
        } else {
            log.add(getColAndLine(node) + "Operacao NEG nao permitida para o tipo: " + expType.toString());
            operands.push(tyError);
        }
    }


    @Override
    public void visit(Decl node) {
        node.getType().accept(this);
    }

    @Override
    public void visit(Print node) {
        node.getExpr().accept(this);
        SType expType = operands.pop();
        if (!(expType.match(tyInt) || expType.match(tyFloat) || expType.match(tyChar) || expType.match(tyBool))) {
            log.add(getColAndLine(node) + "Operacao NEG nao permitida para o tipo: " + expType.toString());
        }
    }

    /*
     * Necessário preservar o contexto eu clono o contexto, o altero como quiser
     * retorno o contexto. Se eu declaro uma variavel la dentro, na volta ela nao
     * pode existir se eu altero o tipo de uma var la dentro, na volta ela tem q ter
     * o tipo original
     */

    @Override
    public void visit(Iterate node) {
        LocalEnv<SType> aux = current.cloneEnv(); // ignorando modificações no contexto de tipos
        node.getCondition().accept(this);
        node.getBody().accept(this);
        current = aux;
        envs.put(current.getFunctionID(), aux);
    }

    
    @Override
    public void visit(Call node) {
        // uma exp sempre deve colocar algo na pilha, portanto, quando não tiver retorno
        // ou nao for declarda, da push no typeerror

        if (!envs.hasKey(node.getFuncName())) {
            log.add(getColAndLine(node) + "Função " + node.getFuncName() + " não declarada");
            return;
        }

        STyFun funType = (STyFun) envs.get(node.getFuncName()).getType();
        SType[] retTypes = funType.getReturns();

        if (retTypes.length == 0) {
            log.add(getColAndLine(node) + "Funcao " + node.getFuncName() + " nao possui retornos");
            operands.push(tyError);
            return;
        }

        if (funType.getParams().length != node.getArgs().size()) {
            log.add(getColAndLine(node) + "Numero de argumentos (" + node.getArgs().size()
                    + ") incompativel com a chamada da funcao " + node.getFuncName() + ". Numero esperado: "
                    + funType.getParams().length);
        } else {
            for (int i = 0; i < node.getArgs().size(); i++) {
                node.getArgs().get(i).accept(this);
                SType arg = operands.pop();
                SType param = funType.getParams()[i];
                if (!arg.match(param)) {
                    log.add(getColAndLine(node) + "Tipo do argumento " + i + " " + arg.toString()
                            + " incompativel com a função " + node.getFuncName() + ". Tipo esperado: "
                            + param.toString());
                }
            }
        }

        node.getIndex().accept(this);
        SType ret = operands.pop();
        if (!ret.match(tyInt)) {
            log.add(getColAndLine(node) + "Tipo do indice do retorno da chamada da funcao " + node.getFuncName() + " é "
                    + ret.toString() + ". Esperado: " + tyInt.toString());
            operands.push(tyError);
        } else {
            if (!(node.getIndex() instanceof NInt)) {
                log.add(getColAndLine(node) + "indice do retorno da chamada da funcao " + node.getFuncName()
                        + " nao e um literal inteiro!");
                operands.push(tyError);
            } else {
                operands.push(retTypes[((NInt) node.getIndex()).getValue()]);
            }
        }
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