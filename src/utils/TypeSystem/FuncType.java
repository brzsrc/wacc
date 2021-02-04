package utils.TypeSystem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class FuncType implements TypeSystem {
  public TypeSystem returnType;
  public List<TypeSystem> parameters;
  public List<Instruction> functionBody;

  public FuncType(TypeSystem returnType, List<Instruction> functionBody, TypeSystem... params) {
    this.returnType = returnType;
    this.functionBody = functionBody;
    this.parameters = new ArrayList<>(Arrays.asList(params));
  }

  /** return function instance, implement ord unary operator */
  public static FuncType ord(CharType expr) {
    return new FuncType(IntegerType.defaultInstance(), null, expr);
  }

  /** return function instance, implement chr unary operator */
  public static FuncType chr(IntegerType expr) {
    return new FuncType(IntegerType.defaultInstance(), null, expr);
  }
}
