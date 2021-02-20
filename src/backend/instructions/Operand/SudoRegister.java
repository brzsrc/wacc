package backend.instructions.Operand;

public class SudoRegister implements Operand {
  long registerNum;

  private static long currAvailableReg = 0;

  public SudoRegister(long sudoRegNum) {
    this.registerNum = sudoRegNum;
  }

  public static SudoRegister getCurrAvailableReg() {
    return new SudoRegister(currAvailableReg++);
  }

  public static long peakLastReg() {
    if (currAvailableReg <= 0) {
      throw new IllegalArgumentException("calling peakSudoReg before any register is allocated");
    }
    return currAvailableReg - 1;
  }
}
