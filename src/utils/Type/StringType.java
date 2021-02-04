package utils.Type;

public class StringType implements Type {

  @Override
  public String getTypeName() {
    return "String";
  }

  @Override
  public boolean equalToType(Type other) {
    return this.getClass().equals(other.getClass());
  }

  
}
