package utils;

import utils.Type.*;

public class Utils {

  public static void check(WACCType type, Class target) {
    if (type.getClass() != target) {
      throw new IllegalArgumentException("Semantic check: type failed to match");
    }
  }
}
