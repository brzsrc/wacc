package utils.backend.register;

import static utils.backend.register.ARMConcreteRegister.MAX_ARM_REGISTER;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ARMConcreteRegisterAllocator {

  public static final int GENERAL_REG_START = 4, GENERAL_REG_END = 12;

  private final List<ARMConcreteRegister> registers;
  private int registerCounter;

  public ARMConcreteRegisterAllocator() {
    registers = new ArrayList<>();
    Arrays.asList(ARMRegisterLabel.values())
        .forEach(label -> registers.add(new ARMConcreteRegister(label)));
    registerCounter = GENERAL_REG_START;
  }

  public ARMConcreteRegister curr() {
    return registers
        .get(registerCounter > GENERAL_REG_START ? registerCounter - 1 : registerCounter);
  }

  public ARMConcreteRegister last() {
    return registers
        .get(registerCounter > GENERAL_REG_START ? registerCounter - 2 : registerCounter);
  }

  public ARMConcreteRegister next() {
    return isFull() ? null : registers.get(registerCounter);
  }

  public ARMConcreteRegister allocate() {
    if (registerCounter < GENERAL_REG_START || registerCounter > GENERAL_REG_END) {
      throw new IllegalArgumentException("cannot allocate register number: " + registerCounter);
    }
    return registers.get(registerCounter++);
  }

  public ARMConcreteRegister free() {
    return registers.get(--registerCounter);
  }

  private boolean isFull() {
    return registerCounter > MAX_ARM_REGISTER;
  }

}
