package backend.intel.instructions;

import utils.backend.register.Register;
import utils.backend.register.intel.IntelConcreteRegister;

public class Set implements IntelInstruction {

  public enum IntelSetType {
    E, NE, L, G, LE, GE
  }

  private IntelSetType type;
  private Register register;

  public Set(Register register, IntelSetType type) {
    this.type = type;
    this.register = register;
  }

  @Override
  public String assemble() {
    return "set" + type.name().toLowerCase() + " " + register;
  }
}
