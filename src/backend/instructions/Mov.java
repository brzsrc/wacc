package backend.instructions;

import backend.instructions.Operand2.Operand2;

public class Mov extends Instruction<Operand2, Void> {

  public Mov(SudoRegister Rd, Operand2 operand2) {
    this.Rd = Rd;
    this.fstExpr = operand2;
  }
}
