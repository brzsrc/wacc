package backend.instructions.memory;

import backend.instructions.Instruction;
import java.util.List;
import utils.backend.register.Register;

public interface Heap {
  /* used for task3 optimisation */
  public int getNextHeapAddr();
  public List<Instruction> allocate(Register reg, int size);
  public boolean isFull();
}
