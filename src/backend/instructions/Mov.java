package backend.instructions;

import static frontend.node.expr.BinopNode.Binop.EQUAL;
import static frontend.node.expr.BinopNode.Binop.GREATER;
import static frontend.node.expr.BinopNode.Binop.GREATER_EQUAL;
import static frontend.node.expr.BinopNode.Binop.INEQUAL;
import static frontend.node.expr.BinopNode.Binop.LESS;
import static frontend.node.expr.BinopNode.Binop.LESS_EQUAL;

import backend.instructions.operand.Operand2;
import frontend.node.expr.BinopNode;
import java.util.Map;
import utils.backend.register.Register;

public class Mov extends Instruction {

  public enum MovType {
    MOV,
    MOVGT,
    MOVGE,
    MOVLT,
    MOVLE,
    MOVEQ,
    MOVNE
  }


  /* MOV{cond}{S} <Rd>, <operand2> */
  public static final Map<BinopNode.Binop, MovType> binOpMovMap = Map.of(
      GREATER, MovType.MOVGT,
      GREATER_EQUAL, MovType.MOVGE,
      LESS, MovType.MOVLT,
      LESS_EQUAL, MovType.MOVLE,
      EQUAL, MovType.MOVEQ,
      INEQUAL, MovType.MOVNE);

  private final Register Rd;
  private final Operand2 operand2;
  private final MovType type;

  public Mov(Register Rd, Operand2 operand2, MovType type) {
    this.Rd = Rd;
    this.operand2 = operand2;
    this.type = type;
  }

  public Mov(Register Rd, Operand2 operand2) {
    this(Rd, operand2, MovType.MOV);
  }

  @Override
  public String assemble() {
    return type + " " + Rd + ", " + operand2;
  }
}
