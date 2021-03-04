package frontend.type;

import static utils.Utils.BYTE_SIZE;
import static utils.Utils.POINTER_SIZE;

import java.util.Objects;

public class ArrayType implements Type {

  private final int ARRAY_HASH_CODE = 20;
  private final Type contentType;
  private final int depth;

  public ArrayType(Type contentType) {
    this.contentType = contentType;

    Type subType = contentType;
    int depth = 1;
    while (subType instanceof ArrayType) {
      subType = subType.asArrayType().getContentType();
      depth++;
    }
    this.depth = depth;
  }

  public ArrayType() {
    this(null);
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
    return POINTER_SIZE;
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
      hash += POINTER_SIZE;
    }
    return hash;
  }
}
