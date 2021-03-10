package backend.instructions.addressing;

import backend.instructions.operand.Immediate;
import backend.instructions.operand.Immediate.BitNum;

public class ImmediateAddressing extends Addressing {

  /* used in case like: ldr r0 =0 */
  private final Immediate immed;

  public ImmediateAddressing(int val) {
    this.immed = new Immediate(val, BitNum.CONST16);
  }

  @Override
  public String toString() {
    if (immed.isChar()) {
      return immed.toString();
    }
    return "=" + immed.getVal();
  }
}
