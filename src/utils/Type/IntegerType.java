package utils.Type;

public class IntegerType implements Type {
  public static int MAX_VALUE = (int) (Math.pow(2, 31) - 1);
  public static int MIN_VALUE = (int) -Math.pow(2, 31);

  @Override
  public String getTypeName() {
    return "Integer";
  }

  @Override
  public boolean equalToType(Type other) {
    return this.getClass().equals(other.getClass());
  }
}
