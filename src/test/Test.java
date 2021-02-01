package test;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import antlr.*;

public class Test{
  public static void main(String[] args) throws Exception {
    CharStream input = CharStreams.fromStream(System.in);
    WACCLexer lexer = new WACCLexer(input);
    CommonTokenStream tokens  = new CommonTokenStream(lexer);
    WACCParser parser = new WACCParser(tokens);
    ParseTree tree = parser.program();

    // System.out.println(tree.toStringTree(parser));
    // WACCParserVisitor visitor = new VisitorExample();
    // visitor.visit(tree);
  }
}