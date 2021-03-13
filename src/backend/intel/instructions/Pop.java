package backend.intel.instructions;

import backend.common.PopInstruction;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import utils.Utils;
import utils.backend.register.Register;
import utils.backend.register.intel.IntelConcreteRegister;

public class Pop extends PopInstruction implements IntelInstruction {

  public Pop(IntelConcreteRegister reg) {
    super(Collections.singletonList(reg));
  }

  @Override
  public String assemble() {
    IntelConcreteRegister reg = reglist.get(0).asIntelRegister();
    return "pop" + Utils.calculateSize(reg.getSize()) + " " + reg;
  }
}
