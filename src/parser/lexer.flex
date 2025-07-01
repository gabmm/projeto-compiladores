package parser;
import beaver.Symbol;
import beaver.Scanner;
import java.math.BigDecimal;

%%

%public //precisa ser publica
%class Lexer //nome da classe que vai ser gerada
%extends Scanner //conexão com o beaver
%function nextToken //nextToken e Symbol tambem interfaciam com o beaver
%type Symbol
%yylexthrow Scanner.Exception
%eofval{
	return newToken(Terminals.EOF, "end-of-file"); //classe terminals agora é gerada pelo beaver
%eofval}
%unicode
%line
%column

%{
    private Symbol newToken(short id){
        return new Symbol(id, yyline + 1, yycolumn +1, yylength());
    }

    private Symbol newToken(short id, Object value){
        return new Symbol(id, yyline + 1, yycolumn +1, yylength(), value);
    }

%}

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
    "data"      { return newToken(Terminals.DATA); }
    "abstract"  { return newToken(Terminals.ABSTRACT); }
    "if"        { return newToken(Terminals.IF); }
    "else"      { return newToken(Terminals.ELSE); }
    "iterate"   { return newToken(Terminals.ITERATE); }
    "read"      { return newToken(Terminals.READ); }
    "print"     { return newToken(Terminals.PRINT); }
    "return"    { return newToken(Terminals.RETURN); }
    "new"       { return newToken(Terminals.NEW); }
    "true"      { return newToken(Terminals.TRUE); }
    "false"     { return newToken(Terminals.FALSE); }
    "null"      { return newToken(Terminals.NULL); }
    "Int"       { return newToken(Terminals.TYINT); }
    "Float"     { return newToken(Terminals.TYFLOAT); }
    "Bool"      { return newToken(Terminals.TYBOOL); }
    "Char"      { return newToken(Terminals.TYCHAR); }

    "=="        { return newToken(Terminals.EQ); }
    "!="        { return newToken(Terminals.NEQ); }
    "="         { return newToken(Terminals.ASSIGN); }
    "::"        { return newToken(Terminals.DCOLON); }
    ":"         { return newToken(Terminals.COLON); }
    ";"         { return newToken(Terminals.SEMI); }
    "."         { return newToken(Terminals.DOT); }
    ","         { return newToken(Terminals.COMMA); }
    "("         { return newToken(Terminals.LPAREN); }
    ")"         { return newToken(Terminals.RPAREN); }
    "{"         { return newToken(Terminals.LBRACE); }
    "}"         { return newToken(Terminals.RBRACE); }
    "["         { return newToken(Terminals.LBRACKET); }
    "]"         { return newToken(Terminals.RBRACKET); }
    "+"         { return newToken(Terminals.PLUS); }
    "-"         { return newToken(Terminals.MINUS); }
    "*"         { return newToken(Terminals.TIMES); }
    "/"         { return newToken(Terminals.DIV); }
    "%"         { return newToken(Terminals.MOD); }
    "<"         { return newToken(Terminals.LT); }
    ">"         { return newToken(Terminals.GT); }
    "&&"        { return newToken(Terminals.AND); }
    "!"         { return newToken(Terminals.NOT); }

    {digit}+            { return newToken(Terminals.INT, Integer.parseInt(yytext())); }
    {digit}*"."{digit}+ { return newToken(Terminals.FLOAT, Double.parseDouble(yytext())); }
    \'([^\'\\]|\\[ntrb\'\\]|\\[0-9]{3})\'   {
        String content = yytext().substring(1, yylength() - 1); // Pega o que está dentro das '', ex: 'a' ou '\\n'
        char charValue;
        if (content.charAt(0) == '\\') { // Se começar com '\', pode ser que o proximo seja
            switch (content.charAt(1)) { // um caractere de escape, tipo \n,\t...que não são um char
                case 'n': charValue = '\n'; break;
                case 't': charValue = '\t'; break;
                case 'r': charValue = '\r'; break;
                case 'b': charValue = '\b'; break;
                default: charValue = content.charAt(1);
            }
        } else {
            charValue = content.charAt(0); // Se for um caractere normal
        }
        return newToken(Terminals.CHAR, new Character(charValue));
    }


    {id}                { return newToken(Terminals.ID, yytext());}
    {tyid}              { return newToken(Terminals.TYID,yytext());}

    "--".*              { /* ignora comentário de linha */ }
    "{-"                { yybegin(COMMENT); }

    {whitespace}        { }

    .                   { throw new RuntimeException("Illegal character: " + yytext()); }
}

<COMMENT>{
    "-}"                { yybegin(YYINITIAL); }
    [^]                 {                     }           
}

