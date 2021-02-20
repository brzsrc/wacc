package backend.instructions;

import backend.instructions.Operand.Operand;
import backend.instructions.Operand.SudoRegister;

public class Mov extends Instruction {

  private final SudoRegister Rd;
  private final Operand operand;

  public Mov(SudoRegister Rd, Operand operand) {
    this.Rd = Rd;
    this.operand = operand;
  }
}
