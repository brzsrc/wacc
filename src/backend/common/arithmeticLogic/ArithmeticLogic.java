package backend.common.arithmeticLogic;

import backend.common.address.Address;
import backend.common.address.Immediate;
import utils.backend.register.Register;

public abstract class ArithmeticLogic {
  protected Register Rd, Rn;
  protected Address operand2;
  protected Immediate immed;

  private ArithmeticLogic(Register rd, Register rn, Address operand2, Immediate immed) {
    Rd = rd;
    Rn = rn;
    this.operand2 = operand2;
    this.immed = immed;
  }

  protected ArithmeticLogic(Register rd, Register rn, Address operand2) {
    this(rd, rn, operand2, null);
  }


  protected ArithmeticLogic(Register rd, Register rn) {
    this(rd, rn, null, null);
  }

  protected ArithmeticLogic(Immediate immed, Register rd) {
    this(rd, null, null, immed);
  }
}
