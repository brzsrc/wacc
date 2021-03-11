package backend.arm.instructions.memory;

import backend.arm.instructions.BL;
import backend.arm.instructions.ARMInstruction;
import backend.arm.instructions.Mov;
import backend.arm.instructions.addressing.ARMImmediate;
import backend.arm.instructions.addressing.ARMImmediate.BitNum;
import backend.arm.instructions.addressing.Operand2;
import java.util.Arrays;
import java.util.List;
import utils.backend.register.Register;

public class ARMHeap implements Heap {

  /* used for task3 optimisation */
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
  public List<ARMInstruction> allocate(Register reg, int size) {
    ARMImmediate sizeImmed = new ARMImmediate(size, BitNum.SHIFT32);
    ARMInstruction mov = new Mov(reg, new Operand2(sizeImmed));
    ARMInstruction malloc = new BL("malloc");
    heapAddrCounter += size * 4;
    return Arrays.asList(mov, malloc);
  }

  @Override
  public boolean isFull() {
    return heapAddrCounter > MAX_ARM_HEAP_SIZE;
  }
}
