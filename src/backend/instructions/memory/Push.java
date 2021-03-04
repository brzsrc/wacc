package backend.instructions.memory;

import backend.instructions.Instruction;
import java.util.List;
import utils.backend.Register;

public class Push extends Instruction {
  /* used for task3 optimisation */
  private List<Register> reglist;

  public Push(List<Register> reglist) {
    this.reglist = reglist;
  }

  @Override
  public String assemble() {
    return "PUSH {" + reglist.stream().map(Register::toString).reduce((i, j) -> i + ", " + j).get() + "}";
  }
}
