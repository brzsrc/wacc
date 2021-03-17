package backend.intel.instructions.directives;

import backend.intel.instructions.IntelInstruction;

public class CFIEndProc implements IntelInstruction {

  @Override
  public String assemble() {
    return ".cfi_endproc";
  }
}
