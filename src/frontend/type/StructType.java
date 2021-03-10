package frontend.type;

import static utils.Utils.POINTER_SIZE;

public class StructType implements Type {

  private String name;

  public StructType(String name) {
    this.name = name;
  }

  public StructType() { name = null; }

  public String getName() {
    return name;
  }

  @Override
  public boolean equalToType(Type other) {
    if (other == null) {
      return true;
    }

    if (!(other instanceof StructType)) {
      return false;
    }

    StructType otherStruct = (StructType) other;

    if (name == null || otherStruct.name == null) {
      return true;
    }

    return name.equals(otherStruct.getName());
  }

  @Override
  public void showType() {
    System.out.print(this.toString());
  }

  @Override
  public int getSize() {
    return POINTER_SIZE;
  }

  @Override
  public String toString() {
    return "struct " + ((name != null) ? name : "\"name\"");
  }
}
