package backend.intel.instructions;

import backend.common.PopInstruction;
import java.util.List;
import utils.backend.register.Register;

public class Pop extends PopInstruction implements IntelInstruction {

  public Pop(List<Register> reglist) {
    super(reglist);
  }

  @Override
  public String assemble() {
    return null;
  }
}
