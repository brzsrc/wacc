package utils;

import frontend.node.Node;
import frontend.node.expr.*;
import frontend.node.expr.BinopNode.Binop;
import frontend.node.expr.UnopNode.Unop;
import frontend.type.ArrayType;
import frontend.type.BasicType;
import frontend.type.BasicTypeEnum;
import frontend.type.PairType;
import frontend.type.Type;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import org.antlr.v4.runtime.ParserRuleContext;
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

  /* char array type would be the same as string for printf */
  public static final Type CHAR_ARRAY_TYPE = new ArrayType(CHAR_BASIC_TYPE);

  /* a list of allowed types in read, free, cmp statement */
  public static final Set<Type> readStatAllowedTypes = new HashSet<>(
      Arrays.asList(STRING_BASIC_TYPE, INT_BASIC_TYPE, CHAR_BASIC_TYPE));
  public static final Set<Type> freeStatAllowedTypes = new HashSet<>(
      Arrays.asList(ARRAY_TYPE, PAIR_TYPE));
  public static final Set<Type> cmpStatAllowedTypes = new HashSet<>(
      Arrays.asList(STRING_BASIC_TYPE, INT_BASIC_TYPE, CHAR_BASIC_TYPE));

  /* mapping from string literals to internal representations of UnopEnum and Type */
  public static final Map<String, Unop> unopEnumMapping = Map.of(
      "-", Unop.MINUS,
      "chr", Unop.CHR,
      "!", Unop.NOT,
      "len", Unop.LEN,
      "ord", Unop.ORD
  );
  public static final Map<String, Type> unopTypeMapping = Map.of(
      "-", INT_BASIC_TYPE,
      "chr", INT_BASIC_TYPE,
      "!", BOOL_BASIC_TYPE,
      "len", ARRAY_TYPE,
      "ord", CHAR_BASIC_TYPE
  );
  public static final Map<String, Binop> binopEnumMapping = Map.of(
      "+", Binop.PLUS,
      "-", Binop.MINUS,
      "*", Binop.MUL,
      "/", Binop.DIV,
      "%", Binop.MOD
  );
  public static final Map<String, Binop> EqEnumMapping = Map.of(
      "==", Binop.EQUAL,
      "!=", Binop.INEQUAL
  );
  public static final Map<String, Binop> CmpEnumMapping = Map.of(
      ">", Binop.GREATER,
      ">=", Binop.GREATER_EQUAL,
      "<", Binop.LESS,
      "<=", Binop.LESS_EQUAL
  );
  public static final Map<Character, Character> escCharMap = Map.of(
      '0', '\0',
      'b', '\b',
      't', '\t',
      'n', '\n',
      'f', '\f',
      'r', '\r',
      '\"', '\"',
      '\'', '\'',
      '\\', '\\'
  );

  public static final Map<Binop, BiFunction<Integer, Integer, ExprNode>> arithmeticApplyMap = Map.of(
          Binop.PLUS, ((x, y) -> arithmeticWithCheck(x, y, Math::addExact)),
          Binop.MINUS, ((x, y) -> arithmeticWithCheck(x, y, Math::subtractExact)),
          Binop.MUL, ((x, y) -> arithmeticWithCheck(x, y, Math::multiplyExact)),
          Binop.DIV, ((x, y) -> new IntegerNode(x / y)),
          Binop.MOD, ((x, y) -> new IntegerNode(x % y))
  );

  public static final Map<Binop, BiFunction<Integer, Integer, Boolean>> cmpMap = Map.of(
          Binop.GREATER, ((x, y) -> x > y),
          Binop.GREATER_EQUAL, ((x,  y) -> x >= y),
          Binop.LESS, ((x, y) -> x < y),
          Binop.LESS_EQUAL, ((x, y) -> x <= y),
          Binop.EQUAL, ((x, y) -> x.compareTo(y) == 0),
          Binop.INEQUAL, ((x, y) -> x.compareTo(y) != 0),
          Binop.AND, ((x, y) -> (x & y) == 1),
          Binop.OR, ((x, y) -> (x | y) == 0)
  );

  public static final Map<Unop, Function<ExprNode, ExprNode>> unopApplyMap = Map.of(
          Unop.MINUS, (x -> arithmeticWithCheck(0, x.getCastedVal(), Math::subtractExact)),
          Unop.NOT, (x -> new BoolNode(x.getCastedVal() != 1)),
          Unop.LEN, (x -> new IntegerNode(x.getCastedVal())),
          Unop.ORD, (x -> new IntegerNode(x.getCastedVal())),
          Unop.CHR, (x -> new CharNode((char) x.getCastedVal()))
  );

  private static ExprNode arithmeticWithCheck(int a, int b, BinaryOperator<Integer> exactOperator) {
    try {
      return new IntegerNode(exactOperator.apply(a, b));
    } catch (ArithmeticException e) {
      System.out.println("WARNING: arithmetic " + " on " + a + " and " + b + " will cause overflow");
    }
    /* return null, inform upper caller to return the original node */
    return null;
  }

  /* error code used in ErrorHandlers */
  public static final int SYNTAX_ERROR_CODE = 100;
  public static final int SEMANTIC_ERROR_CODE = 200;
  public static final int INTERNAL_ERROR_CODE = 300;

  /* word, byte size in unit: byte */
  public static final int WORD_SIZE = 4, BYTE_SIZE = 1, POINTER_SIZE = WORD_SIZE;

  public static final int TRUE = 1;
  public static final int FALSE = 0;

  /* ARM assembly headers */
  public static String BRANCH_HEADER = "L";
  public static String MSG_HEADER = "msg_";
  public static String FUNC_HEADER = "f_";
  public static String MAIN_BODY_NAME = "main";

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

  /* system call instruction */
  public enum SystemCallInstruction {
    MALLOC, PUTCHAR, SCANF, EXIT, PRINTF, FFLUSH, PUTS, FREE;

    @Override
    public String toString() {
      return name().toLowerCase();
    }
  }

  /* ARM routine instruction */
  public enum RoutineInstruction {
    READ_INT, READ_CHAR, PRINT_INT, PRINT_BOOL, PRINT_CHAR, PRINT_STRING, PRINT_REFERENCE, PRINT_LN,
    CHECK_DIVIDE_BY_ZERO, THROW_RUNTIME_ERROR, CHECK_ARRAY_BOUND, FREE_ARRAY, FREE_PAIR, CHECK_NULL_POINTER,
    THROW_OVERFLOW_ERROR;

    @Override
    public String toString() {
      if (this == PRINT_CHAR) {
        return SystemCallInstruction.PUTCHAR.toString();
      }
      return "p_" + name().toLowerCase();
    }
  }

  /**
   * functions used in optimisations */
}
