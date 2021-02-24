package backend.instructions.memory;

import java.util.HashMap;
import java.util.Map;

public class ARMStack implements Stack {

  /* TODO: find the correct value of MAX_ARM_STACK_SIZE */
  public static final int MAX_ARM_STACK_SIZE = 65536;

  /* actually this is the current offset value from the start position */
  private int stackPointer;
  /* store the mapping between identifier and their position on the stack */
  private Map<String, Integer> identStackMap; 

  public ARMStack() {
    this.stackPointer = 0;
    this.identStackMap = new HashMap<>();
  }

  @Override
  public int push(String ident) {
    /* TODO: using isFull() to handle stack overflow */
    identStackMap.put(ident, stackPointer);
    int temp = stackPointer;
    stackPointer += 4;
    return temp;
  }

  @Override
  public int pop(String ident) {
    identStackMap.remove(ident);
    stackPointer -= 4;
    return stackPointer;
  }

  @Override
  public int lookUp(String ident) {
    return identStackMap.get(ident);
  }

  @Override
  public boolean isFull() {
    return stackPointer > MAX_ARM_STACK_SIZE;
  }
}
