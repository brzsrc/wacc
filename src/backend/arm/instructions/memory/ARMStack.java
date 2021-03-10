package backend.arm.instructions.memory;

public class ARMStack implements Stack {

  /* used for task3 optimisation */
  public static final int MAX_ARM_STACK_SIZE = 65536;

  private int stackPointer;

  public ARMStack() {
    this.stackPointer = 0;
  }

  @Override
  public int push(String ident) {
    int temp = stackPointer;
    stackPointer += 4;
    return temp;
  }

  @Override
  public int pop(String ident) {
    stackPointer -= 4;
    return stackPointer;
  }

  @Override
  public boolean isFull() {
    return stackPointer > MAX_ARM_STACK_SIZE;
  }

  @Override
  public int lookUp(String ident) {
    // TODO Auto-generated method stub
    return 0;
  }
}
