package Type;

public class CharType implements Type {

  @Override
  public boolean equalToType(Type other) {
    return this.getClass().equals(other.getClass());
  }

  @Override
  public String toString() {
    return "Character";
  }
  
}
