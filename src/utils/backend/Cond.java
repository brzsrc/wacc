package utils.backend;

public enum Cond {
  NULL,
  EQ,
  LT,
  VS,
  S,
  CS;

  @Override
  public String toString() {
    switch (this) {
      case NULL:
        return "";
      default:
        return super.toString();
    }
  }
}
