package backend.arm.instructions;

import backend.arm.instructions.addressing.Operand2;
import backend.common.CmpInstruction;
import backend.common.address.Address;
import utils.backend.register.Register;

public class Cmp extends CmpInstruction implements ARMInstruction {

  public Cmp(Register Rd, Operand2 operand2) {
    super(Rd, operand2);
  }

  /* CMP{cond} <Rn>, <operand2> */

  @Override
  public String assemble() {
    return "CMP " + this.rd + ", " + this.operand2;
  }
}
