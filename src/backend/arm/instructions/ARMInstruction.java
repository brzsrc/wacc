package backend.arm.instructions;

import backend.Instruction;

public abstract class ARMInstruction extends Instruction {
  public int getIndentationLevel() {
    return 2;
  }
}
