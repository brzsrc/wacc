package backend.intel.instructions.arithmetic;

import backend.common.address.Address;
import backend.common.address.Immediate;
import backend.common.arithmeticLogic.ArithmeticLogic;
import backend.intel.instructions.IntelInstruction;
import utils.backend.register.Register;

public abstract class IntelArithmeticLogic extends ArithmeticLogic implements IntelInstruction {

  protected IntelArithmeticLogic(Register rd, Register rn) {
    super(rd, rn);
  }

  protected IntelArithmeticLogic(Immediate immed, Register rd) {
    super(immed, rd);
  }
}
