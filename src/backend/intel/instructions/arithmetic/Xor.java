package backend.intel.instructions.arithmetic;

import backend.arm.instructions.addressing.Operand2;
import utils.backend.register.Register;

public class Xor extends IntelArithmeticLogic {

  protected Xor(Register rd,
      Register rn) {
    super(rd, rn);
  }

  @Override
  public String assemble() {
    return assembleArithmeticLogic("xor");
  }
}
