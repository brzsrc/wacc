package utils;

import frontend.antlr.WACCParser.FuncContext;
import frontend.antlr.WACCParser.ParamContext;
import frontend.node.expr.*;
import frontend.node.expr.BinopNode.Binop;
import frontend.node.expr.UnopNode.Unop;
import frontend.type.ArrayType;
import frontend.type.BasicType;
import frontend.type.BasicTypeEnum;
import frontend.type.PairType;
import frontend.type.StructType;
import frontend.type.Type;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;

import org.antlr.v4.runtime.ParserRuleContext;
import utils.frontend.SemanticErrorHandler;
import utils.frontend.symbolTable.Symbol;
import utils.frontend.symbolTable.SymbolTable;

public class Utils {

  /**
   * Utils class contains static variables and helper functions used in SemanticCheck
   */

  /* Type classes to represent BasicType, ArrayType, and PairType, used in type comparisons throughout the SemanticChecker */
  public static final Type INT_BASIC_TYPE = new BasicType(BasicTypeEnum.INT, AssemblyArchitecture.ARMv6);
  public static final Type BOOL_BASIC_TYPE = new BasicType(BasicTypeEnum.BOOL, AssemblyArchitecture.ARMv6);
  public static final Type CHAR_BASIC_TYPE = new BasicType(BasicTypeEnum.CHAR, AssemblyArchitecture.ARMv6);
  public static final Type STRING_BASIC_TYPE = new BasicType(BasicTypeEnum.STRING, AssemblyArchitecture.ARMv6);
  public static final Type ARRAY_TYPE = new ArrayType(AssemblyArchitecture.ARMv6);
  public static final Type PAIR_TYPE = new PairType(AssemblyArchitecture.ARMv6);
  public static final Type STRUCT_TYPE = new StructType("",  AssemblyArchitecture.ARMv6);

  /* char array type would be the same as string for printf */
  public static final Type CHAR_ARRAY_TYPE = new ArrayType(CHAR_BASIC_TYPE, AssemblyArchitecture.ARMv6);

  /* a list of allowed types in read, free, cmp statement */
  public static final Set<Type> readStatAllowedTypes = new HashSet<>(
      Arrays.asList(STRING_BASIC_TYPE, INT_BASIC_TYPE, CHAR_BASIC_TYPE));
  public static final Set<Type> freeStatAllowedTypes = new HashSet<>(
      Arrays.asList(ARRAY_TYPE, PAIR_TYPE, STRUCT_TYPE));
  public static final Set<Type> cmpStatAllowedTypes = new HashSet<>(
      Arrays.asList(STRING_BASIC_TYPE, INT_BASIC_TYPE, CHAR_BASIC_TYPE));

  /* mapping from string literals to internal representations of UnopEnum and Type */
  public static final Map<String, Unop> unopEnumMapping = Map.of(
      "-", Unop.MINUS,
      "chr", Unop.CHR,
      "!", Unop.NOT,
      "len", Unop.LEN,
      "ord", Unop.ORD,
      "~", Unop.COMPLEMENT
  );
  public static final Map<String, Type> unopTypeMapping = Map.of(
      "-", INT_BASIC_TYPE,
      "chr", INT_BASIC_TYPE,
      "!", BOOL_BASIC_TYPE,
      "len", ARRAY_TYPE,
      "ord", CHAR_BASIC_TYPE,
      "~", INT_BASIC_TYPE
  );
  public static final Map<String, Binop> binopEnumMapping = Map.of(
      "+", Binop.PLUS,
      "-", Binop.MINUS,
      "*", Binop.MUL,
      "/", Binop.DIV,
      "%", Binop.MOD
  );

  public static final Map<String, Binop> bitwiseOpEnumMapping = Map.of(
      "|", Binop.OR,
      "&", Binop.AND
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
          Binop.BITAND, ((x, y) -> y == 0 ? null : new IntegerNode(x & y, AssemblyArchitecture.ARMv6)),
          Binop.BITOR, ((x, y) -> y == 0 ? null : new IntegerNode(x | y, AssemblyArchitecture.ARMv6)),
          Binop.DIV, ((x, y) -> new IntegerNode(x / y, AssemblyArchitecture.ARMv6)),
          Binop.MOD, ((x, y) -> new IntegerNode(x % y, AssemblyArchitecture.ARMv6))
  );

  public static final Map<Binop, BiFunction<Integer, Integer, Boolean>> cmpMap = Map.of(
          Binop.GREATER, ((x, y) -> x > y),
          Binop.GREATER_EQUAL, ((x,  y) -> x >= y),
          Binop.LESS, ((x, y) -> x < y),
          Binop.LESS_EQUAL, ((x, y) -> x <= y),
          Binop.EQUAL, ((x, y) -> x.compareTo(y) == 0),
          Binop.INEQUAL, ((x, y) -> x.compareTo(y) != 0),
          Binop.AND, ((x, y) -> (x & y) == 1),
          Binop.OR, ((x, y) -> (x | y) == 1)
  );

  public static final Map<Unop, Function<ExprNode, ExprNode>> unopApplyMap = Map.of(
          Unop.MINUS, (x -> arithmeticWithCheck(0, x.getCastedVal(), Math::subtractExact)),
          Unop.NOT, (x -> new BoolNode(x.getCastedVal() != 1, AssemblyArchitecture.ARMv6)),
          Unop.LEN, (x -> new IntegerNode(x.getCastedVal(), AssemblyArchitecture.ARMv6)),
          Unop.ORD, (x -> new IntegerNode(x.getCastedVal(), AssemblyArchitecture.ARMv6)),
          Unop.CHR, (x -> new CharNode((char) x.getCastedVal(), AssemblyArchitecture.ARMv6))
  );

  private static ExprNode arithmeticWithCheck(int a, int b, BinaryOperator<Integer> exactOperator) {
    try {
      return new IntegerNode(exactOperator.apply(a, b), AssemblyArchitecture.ARMv6);
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
  public static final int QUAD_SIZE = 8, WORD_SIZE = 4, BYTE_SIZE = 1;
  public static final int ARM_POINTER_SIZE = WORD_SIZE, INTEL_POINTER_SIZE = QUAD_SIZE;

  public static final int TRUE = 1;
  public static final int FALSE = 0;

  /* ARM assembly headers */
  public static String BRANCH_HEADER = "L";
  public static String MSG_HEADER = "msg_";
  public static String FUNC_HEADER = "f_";
  public static String MAIN_BODY_NAME = "main";

  /* for function overload, to avoid name collision with user defined func name */
  public static String overloadSeparator = "_" + new Random().nextInt(100) + "_";

  /* for pre-compiler */
  public final static String stdlibPath = "src/wacc_lib/";
  public final static String stdlibFormatName = ".stdlib";
  public final static String waccFormatName = ".wacc";
  public final static String mediateFileSuffix = "_mid";
  public final static String outputFileSuffix = "_out";
  public final static String defineRuleContext = "define ";
  public final static String includeRuleContext = "include ";
  public final static String genericMark = "E";
  public final static String commentMark = "#";
  public final static String programBodyMark = "begin";
  public enum IntelInstructionSize {
    Q, L, W, B
  }

  public enum AssemblyArchitecture {
    ARMv6, Intelx86
  }

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

  /* helper function for function overload: append the type texts of all params to the func name */
  public static String findOverloadFuncName(FuncContext ctx) {
    String overloadName = ctx.IDENT().getText();
    if (ctx.param_list() != null) {
      for (ParamContext p : ctx.param_list().param()) {
        overloadName += (overloadSeparator + p.type().getText());
      }
    }
    return formatFuncName(overloadName);
  }

  /* helper function for function overload: replace all invalid func name char with the underline */
  public static String formatFuncName(String funcName) {
    return funcName.replace(" ", "")
        .replace("[]", overloadSeparator + "array").replaceAll("[(),]", overloadSeparator);
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

  /* TODO: better code quality here */
  public static final Map<IntelInstructionSize, Integer> intelSizeToInt = Map.of(
      IntelInstructionSize.Q, 8,
      IntelInstructionSize.L, 4,
      IntelInstructionSize.W, 2,
      IntelInstructionSize.B, 1
  );

  public static final Map<Integer, IntelInstructionSize> intToIntelSize = Map.of(
      8, IntelInstructionSize.Q,
      4, IntelInstructionSize.L,
      2, IntelInstructionSize.W,
      1, IntelInstructionSize.B
  );

  public static String calculateSize(IntelInstructionSize size1, IntelInstructionSize size2) {
    return intToIntelSize.get(Math.max(intelSizeToInt.get(size1), intelSizeToInt.get(size2))).toString().toLowerCase();
  }

  public static String calculateSize(IntelInstructionSize size) {
    return intToIntelSize.get(intelSizeToInt.get(size)).toString().toLowerCase();
  }

  /**
   * functions used in optimisations */
}
