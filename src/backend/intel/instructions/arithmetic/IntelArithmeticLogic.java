package backend.intel.instructions.arithmetic;

import static utils.backend.register.intel.IntelConcreteRegister.rax;

import backend.Instruction;
import backend.common.arithmeticLogic.UnopAssemble;
import backend.common.address.Immediate;
import backend.common.arithmeticLogic.ArithmeticLogic;
import backend.common.arithmeticLogic.BinopAssemble;
import backend.intel.instructions.Cltd;
import backend.intel.instructions.Cmp;
import backend.intel.instructions.IntelInstruction;
import backend.intel.instructions.Mov;
import backend.intel.instructions.Neg;
import backend.intel.instructions.Not;
import backend.intel.instructions.Set;
import backend.intel.instructions.Set.IntelSetType;
import backend.intel.instructions.address.IntelAddress;
import frontend.node.expr.ArrayElemNode;
import frontend.node.expr.ArrayNode;
import frontend.node.expr.BinopNode.Binop;
import frontend.node.expr.IdentNode;
import frontend.node.expr.UnopNode.Unop;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import utils.Utils;
import utils.Utils.IntelInstructionSize;
import utils.backend.register.Register;

public abstract class IntelArithmeticLogic extends ArithmeticLogic implements IntelInstruction {

  public static final BinopAssemble intelBinopAsm = (rd, rn, notUsed, b) -> {
    Map<Binop, List<Instruction>> m = new HashMap<>(Map.of(
        Binop.PLUS, List.of(new Add(rn, rd)),
        Binop.MINUS, List.of(new Sub(rn, rd)),
        Binop.MUL, List.of(new Mul(rn, rd)),
        Binop.AND, List.of(new And(rn, rd)),
        Binop.OR, List.of(new Or(rn, rd)),
        Binop.DIV, List.of(new Mov(rd, rax.withSize(rd.asIntelRegister().getSize())), new Cltd(), new Div(rn)),
        Binop.MOD, List.of(new Mov(rd, rax.withSize(rd.asIntelRegister().getSize())), new Cltd(), new Div(rn)),
        Binop.EQUAL, List.of(new Cmp(rn.asIntelRegister(), rd.asIntelRegister()), new Set(rd.asIntelRegister()
            .withSize(IntelInstructionSize.B), IntelSetType.E)),
        Binop.INEQUAL, List.of(new Cmp(rn.asIntelRegister(), rd.asIntelRegister()), new Set(rd.asIntelRegister()
            .withSize(IntelInstructionSize.B), IntelSetType.NE)),
        Binop.GREATER, List.of(new Cmp(rn.asIntelRegister(), rd.asIntelRegister()), new Set(rd.asIntelRegister()
            .withSize(IntelInstructionSize.B), IntelSetType.G))
    ));

    m.put(Binop.LESS, List.of(new Cmp(rn.asIntelRegister(), rd.asIntelRegister()), new Set(rd.asIntelRegister()
        .withSize(IntelInstructionSize.B), IntelSetType.L)));
    m.put(Binop.GREATER_EQUAL, List.of(new Cmp(rn.asIntelRegister(), rd.asIntelRegister()), new Set(rd.asIntelRegister()
        .withSize(IntelInstructionSize.B), IntelSetType.GE)));
    m.put(Binop.LESS_EQUAL, List.of(new Cmp(rn.asIntelRegister(), rd.asIntelRegister()), new Set(rd.asIntelRegister()
        .withSize(IntelInstructionSize.B), IntelSetType.LE)));

    return m.get(b);
  };

  public static final UnopAssemble intelUnopAsm = (rd, rn, op, expr) -> {
    int len = 0;

    if (op.equals(Unop.LEN)) {
      if (expr instanceof IdentNode) {
        IdentNode id = (IdentNode) expr;
        len = ((ArrayNode) (id.getSymbol().getExprNode())).getLength();
      } else {
        ArrayElemNode elem = (ArrayElemNode) expr;
        len = ((ArrayNode) elem.getArray()).getLength();
      }
    }

    Map<Unop, List<Instruction>> m = Map.of(
        Unop.LEN, List.of(new Mov(new IntelAddress(len), rd)),
        Unop.CHR, List.of(),
        Unop.MINUS, List.of(new Neg(rd)),
        Unop.NOT, List.of(new Xor(1, IntelInstructionSize.Q, rd)),
        Unop.ORD, List.of()
    );

    return m.get(op);
  };

  protected IntelArithmeticLogic(Register rn, Register rd) {
    super(rd, rn);
  }

  protected IntelArithmeticLogic(int val, IntelInstructionSize size, Register rd) {
    super(val, size, rd);
  }

  protected String assembleArithmeticLogic(String s) {
    StringBuilder str = new StringBuilder();

    String size = "";

    if (immed != null) {
      size = Utils.calculateSize(immed.asIntelImmediate().getSize());
    } else {
      size = Utils.calculateSize(rn.asIntelRegister().getSize());
    }

    str.append(s).append(size).append(" ").append(immed == null ? rn : immed.assemble()).append(", ").append(rd);
    return str.toString();
  }
}
