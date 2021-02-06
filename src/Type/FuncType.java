package Type;

import java.util.List;

public class FuncType implements Type {

  private Type returnType;
  private List<Type> paramList;

  public FuncType(Type returnType, List<Type> paramList) {
    this.returnType = returnType;
    this.paramList = paramList;
  }

  @Override
  public boolean equalToType(Type other) {
    return returnType.equalToType(other);
  }

  @Override
  public String toString() {
    StringBuilder typeName = new StringBuilder("Function<");
    for (Type type : paramList) {
      typeName.append(type.toString() + ", ");
    }
    
    return typeName.substring(0, typeName.length() - 2) + ">";
  }
}
