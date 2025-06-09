import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        FileReader reader = new FileReader("input.txt");
        Lexer lexer = new Lexer(reader);
        Token token = lexer.nextToken();

        while (token != null) {
            System.out.println(token.toString());
            token = lexer.nextToken();
        }

        System.out.println("Total: " + lexer.readedTokens());
    }
}
