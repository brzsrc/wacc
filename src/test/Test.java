package test;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import antlr.*;

public class Test{
  public static void main(String[] args) throws Exception {
    CharStream input = CharStreams.fromStream(System.in);
    FullLexer lexer = new FullLexer(input);
    CommonTokenStream tokens  = new CommonTokenStream(lexer);
    FullParser parser = new FullParser(tokens);
    ParseTree tree = parser.expr();

    System.out.println(tree.toStringTree(parser));
  }
}