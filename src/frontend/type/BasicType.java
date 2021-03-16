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
    switch (basicTypeEnum) {
      case CHAR:
      case BOOLEAN:
        return BYTE_SIZE;
      case INTEGER:
        return WORD_SIZE;
      case STRING:
        // todo: change returned value to arch.equals(AssemblyArchitecture.ARMv6) ? Utils.ARM_POINTER_SIZE : Utils.INTEL_POINTER_SIZE
        return QUAD_SIZE;
      default:
        throw new IllegalArgumentException("unsupported base type enum: " + basicTypeEnum);
    }
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
