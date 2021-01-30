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

// unary and binary operators
UNOP  : NOT
      // | MINUS 
      | LEN 
      | ORD 
      | CHR ;
BINOP : MUL 
      | DIV 
      | MOD 
      // | PLUS 
      // | MINUS 
      | GREATER 
      | GREATER_EQUAL 
      | LESS 
      | LESS_EQUAL 
      | EQUAL 
      | UNEQUAL 
      | AND 
      | OR ;


// the literals of different types
INT_LITER         : INT_SIGN? DIGIT+ ;
fragment DIGIT    : [0-9] ;
fragment INT_SIGN : MINUS | PLUS ;
BOOL_LITER        : 'true' | 'false' ;
CHAR_LITER        : '\'' CHARACTER '\'' ;
STR_LITER         : '"' CHARACTER* '"' ;

// definition of characters
fragment CHARACTER : ~['"\\]
          | '\\' ESCAPED_CHAR
          ;
fragment ESCAPED_CHAR : '0' | 'b' | 't' | 'n' | 'f' | 'r' | '"'| '\'' | '\\' ;

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
SKP    : 'skip' ;
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
fragment NOT   : '!' ;
fragment LEN   : 'len' ;
fragment ORD   : 'ord' ;
fragment CHR   : 'chr' ;
fragment MUL   : '*' ;
fragment DIV   : '/' ;
fragment MOD   : '%' ;
fragment GREATER       : '>' ;
fragment GREATER_EQUAL : '>=' ;
fragment LESS          : '<' ;
fragment LESS_EQUAL    : '<=' ;
fragment EQUAL         : '==' ;
fragment UNEQUAL       : '!=' ;
fragment AND           : '&&' ;
fragment OR            : '||' ;

// identifier rule
IDENT : ('_' | [a-z] | [A-Z])('_' | [a-z] | [A-Z] | DIGIT)* ;
