package backend.arm.instructions.arithmeticLogic;

import backend.arm.instructions.addressing.Operand2;
import backend.common.address.Address;
import utils.backend.Cond;
import utils.backend.register.Register;

public class Sub extends ARMArithmeticLogic {

  /* SUB{cond}{S} <Rd>, <Rn>, <operand2> */
  private final Cond cond;

  public Sub(Register rd, Register rn, Address operand2) {
    super(rd, rn, operand2);
    this.cond = Cond.NULL;
  }

  public Sub(Register rd, Register rn,
      Address operand2, Cond cond) {
    super(rd, rn, operand2);
    this.cond = cond;
  }

  @Override
  public String assemble() {
    return "SUB" + cond + " " + rd + ", " + rn + ", " + addr;
  }
}
