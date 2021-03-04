package backend.instructions.memory;

import backend.instructions.BL;
import backend.instructions.Instruction;
import backend.instructions.Mov;
import backend.instructions.operand.Immediate;
import backend.instructions.operand.Immediate.BitNum;
import backend.instructions.operand.Operand2;
import java.util.Arrays;
import java.util.List;
import utils.backend.Register;

public class ARMHeap implements Heap {
  /* used for task3 optimisation */
  /* TODO: find the correct value of this */
  public static final int MAX_ARM_HEAP_SIZE = 65536;
  private int heapAddrCounter;
  /* TODO: maybe we can maintain a list of free blocks here */

  public ARMHeap() {
    this.heapAddrCounter = 0;
  }

  @Override
  public int getNextHeapAddr() {
    return heapAddrCounter + 1;
  }

  @Override
  public List<Instruction> allocate(Register reg, int size) {
    Immediate sizeImmed = new Immediate(size, BitNum.SHIFT32);
    Instruction mov = new Mov(reg, new Operand2(sizeImmed));
    Instruction malloc = new BL("malloc");
    heapAddrCounter += size * 4;
    return Arrays.asList(mov, malloc);
  }

  @Override
  public boolean isFull() {
    return heapAddrCounter > MAX_ARM_HEAP_SIZE;
  }
}
