package backend.common;

import backend.Instruction;
import java.util.List;
import utils.backend.register.Register;

public abstract class PushInstruction implements Instruction {
  protected final List<Register> reglist;

  public PushInstruction(List<Register> reglist) {
    this.reglist = reglist;
  }
}
