package backend.instructions.memory;

import java.util.HashMap;
import java.util.Map;

public class ARMStack implements Stack {
  /* used for task3 optimisation */
  /* TODO: find the correct value of MAX_ARM_STACK_SIZE */
  public static final int MAX_ARM_STACK_SIZE = 65536;

  /* actually this is the current offset value from the start position */
  private int stackPointer;

  public ARMStack() {
    this.stackPointer = 0;
  }

  @Override
  public int push(String ident) {
    /* TODO: using isFull() to handle stack overflow */
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
