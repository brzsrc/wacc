package frontend.type;

import static utils.backend.Utils.*;

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
    switch (basicTypeEnum) {
      case CHAR:
      case BOOLEAN:
        return BYTE_SIZE;
      case INTEGER:
        return WORD_SIZE;
      case STRING:
        return POINTER_SIZE;
      default:
        throw new IllegalArgumentException("getSize on Illegal basicTypeNum" + basicTypeEnum);
    }
  }
}
