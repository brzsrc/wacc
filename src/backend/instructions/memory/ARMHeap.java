package backend.instructions.memory;

import backend.instructions.BL;
import backend.instructions.Instruction;

public class ARMHeap implements Heap {

  /* TODO: find the correct value of this */
  public static final int MAX_ARM_HEAP_SIZE = 65536;
  private int heapAddrCounter;

  public ARMHeap() {
    this.heapAddrCounter = 0;
  }

  @Override
  public int getNextHeapAddr() {
    return heapAddrCounter + 1;
  }

  @Override
  public Instruction allocate() {
    return new BL("malloc");
  }

  @Override
  public boolean isFull() {
    return heapAddrCounter > MAX_ARM_HEAP_SIZE;
  }
}
