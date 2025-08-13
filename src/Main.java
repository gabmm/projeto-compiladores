/*
 * UFJF - Universidade Federal de Juiz de Fora
 * Departamento de Ciência da Computação
 * Trabalho de compiladores - Linguagem Lang
 * Desenvolvido pelos alunos:
 *  - Gabriel Martins da Costa Medeiros - 201935032
 *  - Matheus Peron Resende Corrêa - 201965089C
 */

import java.io.FileReader;
import java.io.Reader;
import java.io.IOException;
import parser.Parser;
import parser.Lexer;
import ast.Prog;
import visitors.*;

public class Main {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Uso: make run ACTION=<diretiva> FILE=<caminho_do_arquivo>");
            System.err.println("Diretivas: -syn (análise sintática), -i (interpretar)");
            System.exit(1);
        }

        String directive = args[0];
        String filePath = args[1];

        try (Reader reader = new FileReader(filePath)) {
            Lexer lexer = new Lexer(reader);
            Parser parser = new Parser();
            //System.out.println("Executando analise sintatica em: " + filePath);
            Prog programNode = (Prog) parser.parse(lexer); 
            // System.out.println("Analise sintatica concluida com sucesso.");
            if (!parser.wasParsed()) {
                System.err.println("Rejected!");
                return;
            }
            System.out.println(("Accepted!"));
            switch (directive) {
                case "-syn":
                    // nao faz mais nada, a analise ja foi feita.
                    break;
                case "-i":
                   // System.out.println("--- Executando Interpretador ---");
                    InterpretVisitor interpreter = new InterpretVisitor();
                    programNode.accept(interpreter);
                  //  System.out.println("--- Fim da Execucao ---");
                    break;
                case "-dot":
                    DotVisitor dot = new DotVisitor();
                    programNode.accept(dot);
                    dot.saveToFile("ast.dot");
                    break;
                case "-t":
                    TypeCheckerVisitor typeChecker = new TypeCheckerVisitor();
                    programNode.accept(typeChecker);
                    typeChecker.printEnvs();
                    System.out.println("-----\n");
                    typeChecker.printData();
                    System.out.println("-----\n");
                    typeChecker.printErrors();
                    break;
                case "-src":
                    TypeCheckerVisitor tJava = new TypeCheckerVisitor();
                    programNode.accept(tJava);
                    tJava.printErrors();
                    JavaVisitor jv = new JavaVisitor(tJava.getEnvs());
                    programNode.accept(jv);
                    // jv.printProg();
                    jv.saveProgram();
                    break;
                case "-gen":
                    TypeCheckerVisitor tJasmin = new TypeCheckerVisitor();
                    programNode.accept(tJasmin);
                    tJasmin.printErrors();
                    JasminVisitor jasv = new JasminVisitor(tJasmin, tJasmin.getEnvs());
                    programNode.accept(jasv);
                    // jasv.printProg();
                    jasv.saveProgram();
                    break;
                default:
                    System.err.println("Diretiva desconhecida: " + directive);
                    System.exit(1);
            }
        } catch (IOException e) {
            System.err.println("Exceção de IO: " + e.getMessage());
        } catch (beaver.Parser.Exception e) {
            System.err.println("Exceção do parser: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Exceção do visitor: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}