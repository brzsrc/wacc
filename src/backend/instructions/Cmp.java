package backend.instructions;

import backend.instructions.operand.Operand2;
import utils.backend.Register;

public class Cmp extends Instruction {

  private Register Rn;
  private Operand2 operand2;

  public Cmp(Register rn, Operand2 operand2) {
    Rn = rn;
    this.operand2 = operand2;
  }

  @Override
  public String assemble() {
    return "CMP " + Rn + ", " + operand2;
  }
}
