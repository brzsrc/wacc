package backend.instructions.memory;

import backend.instructions.Instruction;

public class ARMStack implements Stack {

  private int stackPointer;
  /* TODO: find the correct value of MAX_ARM_STACK_SIZE */
  public static final int MAX_ARM_STACK_SIZE = 65536;

  public ARMStack() {
    this.stackPointer = 0;
  }

  @Override
  public Instruction push() {
    return new Push();
  }

  @Override
  public Instruction pop() {
    return new Pop();
  }

  @Override
  public boolean isFull() {
    return stackPointer > MAX_ARM_STACK_SIZE;
  }
}
