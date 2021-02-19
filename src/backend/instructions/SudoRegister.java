package backend.instructions;

import backend.instructions.Operand2.Operand2;

/* sudo register could be an instance of Operand2, as stated in ARM table 1-14
 * or it could also be included in other operand formats, so not put in Operand/ directory */
public class SudoRegister implements Operand2 {
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
