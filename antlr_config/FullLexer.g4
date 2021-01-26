lexer grammar FullLexer;

/* following rules are introduced 
   because dont know how to use string directly in parser rule specifications
 */
FST: 'fst' ;
SND: 'snd' ;
OPEN_PARENTHESES: '(' ;
CLOSE_PARENTHESES: ')' ;

//type
BASE_TYPE: 'int'
         | 'bool'
         | 'char' 
         | 'string'
         ;

//operators
UNARY_OPER: '!' | '-' | 'len' | 'ord' | 'chr' ;
BINARY_OPER: '*' | '/' | '%' | '>' | '>=' | '<' | '<=' | '==' | '!=' | '&&' | '||' | INT_SIGN ;

INT_SIGN: '-' | '+' ;

//namings
IDENT: ('_' | [a-z] | [A-Z])('_' | [a-z] | [A-Z] | DIGIT)* ;

//numbers
DIGIT: [0-9] ;
BOOL_LITER: 'true' | 'false' ;

INTEGER: DIGIT+ ;

//characters
ESCAPED_CHAR: '0'|'b'|'t'|'n'|'f'|'r'|'"'| '\'' | '\\' ;


PAIR_LITER: 'null' ;




