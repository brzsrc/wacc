package backend.instructions;

import backend.instructions.Operand.Operand;
import backend.instructions.Operand.Operand2;
import utils.backend.PseudoRegister;

public class Mov extends Instruction {

  private final PseudoRegister Rd;
  private final Operand2 operand2;

  public Mov(PseudoRegister Rd, Operand2 operand2) {
    this.Rd = Rd;
    this.operand2 = operand2;
  }

  @Override
  public String assemble() {
    return "MOV " + Rd + ", " + operand2;
  }
}
