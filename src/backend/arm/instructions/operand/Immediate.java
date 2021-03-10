package backend.arm.instructions.operand;

public class Immediate {

  private final int val;
  private final BitNum bitNum;
  private final boolean isChar;
  public Immediate(int val, BitNum bitNum) {
    this(val, bitNum, false);
  }

  public Immediate(int val, BitNum bitNum, boolean isChar) {
    this.val = val;
    this.bitNum = bitNum;
    this.isChar = isChar;
  }

  public boolean isChar() {
    return isChar;
  }

  public int getVal() {
    return val;
  }

  public BitNum getBitNum() {
    return bitNum;
  }

  @Override
  public String toString() {
    if (isChar) {
      return "#'" + (char) val + "'";
    }
    return "#" + val;
  }

  public enum BitNum {SHIFT10, SHIFT32, CONST8, CONST5, CONST12, CONST16}
}
