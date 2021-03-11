package backend.arm.instructions;

import backend.arm.instructions.ARMInstruction;
import backend.common.PopInstruction;
import java.util.List;
import utils.backend.register.Register;

public class Pop extends PopInstruction implements ARMInstruction {

  public Pop(List<Register> reglist) {
    super(reglist);
  }

  /* used for task3 optimisation */

  @Override
  public String assemble() {
    return "POP {" + reglist.stream().map(Register::toString).reduce((i, j) -> i + ", " + j).get()
        + "}";
  }
}
