package utils.frontend;

import java.util.Set;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;

import frontend.node.stat.JumpNode.JumpType;
import frontend.type.Type;

import static utils.Utils.*;

public class SemanticErrorHandler {

  /**
   * SemanticErrorHandler will check all possible semantic/syntax errors during the SemanticChecker
   * visit. Notice that some error handler functions will not exit with status directly in order to
   * support checking of multiple errors in the same program.
   */

  /* add a private constructor to prevent this class from instantization */
  private SemanticErrorHandler() {
    throw new IllegalStateException("SemanticErrorHandler cannot be instantiated!");
  }

  public static void typeMismatch(ParserRuleContext ctx, Type expected, Type actual) {
    String msg =
        "Incompatible type at '" + ctx.getText() + "'': Expected type " + expected +
        ", but the actual type is " + actual;
    errorHandler(ctx, msg);
  }

  public static void typeMismatch(ParserRuleContext ctx, Set<Type> expected, Type actual) {
    String msg = 
        "Incompatible type at '" + ctx.getText() + "'': Expected types are " + expected +
        ", but the actual type is " + actual;
    errorHandler(ctx, msg);
  }

  public static void typeMismatch(ParserRuleContext ctx, String ident, Type expected, Type actual) {
    String msg =
        "Incompatible type at '" + ctx.getText() + "'': Expected type " + expected +
        " for variable " + ident + ", but the actual type is " + actual;
    errorHandler(ctx, msg);
  }

  public static void invalidFuncArgCount(ParserRuleContext ctx, int expected, int actual) {
    String msg =
        "Invalid number of arguments: Expected " + expected + " argument(s), but actual count is "
            + actual + "argument(s)";
    errorHandler(ctx, msg);
    System.exit(SEMANTIC_ERROR_CODE);
  }

  public static void symbolNotFound(ParserRuleContext ctx, String ident) {
    String msg = "Symbol " + ident + " is not found in the current scope of the program";
    errorHandler(ctx, msg);
    System.exit(SEMANTIC_ERROR_CODE);
  }

  public static void symbolRedeclared(ParserRuleContext ctx, String ident) {
    String msg =
        "Symbol " + ident + " has already been declared in the current scope of the program";
    errorHandler(ctx, msg);
  }

  public static void arrayDepthError(ParserRuleContext ctx, Type type, int indexDepth) {
    String msg =
        "Array declared as " + type + ", but called with index depth " + indexDepth;
    errorHandler(ctx, msg);
    System.exit(SEMANTIC_ERROR_CODE);
  }

  public static void returnFromMainError(ParserRuleContext ctx) {
    String msg = "Call return in main function body is not allowed";
    errorHandler(ctx, msg);
  }

  public static void invalidPairError(ParserRuleContext ctx) {
    String msg = "Calling fst/snd on uninitialised pair expr is not allowed";
    errorHandler(ctx, msg);
    System.exit(SEMANTIC_ERROR_CODE);
  }

  public static void integerRangeError(ParserRuleContext ctx, String intText) {
    String msg = "Integer " + intText + " format not compatible with 32bit int";
    errorHandler(ctx, msg);
    System.exit(SYNTAX_ERROR_CODE);
  }

  public static void charOperatorRangeError(ParserRuleContext ctx, String intText) {
    String msg =
        "chr operator will only accept integer in the range of 0-127, but the actual integer is "
            + intText;
    errorHandler(ctx, msg);
    System.exit(SYNTAX_ERROR_CODE);
  }

  public static void invalidFunctionReturnExit(ParserRuleContext ctx, String funcName) {
    String msg = "Function " + funcName + " has not returned or exited properly.";
    errorHandler(ctx, msg);
    System.exit(SYNTAX_ERROR_CODE);
  }

  public static void functionJunkAfterReturn(ParserRuleContext ctx) {
    String msg = "Other statements exist after function return statement.";
    errorHandler(ctx, msg);
    System.exit(SYNTAX_ERROR_CODE);
  }

  public static void invalidRuleException(ParserRuleContext ctx, String visitorName) {
    String msg = "No matching rule for " + visitorName + ", this is a bug in compiler";
    errorHandler(ctx, msg);
    System.exit(INTERNAL_ERROR_CODE);
  }

  public static void branchStatementPositionError(ParserRuleContext ctx, JumpType type) {
    StringBuilder msg = new StringBuilder();
    msg.append(type.name().toLowerCase() + " not within a loop");
    switch (type) {
      case BREAK:
        msg.append(" or switch statement");
        break;
      case CONTINUE:
      default:
        msg.append("");
    }
    errorHandler(ctx, msg.toString());
    System.exit(SYNTAX_ERROR_CODE);
  }

  public static void branchStatementMutipleError(ParserRuleContext ctx, JumpType type) {
    String msg = type.name().toLowerCase() + " occured several times.";
    errorHandler(ctx, msg);
    System.exit(SEMANTIC_ERROR_CODE);
  }

  /* private common handler of all types of errors */
  private static void errorHandler(ParserRuleContext ctx, String msg) {
    int lineNum;
    int charPos;

        /* when ctx is null, it indicates that there is a funcJunk error or symbolRedeclare error
           in SemanticChecker */
    if (ctx == null) {
      System.err.println(msg);
      return;
    }

    if (ctx instanceof TerminalNode) {
      lineNum = ((TerminalNode) ctx).getSymbol().getLine();
      charPos = ((TerminalNode) ctx).getSymbol().getCharPositionInLine();
    } else {
      lineNum = ctx.getStart().getLine();
      charPos = ctx.getStart().getCharPositionInLine();
    }

    /* print line number and char position before the error message */
    System.err.println("line " + lineNum + ":" + charPos + " " + msg);
  }

}
