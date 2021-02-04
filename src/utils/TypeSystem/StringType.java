package utils.TypeSystem;

public class StringType implements TypeSystem {
  public String s;

  public StringType(String s) {
    this.s = s;
  }

  public static StringType defaultInstance() {
    return new StringType("");
  }
    
}
