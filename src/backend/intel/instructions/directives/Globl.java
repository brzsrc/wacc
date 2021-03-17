package backend.intel.instructions.directives;

import backend.intel.instructions.IntelInstruction;

public class Globl implements IntelInstruction {

  private String entryName;

  public Globl(String entryName) {
    this.entryName = entryName;
  }

  @Override
  public String assemble() {
    return ".globl " + entryName;
  }
}
