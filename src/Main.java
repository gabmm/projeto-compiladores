import java.io.FileReader;
import parser.Lexer;
import parser.Parser;

public class Main {
    public static void main(String[] args) {
        String filename = args[0];

        try (FileReader fr = new FileReader(filename)) {
            Lexer lexer = new Lexer(fr);
            Parser parser = new Parser();
            parser.parse(lexer);

            System.out.println("accept");
        } catch (Exception e) {
            System.out.println("reject");
            System.err.println(e.getMessage());
        }
    }
}
