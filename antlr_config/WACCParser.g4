parser grammar WACCParser;

options {
  tokenVocab=WACCLexer;
}

program     : import_file* BEGIN declaration stat END EOF;
library     : import_file* declaration;  // content of library
declaration : (struct | func)*;   // group of struct function declarations
struct      : STRUCT IDENT IS OPEN_CURLY_BRACKET param_list? CLOSE_CURLY_BRACKET;
func        : type IDENT OPEN_PARENTHESES param_list? CLOSE_PARENTHESES IS stat END;
param_list  : param (COMMA param )* ;
param       : type IDENT;

import_file : IMPORT FILE_NAME;


stat : skp                            #StatSkipStat    // This visitor will be replaced by #SkipStat
     | declare                        #StatDeclareStat // This visitor will be replaced by #DeclareStat
     | assign                         #StatAssignStat  // This visitor will be replaced by #AssignStat
     | read                           #StatReadStat    // This visitor will be replaced by #ReadStat
     | free                           #StatFreeStat    // This visitor will be replaced by #FreeStat
     | RETURN expr                    #ReturnStat      
     | exit                           #StatExitStat    // This visitor will be replaced by #ExitStat
     | print                          #StatPrintStat   // This visitor will be replaced by #PrintStat
     | println                        #StatPrintlnStat // This visitor will be replaced by #PrintlnStat
     | BREAK                          #BreakStat
     | CONTINUE                       #ContinueStat
     | IF expr THEN stat (ELSE stat)? FI #IfStat
     | FOR OPEN_PARENTHESES
       for_stat SEMICOLON
       expr SEMICOLON
       for_stat
       CLOSE_PARENTHESES DO stat DONE #ForStat
     | SWITCH expr DO (CASE expr stat)*
       DEFAULT stat DONE              #SwitchStat
     | DO stat WHILE expr             #DoWhileStat
     | WHILE expr DO stat DONE        #WhileStat
     | BEGIN stat END                 #ScopeStat
     | stat SEMICOLON stat            #SeqStat
     ;

assign_lhs : IDENT        #Ident
           | array_elem   #LHSArrayElem // This visitor will be replaced by visitArray_elem()
           | pair_elem    #LHSPairElem  // This visitor will be replaced by visitors in pair_elem
           | struct_elem  #LHSStructElem // This visitor will be replaced by visitors in struct_elem
           ;

for_stat : skp                  #ForStatSkp     // This visitor will be replaced by #SkipStat
     | declare                  #ForStatDeclare // This visitor will be replaced by #DeclareStat
     | assign                   #ForStatAssign  // This visitor will be replaced by #AssignStat
     | read                     #ForStatRead    // This visitor will be replaced by #ReadStat
     | free                     #ForStatFree    // This visitor will be replaced by #FreeStat
     | exit                     #ForStatExit    // This visitor will be replaced by #ExitStat
     | print                    #ForStatPrint   // This visitor will be replaced by #PrintStat
     | println                  #ForStatPrintln // This visitor will be replaced by #PrintlnStat
     | for_stat COMMA for_stat  #ForStatSeq
     ;

skp     : SKP                          #SkipStat;
declare : type IDENT ASSIGN assign_rhs #DeclareStat;
assign  : assign_lhs ASSIGN assign_rhs #AssignStat;
read    : READ assign_lhs              #ReadStat;
free    : FREE expr                    #FreeStat;
exit    : EXIT expr                    #ExitStat;
print   : PRINT expr                   #PrintStat;
println : PRINTLN expr                 #PrintlnStat;

assign_rhs : expr                                                          #ExprNode     // This visitor will be replaced by the general visit()
           | array_liter                                                   #ArrayLiteral // This visitor will be replaced by visitArray_liter()
           | NEWPAIR OPEN_PARENTHESES expr COMMA expr  CLOSE_PARENTHESES   #NewPair
           | pair_elem                                                     #RHSPairElem  // This visitor will be replaced by visitPair_elem()
           | CALL IDENT OPEN_PARENTHESES arg_list? CLOSE_PARENTHESES       #FunctionCall
           ;

// argument list for functions 
arg_list  : expr (COMMA expr)*;

struct_elem : IDENT (DOT IDENT)+;
new_struct  : NEW IDENT OPEN_PARENTHESES arg_list? CLOSE_PARENTHESES ;

pair_elem : FST expr #FstExpr
          | SND expr #SndExpr
          ;

type : base_type    #BaseType
     | array_type   #ArrayType
     | pair_type    #PairType
     | struct_type  #StructType
     ;

base_type : INT     #IntType
          | BOOL    #BoolType
          | CHAR    #CharType
          | STRING  #StringType
          ;

array_type     : array_type OPEN_SQUARE_BRACKET CLOSE_SQUARE_BRACKET
               | base_type OPEN_SQUARE_BRACKET CLOSE_SQUARE_BRACKET
               | pair_type OPEN_SQUARE_BRACKET CLOSE_SQUARE_BRACKET
               | struct_type OPEN_SQUARE_BRACKET CLOSE_SQUARE_BRACKET
               ;
pair_type      : PAIR OPEN_PARENTHESES pair_elem_type  COMMA pair_elem_type  CLOSE_PARENTHESES ;
pair_elem_type : base_type   #PairElemBaseType   // This visitor will be replaced by base_type visitors
               | array_type  #PairElemArrayType  // This visitor will be replaced by array_type visitors
               | struct_type #PairElemStructType // This visitor will be replaced by struct_type visitors
               | PAIR        #PairElemPairType
               ;

struct_type : IDENT ;


expr : INT_LITER      #IntExpr
     | PLUS INT_LITER #IntExpr
     | BINARY_LITER   #BinaryExpr
     | OCTAL_LITER    #OctalExpr
     | HEX_LITER      #HexExpr
     | BOOL_LITER     #BoolExpr
     | CHAR_LITER     #CharExpr
     | STR_LITER      #StrExpr
     | PAIR_LITER     #PairExpr
     | IDENT          #IdExpr
     | array_elem     #ArrayExpr
     | struct_elem    #StructElemExpr
     | new_struct     #StructExpr
     | EMPTY          #EmptyStructExpr
     | uop=( '-' | '!' | 'len' | 'ord' | 'chr' | '~') expr #UnopExpr
     | expr bop=( '*' | '/' | '%' ) expr              #ArithmeticExpr
     | expr bop=( '+' | '-' ) expr                    #ArithmeticExpr
     | expr bop=( '<<' | '>>' ) expr                  #BitwiseExpr
     | expr bop=( '>' | '>=' | '<' | '<=' ) expr      #CmpExpr
     | expr bop=( '==' | '!=' ) expr                  #EqExpr
     | expr bop=( '&' | '|' | '^' ) expr              #BitwiseExpr
     | expr '&&' expr                                 #AndExpr
     | expr '||' expr                                 #OrExpr
     | OPEN_PARENTHESES expr CLOSE_PARENTHESES        #ParenExpr
     ;

array_elem  : IDENT (OPEN_SQUARE_BRACKET expr CLOSE_SQUARE_BRACKET)+ ;
array_liter : OPEN_SQUARE_BRACKET (expr (COMMA expr)*)? CLOSE_SQUARE_BRACKET ;

