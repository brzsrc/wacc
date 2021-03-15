package backend.intel.instructions.arithmetic;

import backend.arm.instructions.addressing.Operand2;
import utils.Utils.IntelInstructionSize;
import utils.backend.register.Register;

public class Xor extends IntelArithmeticLogic {

  protected Xor(Register rn,
      Register rd) {
    super(rn, rd);
  }

  protected Xor(int val, IntelInstructionSize size, Register r) {
    super(val, size, r);
  }

  @Override
  public String assemble() {
    return assembleArithmeticLogic("xor");
  }
}
