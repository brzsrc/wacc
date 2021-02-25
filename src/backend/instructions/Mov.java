package backend.instructions;

import java.util.Map;

import backend.instructions.operand.Operand2;
import utils.backend.Register;

public class Mov extends Instruction {

  public enum MovType {
    NORMAL, GT, GE, LT, LE, EQ, NE
  }

  public static final Map<MovType, String> movTypeMap = Map.of(
    MovType.NORMAL, "MOV", MovType.GT, "MOVGT", MovType.GE, "MOVGE",
    MovType.LT, "MOVLT", MovType.LE, "MOVLE", MovType.EQ, "MOVEQ", MovType.NE, "MOVNE");

  private final Register Rd;
  private final Operand2 operand2;
  private final MovType type;

  public Mov(Register Rd, Operand2 operand2, MovType type) {
    this.Rd = Rd;
    this.operand2 = operand2;
    this.type = type;
  }

  public Mov(Register Rd, Operand2 operand2) {
    this(Rd, operand2, MovType.NORMAL);
  }

  @Override
  public String assemble() {
    return movTypeMap.get(type) + " " + Rd + ", " + operand2;
  }
}
