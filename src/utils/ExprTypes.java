package utils;

import antlr.WACCParser;

/**
 * each class in ExprType is a type an Expr can take,
 * */
public class ExprTypes {
  public static class IntegerType implements TypeSystem {
    int val;
    public IntegerType(Integer val) {
      this.val = val;
    }
  }

  public static class BoolType implements TypeSystem {
    boolean bVal;
    public BoolType(boolean bVal) {
      this.bVal = bVal;
    }
  }

  public static class ArrayType implements TypeSystem {
    // todo: add field of ArrayType
  }

  public static class CharType implements TypeSystem {
    // todo: add field of CharType
  }

  // todo: add class for all other types in expr rule


}