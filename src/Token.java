public class Token {
    public int line;
    public int column;
    public String lexeme;
    public Object info;
    public TOKEN_TYPE type;

    public Token (TOKEN_TYPE t, String lex, Object o, int l, int c){
        this.line = l;
        this.column = c;
        this.lexeme = lex;
        this.info = o;
        this.type = t;
    }

    public Token (TOKEN_TYPE t, String lex, int l, int c){
        this.line = l;
        this.column = c;
        this.lexeme = lex;
        this.info = null;
        this.type = t;
    }

    public Token (TOKEN_TYPE t, Object o, int l, int c){
        this.line = l;
        this.column = c;
        this.lexeme = "";
        this.info = o;
        this.type = t;
    }

    @Override
    public String toString(){
       return "Token{" +
            "type=" + type +
            ", lexeme='" + lexeme + '\'' +
            ", value=" + info +
            ", line=" + line +
            ", column=" + column +
            '}';
    }

}
