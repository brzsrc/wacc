package frontend.type;

import static utils.Utils.*;

import utils.Utils;

public class BasicType implements Type {

  private final BasicTypeEnum basicTypeEnum;
  private final AssemblyArchitecture arch;

  public BasicType(BasicTypeEnum basicTypeEnum, AssemblyArchitecture arch) {
    this.basicTypeEnum = basicTypeEnum;
    this.arch = arch;
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
    return basicTypeEnum.toString().toLowerCase();
  }

  @Override
  public void showType() {
    System.out.print(basicTypeEnum);
  }

  @Override
  public int getSize() {
    switch (basicTypeEnum) {
      case CHAR:
      case BOOL:
        return BYTE_SIZE;
      case INT:
        return WORD_SIZE;
      case STRING:
        System.out.println("using intel string = " + INTEL_POINTER_SIZE );
        System.out.println("arch is " + arch.name());
        return arch.equals(AssemblyArchitecture.ARMv6) ? Utils.ARM_POINTER_SIZE : Utils.INTEL_POINTER_SIZE;
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
