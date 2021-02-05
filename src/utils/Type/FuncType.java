package utils.Type;

import java.util.List;

public class FuncType implements Type {

  private Type returnType;
  private List<Type> param_list;

  public FuncType(Type returnType, List<Type> param_list) {
    this.returnType = returnType;
    this.param_list = param_list;
  }

  @Override
  public boolean equalToType(Type other) {
    return returnType.equalToType(other);
  }

  @Override
  public String getTypeName() {
    return "Function";
  }
}
