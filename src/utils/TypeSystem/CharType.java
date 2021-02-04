package utils.TypeSystem;

public class CharType implements TypeSystem {
  public char c;

  public CharType(char c) {
    this.c = c;
  }

  public static CharType defaultInstance() {
    return new CharType('\0');
  }

}
