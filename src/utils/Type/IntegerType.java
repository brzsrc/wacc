package utils.Type;

public class IntegerType implements WACCType<Integer> {
  public static int MAX_VALUE = (int) (Math.pow(2, 31) - 1);
  public static int MIN_VALUE = (int) -Math.pow(2, 31);
}
