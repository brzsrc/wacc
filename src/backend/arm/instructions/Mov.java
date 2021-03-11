package backend.arm.instructions;

import static frontend.node.expr.BinopNode.Binop.EQUAL;
import static frontend.node.expr.BinopNode.Binop.GREATER;
import static frontend.node.expr.BinopNode.Binop.GREATER_EQUAL;
import static frontend.node.expr.BinopNode.Binop.INEQUAL;
import static frontend.node.expr.BinopNode.Binop.LESS;
import static frontend.node.expr.BinopNode.Binop.LESS_EQUAL;

import backend.arm.instructions.addressing.Operand2;
import backend.common.MovInstruction;
import frontend.node.expr.BinopNode;
import java.util.Map;
import utils.backend.register.Register;

public class Mov extends MovInstruction implements ARMInstruction {

  public enum ARMMovType {
    MOV,
    MOVGT,
    MOVGE,
    MOVLT,
    MOVLE,
    MOVEQ,
    MOVNE
  }

  /* MOV{cond}{S} <Rd>, <operand2> */
  public static final Map<BinopNode.Binop, ARMMovType> binOpMovMap = Map.of(
      GREATER, ARMMovType.MOVGT,
      GREATER_EQUAL, ARMMovType.MOVGE,
      LESS, ARMMovType.MOVLT,
      LESS_EQUAL, ARMMovType.MOVLE,
      EQUAL, ARMMovType.MOVEQ,
      INEQUAL, ARMMovType.MOVNE);

  private final ARMMovType type;

  public Mov(Register Rd, Operand2 operand2, ARMMovType type) {
    super(Rd, operand2);
    this.type = type;
  }

  public Mov(Register Rd, Operand2 operand2) {
    this(Rd, operand2, ARMMovType.MOV);
  }

  @Override
  public String assemble() {
    return type + " " + Rd + ", " + operand2;
  }
}
