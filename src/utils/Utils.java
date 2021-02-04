package utils;

import utils.TypeSystem.*;

public class Utils {

  public static void check(TypeSystem type, Class target) {
    if (type.getClass() != target) {
      throw new IllegalArgumentException("Semantic check: type failed to match");
    }
  }
}
