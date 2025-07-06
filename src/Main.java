import java.io.FileReader;
import java.io.Reader;
import java.io.IOException;;
import parser.Parser;
import parser.Lexer;
import ast.Prog;
import visitors.DotVisitor;
import visitors.InterpretVisitor;

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