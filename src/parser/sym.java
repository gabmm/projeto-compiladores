/*
 * UFJF - Universidade Federal de Juiz de Fora
 * Departamento de Ciência da Computação
 * Trabalho de compiladores - Linguagem Lang
 * Desenvolvido pelos alunos:
 *  - Gabriel Martins da Costa Medeiros - 201935032
 *  - Matheus Peron Resende Corrêa - 201965089C
 */

package parser;

public interface sym {
    // Terminais
    short ABSTRACT = 1;
    short DATA = 2;
    short IF = 3;
    short ELSE = 4;
    short ITERATE = 5;
    short READ = 6;
    short PRINT = 7;
    short RETURN = 8;
    short NEW = 9;
    short TRUE = 10;
    short FALSE = 11;
    short NULL = 12;
    short TYINT = 13;
    short TYFLOAT = 14;
    short TYBOOL = 15;
    short TYCHAR = 16;
    short TYID = 17;
    short EQ = 18;
    short NEQ = 19;
    short ASSIGN = 20;
    short DCOLON = 21;
    short COLON = 22;
    short SEMI = 23;
    short COMMA = 24;
    short DOT = 25;
    short LT = 26;
    short LPAREN = 27;
    short RPAREN = 28;
    short LBRACE = 29;
    short RBRACE = 30;
    short LBRACKET = 31;
    short RBRACKET = 32;
    short AND = 33;
    short OR = 34;
    short NOT = 35;
    short PLUS = 36;
    short MINUS = 37;
    short TIMES = 38;
    short DIV = 39;
    short MOD = 40;
    short INT = 41;
    short FLOAT = 42;
    short CHAR = 43;
    short ID = 44;

    // Não-terminais (para Symbol)
    short PROG = 100;
    short DEF = 101;
    short DEF_LIST = 102;
    short DATA_BODY_ABSTRACT = 103;
    short DECL_OR_FUN = 104;
    short DECL_LIST = 105;
    short DECL = 106;
    short FUN = 107;
    short PARAMS_OPT = 108;
    short PARAMS = 109;
    short PARAM_LIST = 110;
    short TYPE = 111;
    short TYPE_LIST = 112;
    short TYPES_OPT = 113;
    short BTYPE = 114;
    short BLOCK = 115;
    short CMD_LIST = 116;
    short CMD = 117;
    short EXP = 118;
    short EXP_LIST = 119;
    short EXPS = 120;
    short EXPS_OPT = 121;
    short LVALUE = 122;
    short LVALUE_LIST = 123;
    short LVALUE_OPT = 124;
    short ITCOND = 125;
    short EXP_LIST_BRACKET = 126;
    short ID_TYPE_LIST = 127;
    short OP = 128;
    short DECL_FUN_LIST = 129;
}
