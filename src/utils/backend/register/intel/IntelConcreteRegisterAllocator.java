package utils.backend.register.intel;

import java.util.List;
import utils.backend.register.RegisterAllocator;

import static utils.backend.register.intel.IntelConcreteRegister.*;

public class IntelConcreteRegisterAllocator extends RegisterAllocator<IntelConcreteRegister> {

  public static final int INTEL_GENERAL_REG_START = 0, INTEL_GENERAL_REG_END = 12;

  public IntelConcreteRegisterAllocator() {
    super(INTEL_GENERAL_REG_START, INTEL_GENERAL_REG_END);
    this.registers = List.of(rax, rbx, rcx, rdx, rsi, rdi, r8, r9, r10, r11, r12, r13, r14, r15);
  }
}
