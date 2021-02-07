import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import antlr.WACCLexer;
import antlr.WACCParser;
import antlr.WACCParserVisitor;

import java.io.IOException;

public class Test {

  public static void main(String[] args) throws IOException {
    CharStream input = CharStreams.fromStream(System.in);
    WACCLexer lexer = new WACCLexer(input);
    // Obtain the internal tokens from the lexer
    CommonTokenStream tokens  = new CommonTokenStream(lexer);
    // Parse the tokens into a syntax tree
    WACCParser parser = new WACCParser(tokens);
    // Start parsing using the `program` rule defined in antlr_config/WACCParser.g4
    ParseTree tree = parser.program();

    WACCParserVisitor visitor = new SemanticChecker();
    visitor.visit(tree);

  }
}
