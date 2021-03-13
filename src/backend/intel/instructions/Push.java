package backend.intel.instructions;

import backend.common.PushInstruction;
import java.util.List;
import utils.Utils;
import utils.backend.register.Register;
import utils.backend.register.intel.IntelConcreteRegister;

public class Push extends PushInstruction implements IntelInstruction {

  public Push(List<Register> reglist) {
    super(reglist);
  }

  @Override
  public String assemble() {
    IntelConcreteRegister reg = reglist.get(0).asIntelRegister();
    return "push" + Utils.calculateSize(reg.getSize()) + " " + reg;
  }
}
