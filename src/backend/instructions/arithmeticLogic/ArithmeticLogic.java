package backend.instructions.arithmeticLogic;

import backend.instructions.Instruction;
import backend.instructions.operand.Operand2;
import utils.backend.PseudoRegister;

public abstract class ArithmeticLogic extends Instruction {
  protected PseudoRegister Rd, Rn;
  protected Operand2 operand2;
}
