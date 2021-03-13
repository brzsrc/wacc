package backend.arm.instructions.arithmeticLogic;

import backend.arm.instructions.addressing.Operand2;
import backend.common.address.Address;
import utils.backend.register.Register;

public class SMull extends ARMArithmeticLogic {

  /* SMULL{cond}{S} <RdLo>, <RdHi>, <Rm>, <Rs> */
  public SMull(Register rd, Register rn, Address op2) {
    super(rd, rn, op2);
  }

  @Override
  public String assemble() {
    return "SMULL " + rd + ", " + addr + ", " + rd + ", " + addr;
  }
}
