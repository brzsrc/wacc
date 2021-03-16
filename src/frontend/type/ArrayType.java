package frontend.type;

import static utils.Utils.ARM_POINTER_SIZE;
import static utils.Utils.BYTE_SIZE;
import static utils.Utils.INTEL_POINTER_SIZE;

import java.util.Objects;
import utils.Utils.AssemblyArchitecture;

public class ArrayType implements Type {

  private final int ARRAY_HASH_CODE = 20;
  private final Type contentType;
  private final int depth;
  private final AssemblyArchitecture arch;

  public ArrayType(Type contentType, AssemblyArchitecture arch) {
    this.contentType = contentType;

    Type subType = contentType;
    int depth = 1;
    while (subType instanceof ArrayType) {
      subType = subType.asArrayType().getContentType();
      depth++;
    }
    this.depth = depth;
    this.arch = arch;
  }

  public ArrayType(AssemblyArchitecture arch) {
    this(null, arch);
  }

  @Override
  public boolean equalToType(Type other) {
    if (other == null || contentType == null) {
      return true;
    }
    if (!(other instanceof ArrayType)) {
      return false;
    }

    return contentType.equalToType(((ArrayType) other).getContentType());
  }

  public int getDepth() {
    return depth;
  }

  public Type getContentType() {
    return contentType;
  }

  @Override
  public String toString() {
    return "Array<" + contentType + ">";
  }

  @Override
  public ArrayType asArrayType() {
    return this;
  }

  @Override
  public void showType() {
    contentType.showType();
    System.out.print("[]");
  }

  @Override
  public int getSize() {
    return arch.equals(AssemblyArchitecture.ARMv6) ? ARM_POINTER_SIZE : INTEL_POINTER_SIZE;
  }

  @Override
  public boolean equals(Object obj) {
    return hashCode() == obj.hashCode();
  }

  @Override
  public int hashCode() {
    int hash = ARRAY_HASH_CODE;
    if (contentType != null &&
            contentType.equalToType(new BasicType(BasicTypeEnum.CHAR))) {
      hash += BYTE_SIZE;
    } else {
      hash += arch.equals(AssemblyArchitecture.ARMv6) ? ARM_POINTER_SIZE : INTEL_POINTER_SIZE;
    }
    return hash;
  }
}
