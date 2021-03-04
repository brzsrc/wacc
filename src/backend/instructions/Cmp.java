package backend.instructions;

import backend.instructions.operand.Operand2;
import utils.backend.Register;

public class Cmp extends Instruction {
  /* CMP{cond} <Rn>, <operand2> */
  private final Register Rd;
  private final Operand2 operand2;

  public Cmp(Register Rd, Operand2 operand2) {
    this.Rd = Rd;
    this.operand2 = operand2;
  }

  @Override
  public String assemble() {
    return "CMP " + Rd + ", " + operand2;
  }
}
