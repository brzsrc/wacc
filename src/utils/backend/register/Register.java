package utils.backend.register;

import utils.backend.register.arm.ARMConcreteRegister;
import utils.backend.register.intel.IntelConcreteRegister;

/* the abstract class representing any types of register. The generic <T> here represents the labeling system of the registers */
public abstract class Register {

  public IntelConcreteRegister asIntelRegister() {
    return (IntelConcreteRegister) this;
  }

  public ARMConcreteRegister asArmRegister() {
    return (ARMConcreteRegister) this;
  }

  @Override
  public String toString() {
    return "";
  }
}
