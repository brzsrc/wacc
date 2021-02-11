package utils;

import java.util.Set;

import org.antlr.v4.runtime.ParserRuleContext;

import antlr.WACCParser.Pair_elemContext;
import node.expr.ExprNode;
import type.Type;

public class Utils {
    /* Helper functions */

  public static void typeCheck(ParserRuleContext ctx, Type expected, Type actual) {
    if (!actual.equalToType(expected)) {
      SemanticErrorHandler.typeMismatch(ctx, expected, actual);
    }
  }

  public static void typeCheck(ParserRuleContext ctx, Set<Type> expected, Type actual) {
    if (expected.stream().noneMatch(actual::equalToType)) {
      SemanticErrorHandler.typeMismatch(ctx, expected, actual);
    }
  }

  public static void typeCheck(ParserRuleContext ctx, String varName, Type expected, Type actual) {
    if (!actual.equalToType(expected)) {
      SemanticErrorHandler.typeMismatch(ctx, varName, expected, actual);
    }
  }

  public static ExprNode lookUpWithNotFoundException(ParserRuleContext ctx, SymbolTable table, String varName) {
    ExprNode value = table.lookupAll(varName);
    if (value == null) {
      SemanticErrorHandler.symbolNotFound(ctx, varName);
    }

    return value;
  }

  public static Integer intParse(ParserRuleContext ctx, String intExt) {
    int integer = 0;
    try {
      integer = Integer.parseInt(intExt);
    } catch (NumberFormatException e) {
      SemanticErrorHandler.integerRangeError(ctx, intExt);
    }
    return integer;
  }

  public static boolean isInteger(String s) {
    return s.matches("[0-9]+");
  }
}
