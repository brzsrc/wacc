package backend.instructions.memory;

import backend.instructions.Instruction;
import java.util.List;
import utils.backend.register.Register;

public class Pop extends Instruction {
  /* used for task3 optimisation */
  private List<Register> reglist;

  public Pop(List<Register> reglist) {
    this.reglist = reglist;
  }

  @Override
  public String assemble() {
    return "POP {" + reglist.stream().map(Register::toString).reduce((i, j) -> i + ", " + j).get() + "}";
  }
}
