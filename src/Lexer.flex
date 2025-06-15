
%%

%class Lexer
%unicode
%function nextToken
%type Token
%line
%column

%{

    private int ntk;

    public int readedTokens(){
        return ntk;
    }

    private Token symbol(TOKEN_TYPE t) {
        ntk++;
        return new Token(t, yytext(), yyline+1, yycolumn+1);
    }

    private Token symbol(TOKEN_TYPE t, Object value){
        ntk++;
        return new Token(t, value, yyline+1, yycolumn+1);
    }

%}

%init{
    ntk = 0;
%init}

%state COMMENT

digit = [0-9]
lower = [a-z]
upper = [A-Z]
id = {lower}({lower}|{upper}|{digit}|_)*
tyid = {upper}({lower}|{upper}|{digit}|_)*

newline = \r|\n|\r\n
whitespace = {newline} | [ \t\f]

%%

<YYINITIAL>{
    "data"      { return symbol(TOKEN_TYPE.DATA); }
    "abstract"  { return symbol(TOKEN_TYPE.ABSTRACT); }
    "if"        { return symbol(TOKEN_TYPE.IF); }
    "else"      { return symbol(TOKEN_TYPE.ELSE); }
    "iterate"   { return symbol(TOKEN_TYPE.ITERATE); }
    "read"      { return symbol(TOKEN_TYPE.READ); }
    "print"     { return symbol(TOKEN_TYPE.PRINT); }
    "return"    { return symbol(TOKEN_TYPE.RETURN); }
    "new"       { return symbol(TOKEN_TYPE.NEW); }
    "true"      { return symbol(TOKEN_TYPE.TRUE); }
    "false"     { return symbol(TOKEN_TYPE.FALSE); }
    "null"      { return symbol(TOKEN_TYPE.NULL); }
    "Int"       { return symbol(TOKEN_TYPE.Int); }
    "Float"     { return symbol(TOKEN_TYPE.Float); }
    "Bool"      { return symbol(TOKEN_TYPE.Bool); }
    "Char"      { return symbol(TOKEN_TYPE.Char); }

    "=="        { return symbol(TOKEN_TYPE.EQ); }
    "!="        { return symbol(TOKEN_TYPE.NEQ); }
    "="         { return symbol(TOKEN_TYPE.ASSIGN); }
    "::"        { return symbol(TOKEN_TYPE.DCOLON); }
    ":"         { return symbol(TOKEN_TYPE.COLON); }
    ";"         { return symbol(TOKEN_TYPE.SEMI); }
    "."         { return symbol(TOKEN_TYPE.DOT); }
    ","         { return symbol(TOKEN_TYPE.COMMA); }
    "("         { return symbol(TOKEN_TYPE.LPAREN); }
    ")"         { return symbol(TOKEN_TYPE.RPAREN); }
    "{"         { return symbol(TOKEN_TYPE.LBRACE); }
    "}"         { return symbol(TOKEN_TYPE.RBRACE); }
    "["         { return symbol(TOKEN_TYPE.LBRACKET); }
    "]"         { return symbol(TOKEN_TYPE.RBRACKET); }
    "+"         { return symbol(TOKEN_TYPE.PLUS); }
    "-"         { return symbol(TOKEN_TYPE.MINUS); }
    "*"         { return symbol(TOKEN_TYPE.TIMES); }
    "/"         { return symbol(TOKEN_TYPE.DIV); }
    "%"         { return symbol(TOKEN_TYPE.MOD); }
    "<"         { return symbol(TOKEN_TYPE.LT); }
    ">"         { return symbol(TOKEN_TYPE.GT); }
    "&&"        { return symbol(TOKEN_TYPE.AND); }
    "!"         { return symbol(TOKEN_TYPE.NOT); }

    {digit}+            { return symbol(TOKEN_TYPE.INT, Integer.parseInt(yytext())); }
    {digit}*"."{digit}+ { return symbol(TOKEN_TYPE.FLOAT, Double.parseDouble(yytext())); }
    \'([^\'\\]|\\[ntrb\'\\]|\\[0-9]{3})\'   { return symbol(TOKEN_TYPE.CHAR); }


    {id}                { return symbol(TOKEN_TYPE.ID); }
    {tyid}              { return symbol(TOKEN_TYPE.TYID); }

    "--".*              { /* ignora comentário de linha */ }
    "{-"                { yybegin(COMMENT); }

    {whitespace}        { }

    .                   { throw new RuntimeException("Illegal character: " + yytext()); }
}

<COMMENT>{
    "-}"                { yybegin(YYINITIAL); }
    [^]                 {                     }           
}

