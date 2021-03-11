package backend.intel.instructions;

import backend.Instruction;

public interface IntelInstruction extends Instruction {
  public default int getIndentationLevel() { return 1; }
}
