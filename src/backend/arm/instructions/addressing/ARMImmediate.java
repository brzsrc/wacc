package backend.arm.instructions.addressing;

import backend.arm.instructions.Label;
import backend.common.address.Immediate;

public class ARMImmediate extends Immediate {

  private final BitNum bitNum;

  public ARMImmediate(int val, BitNum bitNum) {
    super(val);
    this.bitNum = bitNum;
  }

  public ARMImmediate(int val, BitNum bitNum, boolean isChar) {
    super(val, null, isChar, false);
    this.bitNum = bitNum;
  }

  public ARMImmediate(Label label) {
    super(label);
    this.bitNum = null;
  }

  public boolean isChar() {
    return isChar;
  }

  public String getVal() {
    if (label != null) {
      return label.getName();
    } else {
      return Integer.toString(val);
    }
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
