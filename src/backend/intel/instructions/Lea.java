package backend.intel.instructions;

import backend.intel.instructions.address.IntelAddress;
import utils.Utils;
import utils.backend.register.Register;
import utils.backend.register.intel.IntelConcreteRegister;

public class Lea implements IntelInstruction {

  private final IntelConcreteRegister rd;
  private final IntelAddress addr;

  public Lea(IntelAddress addr, IntelConcreteRegister rd) {
    this.rd = rd;
    this.addr = addr;
  }

  @Override
  public String assemble() {
    return "lea" + rd.getSize().name().toLowerCase() + " " + addr.toString() + ", " + rd;
  }
}
