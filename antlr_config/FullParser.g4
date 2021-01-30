parser grammar FullParser;

options {
  tokenVocab=FullLexer;
}

program     : BEGIN func* stat END ;
func        : type IDENT OPEN_PARENTHESES param_list? CLOSE_PARENTHESES IS stat END;
param_list  : param (COMMA param )* ;
param       : type IDENT;

stat : SKIP
     | type IDENT ASSIGN assign_rhs   
     | assign_lhs ASSIGN assign_rhs  
     | READ assign_lhs  
     | FREE expr  
     | RETURN expr  
     | EXIT expr  
     | PRINT expr  
     | PRINTLN expr  
     | IF expr THEN stat ELSE stat FI | WHILE expr DO stat DONE
     | BEGIN stat END
     | stat SEMICOLON stat 
     ;

assign_lhs : IDENT 
           | array_elem  
           | pair_elem 
           ;

assign_rhs : expr 
           | array_liter 
           | NEWPAIR OPEN_PARENTHESES expr  COMMA expr  CLOSE_PARENTHESES | pair_elem 
           | CALL IDENT  OPEN_PARENTHESES arg_list? CLOSE_PARENTHESES
           ;

arg_list  : expr (COMMA expr)*;
pair_elem : FST expr
          | SND expr
          ;

type : BASE_TYPE  
     | type OPEN_SQUARE_BRACKET CLOSE_SQUARE_BRACKET
     | pair_type 
     ;

array_type     : type OPEN_SQUARE_BRACKET CLOSE_SQUARE_BRACKET ;
pair_type      : PAIR OPEN_PARENTHESES pair_elem_type  COMMA pair_elem_type  CLOSE_PARENTHESES ;
pair_elem_type : BASE_TYPE  
               | array_type  
               | PAIR 
               ;

expr : INT_LITER 
     | BOOL_LITER 
     | CHAR_LITER 
     | STR_LITER 
     | PAIR_LITER 
     | IDENT 
     | array_elem 
     | UNOP expr 
     | expr BINOP expr  
     | OPEN_PARENTHESES expr CLOSE_PARENTHESES
     ;

array_elem  : IDENT (OPEN_SQUARE_BRACKET expr CLOSE_SQUARE_BRACKET)+ ;
array_liter : OPEN_SQUARE_BRACKET (expr (COMMA expr)*)? CLOSE_SQUARE_BRACKET ;
