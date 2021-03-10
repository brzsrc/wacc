package backend.arm.instructions.memory;

import backend.arm.instructions.ARMInstruction;
import java.util.List;
import utils.backend.register.Register;

public class Pop extends ARMInstruction {

  /* used for task3 optimisation */
  private final List<Register> reglist;

  public Pop(List<Register> reglist) {
    this.reglist = reglist;
  }

  @Override
  public String assemble() {
    return "POP {" + reglist.stream().map(Register::toString).reduce((i, j) -> i + ", " + j).get()
        + "}";
  }
}
