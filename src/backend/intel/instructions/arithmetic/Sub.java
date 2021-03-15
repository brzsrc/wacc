package backend.intel.instructions.arithmetic;

import backend.arm.instructions.addressing.Operand2;
import backend.intel.instructions.address.IntelImmediate;
import utils.Utils.IntelInstructionSize;
import utils.backend.register.Register;

public class Sub extends IntelArithmeticLogic {

  public Sub(Register rd,
      Register rn) {
    super(rd, rn);
  }

  public Sub(int val, IntelInstructionSize size, Register rd) {
    super(val, size, rd);
  }

  @Override
  public String assemble() {
    return assembleArithmeticLogic("sub");
  }
}
