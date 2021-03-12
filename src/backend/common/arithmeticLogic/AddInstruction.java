package backend.common.arithmeticLogic;

import backend.Instruction;
import backend.arm.instructions.addressing.Operand2;
import utils.backend.register.Register;

public abstract class AddInstruction implements Instruction {

  protected AddInstruction(Register rd,
      Register rn, Operand2 operand2) {

  }
}
