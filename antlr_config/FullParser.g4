parser grammar FullParser;

options {
  tokenVocab=FullLexer;
}

pair_elem: FST expr
         | SND expr
         ;

expr: IDENT 
    | UNARY_OPER expr
    | expr BINARY_OPER expr
    | OPEN_PARENTHESES expr CLOSE_PARENTHESES
    ;