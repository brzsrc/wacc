package utils.backend.register.arm;

import static utils.backend.register.arm.ARMConcreteRegister.MAX_ARM_REGISTER;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import utils.backend.register.RegisterAllocator;

public class ARMConcreteRegisterAllocator extends RegisterAllocator<ARMConcreteRegister> {

  public static final int ARM_GENERAL_REG_START = 4, ARM_GENERAL_REG_END = 12;

  public ARMConcreteRegisterAllocator() {
    super(ARM_GENERAL_REG_START, ARM_GENERAL_REG_END);
    this.registers = Arrays.asList(ARMRegisterLabel.values()).stream().map(ARMConcreteRegister::new).collect(
        Collectors.toList());
  }
}
