package utils.Type;

public class CharType implements WACCType<Character> {
  private char c;
  private int ascii;

  public CharType(char c) {
      this.c = c;
      this.ascii = c;
  }

  @Override
  public Character getValue() {
    return this.c;
  }

  @Override
  public void setValue(Character value) {
    this.c = value;
    this.ascii = value;
  }

  public int getAsciiValue() {
    return this.ascii;
  }
}
