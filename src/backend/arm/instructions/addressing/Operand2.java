package backend.arm.instructions.addressing;

import backend.arm.instructions.addressing.ARMImmediate;
import backend.arm.instructions.addressing.ARMImmediate.BitNum;
import backend.common.address.Address;
import utils.backend.register.Register;

/* operand specified as Table 1-14 in ARM spec */
public class Operand2 extends Address {

  private final ARMImmediate immed;
  private final Operand2Operator operator;
  private final Register Rm;
  private final Register Rs;

  private Operand2(Register Rm, Register Rs, Operand2Operator operator, ARMImmediate immed) {
    this.immed = immed;
    this.operator = operator;
    this.Rm = Rm;
    this.Rs = Rs;
  }

  public Operand2(Register Rm, Operand2Operator operator, int intVal) {
    this.immed = new ARMImmediate(intVal, BitNum.CONST5);
    this.operator = operator;
    this.Rm = Rm;
    this.Rs = null;
  }

  public Operand2(ARMImmediate immed) {
    this(null, null, Operand2Operator.NONE, immed);
  }

  public Operand2(int intVal) {
    this(null, null, Operand2Operator.NONE, new ARMImmediate(intVal, BitNum.CONST8));
  }

  public Operand2(Register Rm) {
    this(Rm, null, Operand2Operator.NONE, null);
  }

  public Operand2(Register Rm, Operand2Operator operator) {
    this(Rm, null, operator, null);
  }

  public Operand2(Register Rm, Register Rs, Operand2Operator operator) {
    this(Rm, Rs, operator, null);
  }

  @Override
  public String toString() {
    StringBuilder res = new StringBuilder();

    if (Rm != null) {
        res.append(Rm.toString());
    }
    if (Rs != null) {
      res.append(", ").append(operator).append(" ").append(Rs);
    } else {
      if (operator != Operand2Operator.NONE) {
        res.append(", " + operator + " ");
      }
      if (immed != null) {
        res.append(immed.toString());
      }
    }

    return res.toString();
  }

  public enum Operand2Operator {LSL, LSR, ASR, ROR, RRX, NONE}
}
