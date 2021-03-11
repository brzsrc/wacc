package backend.common;

import backend.Instruction;
import java.util.List;
import utils.backend.register.Register;

public abstract class PopInstruction implements Instruction {
  protected final List<Register> reglist;

  public PopInstruction(List<Register> reglist) {
    this.reglist = reglist;
  }
}
