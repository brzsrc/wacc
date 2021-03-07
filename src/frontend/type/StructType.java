package frontend.type;

import static utils.Utils.POINTER_SIZE;

public class StructType implements Type {

  private final String name;

  public StructType(String name) {
    this.name = name;
  }

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

    return name.equals(((StructType) other).getName());
  }

  @Override
  public void showType() {

  }

  @Override
  public int getSize() {
    return POINTER_SIZE;
  }
}
