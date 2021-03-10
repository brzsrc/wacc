package backend.arm.instructions.memory;

import backend.arm.instructions.ARMInstruction;
import java.util.List;
import utils.backend.register.Register;

public interface Heap {

  /* used for task3 optimisation */
  int getNextHeapAddr();

  List<ARMInstruction> allocate(Register reg, int size);

  boolean isFull();
}
