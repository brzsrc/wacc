package backend.intel.instructions;

import backend.common.PushInstruction;
import java.util.List;
import utils.backend.register.Register;

public class Push extends PushInstruction implements IntelInstruction {

  public Push(List<Register> reglist) {
    super(reglist);
  }

  @Override
  public String assemble() {
    return null;
  }
}
