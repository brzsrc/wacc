package utils.Type;

public class StringType implements Type<String> {
  private String string;
  private int length;

  public StringType(String string) {
    this.string = string;
    this.length = string.length();
  }

  @Override
  public String getValue() {
    return this.string;
  }

  @Override
  public void setValue(String value) {
    this.string = value;
    this.length = value.length();
  }

  public int getLength() {
    return this.length;
  }
}
