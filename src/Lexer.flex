
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

    endofline = \r|\n|\r\n
    whitespace = {endofline} | [ \t\f]
    number = [:digit:] [:digit:]*
    identifier = [:lowercase:]
    linecomment = "//" (.)* {endofline}

%state COMMENT

%%

<YYINITIAL>{
    {identifier} { return symbol(TOKEN_TYPE.ID);   }
    {number}        { return symbol(TOKEN_TYPE.NUM, Integer.parseInt(yytext()));  }
    "="             { return symbol(TOKEN_TYPE.EQ);   }
    ";"             { return symbol(TOKEN_TYPE.SEMI); }
    "*"             { return symbol(TOKEN_TYPE.TIMES); }
    "+"             { return symbol(TOKEN_TYPE.PLUS); }
    "/*"            { yybegin(COMMENT);               }
    {whitespace}    { /* Não faz nada  */             }
    {linecomment}   {                          }
}

<COMMENT>{
   "*/"     { yybegin(YYINITIAL); } 
   [^"*/"]* {                     }
}

[^]                 { throw new RuntimeException("Illegal character <"+yytext()+">"); }