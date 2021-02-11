package utils;

import java.util.Set;

import org.antlr.v4.runtime.ParserRuleContext;

import antlr.WACCParser.Pair_elemContext;
import node.expr.ExprNode;
import type.Type;

import static utils.SemanticErrorHandler.SEMANTIC_ERROR_CODE;

public class Utils {
    /* Helper functions */

  public static boolean typeCheck(ParserRuleContext ctx, Type expected, Type actual) {
    if (!actual.equalToType(expected)) {
      SemanticErrorHandler.typeMismatch(ctx, expected, actual);
      return true;
    }
    return false;
  }

  public static boolean typeCheck(ParserRuleContext ctx, Set<Type> expected, Type actual) {
    if (expected.stream().noneMatch(actual::equalToType)) {
      SemanticErrorHandler.typeMismatch(ctx, expected, actual);
      return true;
    }
    return false;
  }

  public static boolean typeCheck(ParserRuleContext ctx, String varName, Type expected, Type actual) {
    if (!actual.equalToType(expected)) {
      SemanticErrorHandler.typeMismatch(ctx, varName, expected, actual);
      return true;
    }
    return false;
  }

  public static ExprNode lookUpWithNotFoundException(ParserRuleContext ctx, SymbolTable table, String varName) {
    ExprNode value = table.lookupAll(varName);
    if (value == null) {
      SemanticErrorHandler.symbolNotFound(ctx, varName);
      /* symbolNotFound error will call system exit, value will not be null */
    }

    return value;
  }

  public static Integer intParse(ParserRuleContext ctx, String intExt) {
    int integer = 0;
    try {
      integer = Integer.parseInt(intExt);
    } catch (NumberFormatException e) {
      SemanticErrorHandler.integerRangeError(ctx, intExt);
      /* intError will terminate semantic check */
    }
    return integer;
  }

  public static boolean isInteger(String s) {
    return s.matches("[0-9]+");
  }
}
