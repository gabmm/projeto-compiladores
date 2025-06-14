public enum TOKEN_TYPE {
    INT,
    FLOAT,
    CHAR,
    TRUE,
    FALSE,
    NULL,

    ID,
    TYID,

    DATA,
    ABSTRACT,
    IF,
    ELSE,
    ITERATE,
    READ,
    PRINT,
    RETURN,
    NEW,

    LPAREN,      // (
    RPAREN,      // )
    LBRACE,      // {
    RBRACE,      // }
    LBRACKET,    // [
    RBRACKET,    // ]
    LT,          // <
    SEMI,        // ;
    COLON,       // :
    DCOLON,      // ::
    DOT,         // .
    COMMA,       // ,
    ASSIGN,      // =
    EQ,          // ==
    NEQ,         // !=
    PLUS,        // +
    MINUS,       // -
    TIMES,       // *
    DIV,         // /
    MOD,         // %
    AND,         // &&
    NOT,         // !

    EOF
}
