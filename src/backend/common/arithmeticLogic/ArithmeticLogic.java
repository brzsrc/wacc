package backend.common.arithmeticLogic;

import backend.common.address.Address;
import utils.backend.register.Register;

public abstract class ArithmeticLogic {
  protected Register Rd, Rn;
  protected Address operand2;

  protected ArithmeticLogic(Register rd, Register rn, Address operand2) {
    Rd = rd;
    Rn = rn;
    this.operand2 = operand2;
  }
}
