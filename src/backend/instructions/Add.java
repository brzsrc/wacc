package backend.instructions;

import backend.instructions.Operand.*;
import utils.backend.PseudoRegister;

public class Add extends Instruction {

  private PseudoRegister Rd, Rn;
  private Operand2 operand2;

  public Add(PseudoRegister rd, PseudoRegister rn, Operand2 operand2) {
    Rd = rd;
    Rn = rn;
    this.operand2 = operand2;
  }

  @Override
  public String assemble() {
    return "ADD " + Rd + ", " + Rn + ", " + operand2;
  }

}
