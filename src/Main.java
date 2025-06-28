import java.io.FileReader;
import java.io.StringReader;

import parser.Lexer;
import parser.Parser;
import ast.*;
// import visitors.PrintVisitor;
import visitors.*;
public class Main {
    public static void main(String[] args) {
        String filename = args[0];

        try (FileReader fr = new FileReader(filename)) {
            Lexer lexer = new Lexer(fr);
            Parser parser = new Parser();
            parser.parse(lexer);
        
            // try {
            //     Node ast = (Node) parser.parse(lexer);
            //     PrintVisitor pv = new PrintVisitor();
            //     ast.accept(pv);
            // }catch (Exception e){
            //     e.printStackTrace();
            // }
            
            System.out.println("accept");
        } catch (Exception e) {
            System.out.println("reject");
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
