package backend.intel.instructions;

import backend.Instruction;
import backend.common.LabelInstruction;

public interface IntelInstruction extends Instruction {
  public default int getIndentationLevel() { return 1; }
}
