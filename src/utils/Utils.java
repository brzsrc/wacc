package utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import frontend.node.expr.BinopNode.Binop;
import frontend.node.expr.IdentNode;
import frontend.node.expr.UnopNode.Unop;
import org.antlr.v4.runtime.ParserRuleContext;

import frontend.node.expr.ExprNode;
import frontend.type.ArrayType;
import frontend.type.BasicType;
import frontend.type.BasicTypeEnum;
import frontend.type.PairType;
import frontend.type.Type;
import utils.frontend.SemanticErrorHandler;
import utils.frontend.symbolTable.Symbol;
import utils.frontend.symbolTable.SymbolTable;

public class Utils {

  /**
   * Utils class contains static variables and helper functions used in SemanticCheck
   */

  /* Type classes to represent BasicType, ArrayType, and PairType, used in type comparisons throughout the SemanticChecker */
  public static final Type INT_BASIC_TYPE = new BasicType(BasicTypeEnum.INTEGER);
  public static final Type BOOL_BASIC_TYPE = new BasicType(BasicTypeEnum.BOOLEAN);
  public static final Type CHAR_BASIC_TYPE = new BasicType(BasicTypeEnum.CHAR);
  public static final Type STRING_BASIC_TYPE = new BasicType(BasicTypeEnum.STRING);
  public static final Type ARRAY_TYPE = new ArrayType();
  public static final Type PAIR_TYPE = new PairType();

  /* a list of allowed types in read, free, cmp statement */
  public static final Set<Type> readStatAllowedTypes = new HashSet<>(Arrays.asList(STRING_BASIC_TYPE, INT_BASIC_TYPE, CHAR_BASIC_TYPE));
  public static final Set<Type> freeStatAllowedTypes = new HashSet<>(Arrays.asList(ARRAY_TYPE, PAIR_TYPE));
  public static final Set<Type> cmpStatAllowedTypes = new HashSet<>(Arrays.asList(STRING_BASIC_TYPE, INT_BASIC_TYPE, CHAR_BASIC_TYPE));

  /* mapping from string literals to internal representations of UnopEnum and Type */
  public static final Map<String, Unop> unopEnumMapping = new HashMap<String, Unop>(){{
    put("-", Unop.MINUS);
    put("chr", Unop.CHR);
    put("!", Unop.NOT);
    put("len", Unop.LEN);
    put("ord", Unop.ORD);
  }};
  public static final Map<String, Type> unopTypeMapping = new HashMap<String, Type>(){{
    put("-", INT_BASIC_TYPE);
    put("chr", INT_BASIC_TYPE);
    put("!", BOOL_BASIC_TYPE);
    put("len", ARRAY_TYPE);
    put("ord", CHAR_BASIC_TYPE);
  }};
  public static final Map<String, Binop> binopEnumMapping = new HashMap<String, Binop>(){{
    put("+", Binop.PLUS);
    put("-", Binop.MINUS);
    put("*", Binop.MUL);
    put("/", Binop.DIV);
    put("%", Binop.MOD);
  }};
  public static final Map<String, Binop> EqEnumMapping = new HashMap<String, Binop>(){{
    put("==", Binop.EQUAL);
    put("!=", Binop.INEQUAL);
  }};
  public static final Map<String, Binop> LogicOpEnumMapping = new HashMap<String, Binop>(){{
    put("&&", Binop.AND);
    put("||", Binop.OR);
  }};
  public static final Map<String, Binop> CmpEnumMapping = new HashMap<String, Binop>(){{
    put(">", Binop.GREATER);
    put(">=", Binop.GREATER_EQUAL);
    put("<", Binop.LESS);
    put("<=", Binop.LESS_EQUAL);
  }};
  public static final Map<Character, Character> escCharMap = new HashMap<Character, Character>(){{
    put('0', '\0');
    put('b', '\b');
    put('t', '\t');
    put('n', '\n');
    put('f', '\f');
    put('r', '\r');
    put('\"', '\"');
    put('\'', '\'');
    put('\\', '\\');
  }};

  /* error code used in ErrorHandlers */
  public static final int SYNTAX_ERROR_CODE = 100;
  public static final int SEMANTIC_ERROR_CODE = 200;
  public static final int INTERNAL_ERROR_CODE = 300;

  /* word, byte size in unit: byte */
  public static final int WORD_SIZE = 4, BYTE_SIZE = 1, POINTER_SIZE = WORD_SIZE;

  public static final int TRUE = 1;
  public static final int FALSE = 0;

  /* adding a private constructor to override the default public constructor in order to 
     indicate Utils class cannot be instantiated */
  private Utils() {
    throw new IllegalStateException("Utility Class cannot be instantiated!");
  }

  /* wrapper functions for checking the types and throw an error if there is a mismatch */
  public static boolean typeCheck(ParserRuleContext ctx, Set<Type> expected, Type actual) {
    if (expected.stream().noneMatch(actual::equalToType)) {
      SemanticErrorHandler.typeMismatch(ctx, expected, actual);
      return true;
    }
    return false;
  }

  public static boolean typeCheck(ParserRuleContext ctx, Type expected, Type actual) {
    if (!actual.equalToType(expected)) {
      SemanticErrorHandler.typeMismatch(ctx, expected, actual);
      return true;
    }
    return false;
  }


  public static boolean typeCheck(ParserRuleContext ctx, String varName, Type expected,
      Type actual) {
    if (!actual.equalToType(expected)) {
      SemanticErrorHandler.typeMismatch(ctx, varName, expected, actual);
      return true;
    }
    return false;
  }

  public static Symbol lookUpWithNotFoundException(ParserRuleContext ctx, SymbolTable table,
      String varName) {
    Symbol value = table.lookupAll(varName);
    if (value == null) {
      SemanticErrorHandler.symbolNotFound(ctx, varName);
    }
    return value;
  }

  /* parse an integer from @param String intExt */
  public static Integer intParse(ParserRuleContext ctx, String intExt) {
    int integer = 0;
    try {
      integer = Integer.parseInt(intExt);
    } catch (NumberFormatException e) {
      SemanticErrorHandler.integerRangeError(ctx, intExt);
    }
    return integer;
  }

  /* check whether @param String s represents a number */
  public static boolean isInteger(String s) {
    return s.matches("[0-9]+");
  }

  public static boolean isCharInRange(int intVal) {
    return intVal >= 0 && intVal < 128;
  }
}
