parser grammar WACCParser;

options {
  tokenVocab=WACCLexer;
}

program     : BEGIN func* stat END EOF;
func        : type IDENT OPEN_PARENTHESES param_list? CLOSE_PARENTHESES IS stat END;
param_list  : param (COMMA param )* ;
param       : type IDENT;

stat : SKP                               #SkipStat
     | type IDENT ASSIGN assign_rhs      #DeclareStat
     | assign_lhs ASSIGN assign_rhs      #AssignStat
     | READ assign_lhs                   #ReadStat
     | FREE expr                         #FreeStat
     | RETURN expr                       #ReturnStat
     | EXIT expr                         #ExitStat
     | PRINT expr                        #PrintStat
     | PRINTLN expr                      #PrintlnStat
     | IF expr THEN stat ELSE stat FI    #IfStat
     | WHILE expr DO stat DONE           #WhileStat
     | BEGIN stat END                    #ScopeStat
     | stat SEMICOLON stat               #SeqStat
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

// argument list for functions 
arg_list  : expr (COMMA expr)*;
pair_elem : FST expr
          | SND expr
          ;

type : base_type
     | array_type
     | pair_type 
     ;

// put base type here, maybe easier to implement expr? (not sure)
base_type : INT
          | BOOL
          | CHAR
          | STRING
          ;

array_type     : array_type OPEN_SQUARE_BRACKET CLOSE_SQUARE_BRACKET 
               | base_type OPEN_SQUARE_BRACKET CLOSE_SQUARE_BRACKET
               | pair_type OPEN_SQUARE_BRACKET CLOSE_SQUARE_BRACKET
               ;
pair_type      : PAIR OPEN_PARENTHESES pair_elem_type  COMMA pair_elem_type  CLOSE_PARENTHESES ;
pair_elem_type : base_type
               | array_type  
               | PAIR 
               ;

expr : INT_LITER      #IntExpr
     | PLUS INT_LITER #IntExpr
     | BOOL_LITER     #BoolExpr
     | CHAR_LITER     #CharExpr
     | STR_LITER      #StrExpr
     | PAIR_LITER     #PairExpr
     | IDENT          #IdExpr
     | array_elem     #ArrayExpr
     | uop=( '-' | '!' | 'len' | 'ord' | 'chr' ) expr #UnopExpr
     | expr bop=( '*' | '/' | '%' ) expr              #ArithmeticExpr
     | expr bop=( '+' | '-' ) expr                    #ArithmeticExpr
     | expr bop=( '>' | '>=' | '<' | '<=' ) expr      #CmpExpr
     | expr bop=( '==' | '!=' ) expr                  #EqExpr
     | expr bop=( '&&' | '||' ) expr                  #AndOrExpr
     | OPEN_PARENTHESES expr CLOSE_PARENTHESES        #ParenExpr
     ;

array_elem  : IDENT (OPEN_SQUARE_BRACKET expr CLOSE_SQUARE_BRACKET)+ ;
array_liter : OPEN_SQUARE_BRACKET (expr (COMMA expr)*)? CLOSE_SQUARE_BRACKET ;