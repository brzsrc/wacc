package Type;

public class StringType implements Type {

  @Override
  public boolean equalToType(Type other) {
    return this.getClass().equals(other.getClass());
  }

  @Override
  public String toString() {
    return "String";
  }
  
}
