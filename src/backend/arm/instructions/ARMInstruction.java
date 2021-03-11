package backend.arm.instructions;

import backend.Instruction;

public interface ARMInstruction extends Instruction {
  public default int getIndentationLevel() {
    return 2;
  }
}
