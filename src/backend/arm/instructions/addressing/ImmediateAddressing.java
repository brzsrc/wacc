package backend.arm.instructions.addressing;

import backend.arm.instructions.addressing.ARMImmediate.BitNum;
import backend.common.address.Address;

public class ImmediateAddressing extends Address {

  /* used in case like: ldr r0 =0 */
  private final ARMImmediate immed;

  public ImmediateAddressing(int val) {
    this.immed = new ARMImmediate(val, BitNum.CONST16);
  }

  @Override
  public String toString() {
    if (immed.isChar()) {
      return immed.toString();
    }
    return "=" + immed.getVal();
  }
}
