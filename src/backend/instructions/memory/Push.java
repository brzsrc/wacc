package backend.instructions.memory;

import backend.instructions.Instruction;
import java.util.List;
import utils.backend.Register;

public class Push extends Instruction {

  private List<Register> reglist;

  public Push(List<Register> reglist) {
    this.reglist = reglist;
  }

  @Override
  public String assemble() {
    return "PUSH {" + reglist.stream().map(Register::toString).reduce((i, j) -> i + ", " + j).get() + "}";
  }
}
