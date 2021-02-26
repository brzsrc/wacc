package backend.instructions.operand;

public class Immediate {

  public enum BitNum {SHIFT10, CONST8, SHIFT32}

  private int val;
  private BitNum bitNum;

  public Immediate(int val, BitNum bitNum) {
    this.val = val;
    this.bitNum = bitNum;
  }

  public int getVal() {
    return val;
  }

  public BitNum getBitNum() {
    return bitNum;
  }

  @Override
  public String toString() {
    return "#" + val;
  }
}
