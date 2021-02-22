package backend.instructions.arithmeticLogic;

import backend.instructions.Instruction;
import backend.instructions.operand.Operand2;
import utils.backend.PseudoRegister;

public abstract class ArithmeticLogic extends Instruction {
  protected PseudoRegister Rd, Rn;
  protected Operand2 operand2;

  public ArithmeticLogic(PseudoRegister rd, PseudoRegister rn, Operand2 operand2) {
    Rd = rd;
    Rn = rn;
    this.operand2 = operand2;
  }
}
