parser grammar WACCParser;

options {
  tokenVocab=WACCLexer;
}

program     : BEGIN func* stat END EOF;
func        : type IDENT OPEN_PARENTHESES param_list? CLOSE_PARENTHESES IS stat END;
param_list  : param (COMMA param )* ;
param       : type IDENT;

stat : SKP                            #SkipStat
     | type IDENT ASSIGN assign_rhs   #DeclareStat
     | assign_lhs ASSIGN assign_rhs   #AssignStat
     | READ assign_lhs                #ReadStat
     | FREE expr                      #FreeStat
     | RETURN expr                    #ReturnStat
     | EXIT expr                      #ExitStat
     | PRINT expr                     #PrintStat
     | PRINTLN expr                   #PrintlnStat
     | BREAK                          #BreakStat
     | CONTINUE                       #ContinueStat
     | IF expr THEN stat ELSE stat FI #IfStat
     | FOR OPEN_PARENTHESES
       (expr (COMMA expr)*)? SEMICOLON
       (expr (COMMA expr)*)? SEMICOLON
       (expr (COMMA expr)*)?
       CLOSE_PARENTHESES DO stat DONE #ForStat
     | SWITCH expr DO (CASE expr)* 
       DEFAULT expr DONE              #SwitchStat
     | DO stat WHILE expr             #DoWhileStat
     | WHILE expr DO stat DONE        #WhileStat
     | BEGIN stat END                 #ScopeStat
     | stat SEMICOLON stat            #SeqStat
     ;

assign_lhs : IDENT      #Ident
           | array_elem #LHSArrayElem // This visitor will be replaced by visitArray_elem()
           | pair_elem  #LHSPairElem  // This visitor will be replaced by visitors in pair_elem
           ;

assign_rhs : expr                                                         #ExprNode     // This visitor will be replaced by the general visit()
           | array_liter                                                  #ArrayLiteral // This visitor will be replaced by visitArray_liter()
           | NEWPAIR OPEN_PARENTHESES expr  COMMA expr  CLOSE_PARENTHESES #NewPair
           | pair_elem                                                    #RHSPairElem  // This visitor will be replaced by visitPair_elem()
           | CALL IDENT  OPEN_PARENTHESES arg_list? CLOSE_PARENTHESES     #FunctionCall
           ;

// argument list for functions 
arg_list  : expr (COMMA expr)*;
pair_elem : FST expr #FstExpr
          | SND expr #SndExpr
          ;

type : base_type  #BaseType
     | array_type #ArrayType
     | pair_type  #PairType
     ;

base_type : INT    #IntType
          | BOOL   #BoolType
          | CHAR   #CharType
          | STRING #StringType
          ;

array_type     : array_type OPEN_SQUARE_BRACKET CLOSE_SQUARE_BRACKET
               | base_type OPEN_SQUARE_BRACKET CLOSE_SQUARE_BRACKET
               | pair_type OPEN_SQUARE_BRACKET CLOSE_SQUARE_BRACKET
               ;
pair_type      : PAIR OPEN_PARENTHESES pair_elem_type  COMMA pair_elem_type  CLOSE_PARENTHESES ;
pair_elem_type : base_type  #PairElemBaseType   // This visitor will be replaced by base_type visitors
               | array_type #PairElemArrayType  // This visitor will be replaced by array_type visitors
               | PAIR       #PairElemPairType
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
     | expr '&&' expr                                 #AndExpr
     | expr '||' expr                                 #OrExpr
     | OPEN_PARENTHESES expr CLOSE_PARENTHESES        #ParenExpr
     ;

array_elem  : IDENT (OPEN_SQUARE_BRACKET expr CLOSE_SQUARE_BRACKET)+ ;
array_liter : OPEN_SQUARE_BRACKET (expr (COMMA expr)*)? CLOSE_SQUARE_BRACKET ;