package backend.arm.instructions;

import backend.arm.instructions.ARMInstruction;
import backend.common.PushInstruction;
import java.util.List;
import utils.backend.register.Register;

public class Push extends PushInstruction implements ARMInstruction {

  public Push(List<Register> reglist) {
    super(reglist);
  }

  @Override
  public String assemble() {
    return "PUSH {" + reglist.stream().map(Register::toString).reduce((i, j) -> i + ", " + j).get()
        + "}";
  }
}
