package frontend.type;

import static utils.Utils.*;

public class BasicType implements Type {

  private final BasicTypeEnum basicTypeEnum;

  public BasicType(BasicTypeEnum basicTypeEnum) {
    this.basicTypeEnum = basicTypeEnum;
  }

  @Override
  public boolean equalToType(Type other) {
    if (other == null) {
      return true;
    }
    if (!(other instanceof BasicType)) {
      return false;
    }

    return basicTypeEnum.equals(((BasicType) other).getTypeEnum());
  }



  public BasicTypeEnum getTypeEnum() {
    return basicTypeEnum;
  }

  @Override
  public String toString() {
    return basicTypeEnum.toString();
  }

  @Override
  public void showType() {
    System.out.print(basicTypeEnum);
  }

  @Override
  public int getSize() {
    return switch (basicTypeEnum) {
      case CHAR, BOOLEAN -> BYTE_SIZE;
      case INTEGER -> WORD_SIZE;
      case STRING -> QUAD_SIZE;
    };
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Type) {
      return this.equalToType((Type) obj);
    }
    
    return false;
  }

  @Override
  public int hashCode() {
    return basicTypeEnum.hashCode();
  }
}
