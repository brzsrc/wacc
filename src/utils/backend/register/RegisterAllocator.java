package utils.backend.register;

import static utils.backend.register.arm.ARMConcreteRegister.MAX_ARM_REGISTER;

import java.util.List;
import utils.backend.register.arm.ARMConcreteRegister;

public abstract class RegisterAllocator<T extends Register> {

  private final int generalRegStart;
  private final int generalRegEnd;
  private int registerCounter;
  protected List<T> registers;

  public RegisterAllocator(int generalRegStart, int generalRegEnd) {
    this.generalRegStart = generalRegStart;
    this.generalRegEnd = generalRegEnd;
    this.registerCounter = generalRegStart;
  }

  public T curr() {
    return registers
        .get(registerCounter > generalRegStart ? registerCounter - 1 : registerCounter);
  }

  public T last() {
    return registers
        .get(registerCounter > generalRegStart ? registerCounter - 2 : registerCounter);
  }

  public T next() {
    return isFull() ? null : registers.get(registerCounter);
  }

  public T allocate() {
    if (registerCounter < generalRegStart || registerCounter > generalRegEnd) {
      throw new IllegalArgumentException("cannot allocate register number: " + registerCounter);
    }
    return registers.get(registerCounter++);
  }

  public T free() {
    return registers.get(--registerCounter);
  }

  private boolean isFull() {
    return registerCounter > MAX_ARM_REGISTER;
  }

}
