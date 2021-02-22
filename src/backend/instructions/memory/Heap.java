package backend.instructions.memory;

import backend.instructions.Instruction;

public interface Heap {
  public int getNextHeapAddr();
  public Instruction allocate();
  public boolean isFull();
}
