package backend.arm.instructions.arithmeticLogic;

import backend.arm.instructions.addressing.Operand2;
import backend.common.address.Address;
import utils.backend.Cond;
import utils.backend.register.Register;

public class Add extends ARMArithmeticLogic {

  /* ADD{cond}{S} <Rd>, <Rn>, <operand2> */
  private final Cond cond;

  public Add(Register rd, Register rn,
      Address operand2) {
    super(rd, rn, operand2);
    cond = Cond.NULL;
  }

  public Add(Register rd, Register rn,
      Address operand2, Cond cond) {
    super(rd, rn, operand2);
    this.cond = cond;
  }

  @Override
  public String assemble() {
    return "ADD" + cond + " " + Rd + ", " + Rn + ", " + operand2;
  }

}
