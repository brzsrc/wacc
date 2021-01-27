lexer grammar FullLexer;

/* following rules are introduced 
   because dont know how to use string directly in parser rule specifications
 */
FST: 'fst' ;
SND: 'snd' ;
OPEN_PARENTHESES: '(' ;
CLOSE_PARENTHESES: ')' ;
OPEN_SQUARE_BRACKET: '[' ;
CLOSE_SQUARE_BRACKET: ']' ;

//avoid making INT_SIGN be part of BINARY_OPER
fragment PLUS: '+' ;
fragment MINUS: '-' ;

//type
BASE_TYPE: 'int'
         | 'bool'
         | 'char' 
         | 'string'
         ;

//operators
UNARY_OPER: '!' | MINUS | 'len' | 'ord' | 'chr' ;
BINARY_OPER: '*' | '/' | '%' | PLUS | MINUS | '>' | '>=' | '<' | '<=' | '==' | '!=' | '&&' | '||' ;

INT_SIGN: MINUS | PLUS ;

//namings
IDENT: ('_' | [a-z] | [A-Z])('_' | [a-z] | [A-Z] | DIGIT)* ;

//numbers
fragment DIGIT: [0-9] ;
BOOL_LITER: 'true' | 'false' ;

INTEGER: DIGIT+ ;

//characters
fragment ESCAPED_CHAR: '0'|'b'|'t'|'n'|'f'|'r'|'"'| '\'' | '\\' ;


PAIR_LITER: 'null' ;




