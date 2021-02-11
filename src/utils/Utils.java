package utils;

import java.util.Map;
import java.util.Set;

import node.expr.BinopNode.Binop;
import node.expr.UnopNode.Unop;
import org.antlr.v4.runtime.ParserRuleContext;

import antlr.WACCParser.Pair_elemContext;
import node.expr.ExprNode;
import type.ArrayType;
import type.BasicType;
import type.BasicTypeEnum;
import type.PairType;
import type.Type;

import static utils.SemanticErrorHandler.SEMANTIC_ERROR_CODE;

public class Utils {

  /* Type classes to represent BasicType, ArrayType, and PairType, used in type comparisons throughout the SemanticChecker */
  public static final Type INT_BASIC_TYPE = new BasicType(BasicTypeEnum.INTEGER);
  public static final Type BOOL_BASIC_TYPE = new BasicType(BasicTypeEnum.BOOLEAN);
  public static final Type CHAR_BASIC_TYPE = new BasicType(BasicTypeEnum.CHAR);
  public static final Type STRING_BASIC_TYPE = new BasicType(BasicTypeEnum.STRING);
  public static final Type ARRAY_TYPE = new ArrayType();
  public static final Type PAIR_TYPE = new PairType();

  /* a list of allowed types in read, free, cmp statement */
  public static final Set<Type> readStatAllowedTypes = Set.of(STRING_BASIC_TYPE, INT_BASIC_TYPE, CHAR_BASIC_TYPE);
  public static final Set<Type> freeStatAllowedTypes = Set.of(ARRAY_TYPE, PAIR_TYPE);
  public static final Set<Type> cmpStatAllowedTypes = Set.of(STRING_BASIC_TYPE, INT_BASIC_TYPE, CHAR_BASIC_TYPE);

  /* mapping from string literals to internal representations of UnopEnum and Type */
  public static final Map<String, Unop> unopEnumMapping = Map.of("-", Unop.MINUS,
      "chr", Unop.CHR,
      "!", Unop.NOT,
      "len", Unop.LEN,
      "ord", Unop.ORD);
  public static final Map<String, Type> unopTypeMapping = Map.of("-", INT_BASIC_TYPE,
      "chr", INT_BASIC_TYPE,
      "!", BOOL_BASIC_TYPE,
      "len", ARRAY_TYPE,
      "ord", CHAR_BASIC_TYPE);
  public static final Map<String, Binop> binopEnumMapping = Map.of("+", Binop.PLUS,
      "-", Binop.MINUS,
      "*", Binop.MUL,
      "/", Binop.DIV,
      "%", Binop.MOD);
  public static final Map<String, Binop> EqEnumMapping = Map.of("==", Binop.EQUAL, "!=", Binop.INEQUAL);
  public static final Map<String, Binop> LogicOpEnumMapping = Map.of("&&", Binop.AND, "||", Binop.OR);
  public static final Map<String, Binop> CmpEnumMapping = Map.of(">", Binop.GREATER, ">=", Binop.GREATER_EQUAL,
      "<", Binop.LESS, "<=", Binop.LESS_EQUAL);


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
