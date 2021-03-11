package backend.arm.instructions.addressing;

import backend.common.address.Immediate;

public class ARMImmediate extends Immediate {

  private final BitNum bitNum;
  public ARMImmediate(int val, BitNum bitNum) {
    this(val, bitNum, false);
  }

  public ARMImmediate(int val, BitNum bitNum, boolean isChar) {
    super(val, null, isChar, false);
    this.bitNum = bitNum;
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
    return assemble();
  }

  @Override
  public String assemble() {
    if (isChar) {
      return "#'" + (char) val + "'";
    }
    return "#" + val;
  }

  public enum BitNum {SHIFT10, SHIFT32, CONST8, CONST5, CONST12, CONST16}
}
