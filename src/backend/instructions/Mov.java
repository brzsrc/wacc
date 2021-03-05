package backend.instructions;

import java.util.Map;

import backend.instructions.arithmeticLogic.MovType;
import backend.instructions.operand.Operand2;
import frontend.node.expr.BinopNode;
import utils.backend.register.Register;

import static backend.instructions.arithmeticLogic.MovType.*;
import static frontend.node.expr.BinopNode.Binop.*;

public class Mov extends Instruction {
  /* MOV{cond}{S} <Rd>, <operand2> */
  public static final Map<BinopNode.Binop, MovType> binOpMovMap = Map.of(
          GREATER, MOVGT,
          GREATER_EQUAL, MOVGE,
          LESS, MOVLT,
          LESS_EQUAL, MOVLE,
          EQUAL, MOVEQ,
          INEQUAL, MOVNE);

  private final Register Rd;
  private final Operand2 operand2;
  private final MovType type;

  public Mov(Register Rd, Operand2 operand2, MovType type) {
    this.Rd = Rd;
    this.operand2 = operand2;
    this.type = type;
  }

  public Mov(Register Rd, Operand2 operand2) {
    this(Rd, operand2, MOV);
  }

  @Override
  public String assemble() {
    return type + " " + Rd + ", " + operand2;
  }
}
