package backend.intel.instructions;

import backend.Instruction;

public abstract class IntelInstruction extends Instruction {

  public enum IntelInstructionSize {
    B, W, L, Q
  }

  private IntelInstructionSize size;

  public int getIndentationLevel() { return 1; }
}
