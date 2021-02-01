lexer grammar FullLexer;

/* Main body of Array, Pair and Comment unimplemented
 * might put them in parser later (not sure) */

// skip space, tab, and newline
WS : [ \t\r\n]+ -> skip ;

// comments
SHARP   : '#' ;
EOL     : '\n' ;
COMMENT : SHARP ~('\n')* EOL -> skip;

// type
BASE_TYPE: 'int'
         | 'bool'
         | 'char' 
         | 'string'
         ;

// the literals of different types
BOOL_LITER        : 'true' | 'false' ;
CHAR_LITER        : '\'' CHARACTER '\'' ;
STR_LITER         : '"' CHARACTER* '"' ;

// pairs
FST        : 'fst' ;
SND        : 'snd' ;
PAIR_LITER : 'null' ;
PAIR       : 'pair' ;
NEWPAIR    : 'newpair' ;

// keywords
BEGIN   : 'begin' ;
END     : 'end' ;
IS      : 'is' ;
SKP     : 'skip' ;
ASSIGN  : '=' ;
READ    : 'read' ;
FREE    : 'free' ;
RETURN  : 'retrun' ;
EXIT    : 'exit' ;
PRINT   : 'print' ;
PRINTLN : 'println' ;
IF      : 'if' ;
ELSE    : 'else' ;
THEN    : 'then' ;
FI      : 'fi' ;
WHILE   : 'while' ;
DO      : 'do' ;
DONE    : 'done' ;
CALL    : 'call' ;
SEMICOLON : ';';
COMMA     : ',';

// brackets
OPEN_PARENTHESES     : '(' ;
CLOSE_PARENTHESES    : ')' ;
OPEN_SQUARE_BRACKET  : '[' ;
CLOSE_SQUARE_BRACKET : ']' ;

// operator fragments
PLUS  : '+' ;
MINUS : '-' ;
NOT   : '!' ;
LEN   : 'len' ;
ORD   : 'ord' ;
CHR   : 'chr' ;
MUL   : '*' ;
DIV   : '/' ;
MOD   : '%' ;
GREATER       : '>' ;
GREATER_EQUAL : '>=' ;
LESS          : '<' ;
LESS_EQUAL    : '<=' ;
EQUAL         : '==' ;
UNEQUAL       : '!=' ;
AND           : '&&' ;
OR            : '||' ;

INT_LITER         : DIGIT+ ;
fragment DIGIT    : [0-9] ;
// fragment INT_SIGN : MINUS | PLUS ;

// identifier rule
IDENT : ('_' | [a-z] | [A-Z])('_' | [a-z] | [A-Z] | DIGIT)* ;

// definition of characters
fragment CHARACTER : ~['"\\]
          | '\\' ESCAPED_CHAR
          ;
fragment ESCAPED_CHAR : '0' | 'b' | 't' | 'n' | 'f' | 'r' | '"'| '\'' | '\\' ;
