package utils.backend.register.intel;

import java.util.List;
import utils.Utils.IntelInstructionSize;
import utils.backend.register.RegisterAllocator;

import static utils.backend.register.intel.IntelConcreteRegister.*;

public class IntelConcreteRegisterAllocator extends RegisterAllocator<IntelConcreteRegister> {

  public static final int INTEL_GENERAL_REG_START = 0, INTEL_GENERAL_REG_END = 12;

  public IntelConcreteRegisterAllocator() {
    super(INTEL_GENERAL_REG_START, INTEL_GENERAL_REG_END);
    this.registers = List.of(rbx, rcx, rsi, rdi, r8, r9, r10, r11, r12, r13, r14, r15);
  }

  public IntelConcreteRegister curr(IntelInstructionSize size) {
    IntelConcreteRegister curr = super.curr();
    return curr.withSize(size);
  }

  public IntelConcreteRegister last(IntelInstructionSize size) {
    IntelConcreteRegister last = super.last();
    return last.withSize(size);
  }

  public IntelConcreteRegister next(IntelInstructionSize size) {
    IntelConcreteRegister next = super.next();
    return next.withSize(size);
  }

  public IntelConcreteRegister allocate(IntelInstructionSize size) {
    IntelConcreteRegister allocate = super.allocate();
    return allocate.withSize(size);
  }
}
