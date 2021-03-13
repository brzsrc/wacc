package backend.intel.instructions.arithmetic;

import backend.arm.instructions.addressing.Operand2;
import backend.intel.instructions.address.IntelImmediate;
import utils.backend.register.Register;

public class Sub extends IntelArithmeticLogic {

  public Sub(Register rd,
      Register rn) {
    super(rd, rn);
  }

  public Sub(int val, Register rd) {
    super(val, rd);
  }

  @Override
  public String assemble() {
    return assembleArithmeticLogic("sub");
  }
}
