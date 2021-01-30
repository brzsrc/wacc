lexer grammar FullLexer;

/* Main body of Array, Pair and Comment unimplemented
 * might put them in parser later (not sure) */

//type
BASE_TYPE: 'int'
         | 'bool'
         | 'char' 
         | 'string'
         ;

//operators
UNARY_OPER: NOT | MINUS | LEN | ORD | CHR ;
BINARY_OPER: MUL | DIV | MOD | PLUS | MINUS | GREATER | GREATER_EQUAL | LESS | LESS_EQUAL | EQUAL | UNEQUAL | AND | OR ;

//namings
IDENT: ('_' | [a-z] | [A-Z])('_' | [a-z] | [A-Z] | DIGIT)* ;

//integers
INT_LITER: INT_SIGN? DIGIT+ ;
fragment DIGIT: [0-9] ;
fragment INT_SIGN: MINUS | PLUS ;

//booleans
BOOL_LITER: 'true' | 'false' ;

//characters
CHAR_LITER: '\'' CHARACTER '\'' ;

//strings
STR_LITER: '"' CHARACTER* '"' ;

/* not fragment cauz might be used for comment later */
CHARACTER: ~['"\\]
         | '\\' ESCAPED_CHAR
         ;
fragment ESCAPED_CHAR: '0'|'b'|'t'|'n'|'f'|'r'|'"'| '\'' | '\\' ;

//pairs
FST: 'fst' ;
SND: 'snd' ;
PAIR_LITER: 'null' ;
PAIR: 'pair' ;
NEWPAIR: 'newpair' ;

//comments
SHARP: '#' ;
EOL: '\n' ;
COMMENT: SHARP (~['"\\])* EOL;

//keywords
BEGIN: 'begin' ;
END: 'end' ;
IS: 'is' ;
/* Skip is a preserved word */
SKP: 'skip' ;
ASSIGN: '=' ;
READ: 'read' ;
FREE: 'free' ;
RETURN: 'retrun' ;
EXIT: 'exit' ;
PRINT: 'print' ;
PRINTLN: 'println' ;
IF: 'if' ;
ELSE: 'else' ;
THEN: 'then' ;
FI: 'fi' ;
WHILE: 'while' ;
DO: 'do' ;
DONE: 'done' ;
CALL: 'call' ;
SEMICOLON: ';';
COMMA: ',';

//brackets
OPEN_PARENTHESES: '(' ;
CLOSE_PARENTHESES: ')' ;
OPEN_SQUARE_BRACKET: '[' ;
CLOSE_SQUARE_BRACKET: ']' ;

//operator fragments
fragment PLUS: '+' ;
fragment MINUS: '-' ;
fragment NOT: '!' ;
fragment LEN: 'len' ;
fragment ORD: 'ord' ;
fragment CHR: 'chr' ;
fragment MUL: '*' ;
fragment DIV: '/' ;
fragment MOD: '%' ;
fragment GREATER: '>' ;
fragment GREATER_EQUAL: '>=' ;
fragment LESS: '<' ;
fragment LESS_EQUAL: '<=' ;
fragment EQUAL: '==' ;
fragment UNEQUAL: '!=' ;
fragment AND: '&&' ;
fragment OR: '||' ;

