package backend.intel.instructions;

import backend.common.PopInstruction;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import utils.backend.register.Register;
import utils.backend.register.intel.IntelConcreteRegister;

public class Pop extends PopInstruction implements IntelInstruction {

  public Pop(IntelConcreteRegister reg) {
    super(Collections.singletonList(reg));
  }

  @Override
  public String assemble() {
    return null;
  }
}
