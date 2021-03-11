package backend.arm.instructions.arithmeticLogic;

import backend.arm.instructions.BL;
import backend.arm.instructions.Cmp;
import backend.arm.instructions.ARMInstruction;
import backend.arm.instructions.LDR;
import backend.arm.instructions.Mov;
import backend.arm.instructions.Mov.ARMMovType;
import backend.arm.instructions.addressing.AddressingMode2;
import backend.arm.instructions.addressing.AddressingMode2.AddrMode2;
import backend.arm.instructions.addressing.ARMImmediate;
import backend.arm.instructions.addressing.ARMImmediate.BitNum;
import backend.arm.instructions.addressing.Operand2;
import backend.common.arithmeticLogic.ArithmeticLogic;
import frontend.node.expr.BinopNode.Binop;
import frontend.node.expr.UnopNode.Unop;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import utils.backend.Cond;
import utils.backend.register.arm.ARMConcreteRegister;
import utils.backend.register.arm.ARMRegisterLabel;
import utils.backend.register.Register;

public abstract class ARMArithmeticLogic extends ArithmeticLogic implements ARMInstruction {

  /* Instruction sets for binop and unop operations */

  public static final BinopAssemble BasicBinopAsm = (rd, rn, op2, b) -> {
    Map<Binop, ARMInstruction> m = Map.of(
        Binop.PLUS, new Add(rd, rn, op2, Cond.S),
        Binop.MINUS, new Sub(rd, rn, op2, Cond.S),
        Binop.MUL, new SMull(rd, rn, op2),
        Binop.AND, new And(rd, rn, op2),
        Binop.OR, new Or(rd, rn, op2)
    );
    return List.of(m.get(b));
  };

  public static final BinopAssemble DivModAsm = (rd, rn, op2, b) -> {
    /* op2 will not be used here */
    List<ARMInstruction> list = new ArrayList<>();

    Register r0 = new ARMConcreteRegister(ARMRegisterLabel.R0);
    Register r1 = new ARMConcreteRegister(ARMRegisterLabel.R1);
    Operand2 dividend = new Operand2(rd);

    list.add(new Mov(r0, dividend));
    list.add(new Mov(r1, op2));
    list.add(new BL("p_check_divide_by_zero"));
    if (b.equals(Binop.DIV)) {
      list.add(new BL("__aeabi_idiv"));
      list.add(new Mov(rd, new Operand2(r0)));
    } else if (b.equals(Binop.MOD)) {
      list.add(new BL("__aeabi_idivmod"));
      /* we only put the remainder in the register and discard the quotient */
      list.add(new Mov(rd, new Operand2(r1)));
    }

    return list;
  };

  public static final BinopAssemble CmpAsm = (rd, rn, op2, b) -> {
    List<ARMInstruction> list = new ArrayList<>();

    Operand2 one = new Operand2(new ARMImmediate(1, BitNum.CONST8));
    Operand2 zero = new Operand2(new ARMImmediate(0, BitNum.CONST8));

    list.add(new Cmp(rd, op2));
    /* default as false, set as true in following check */
    list.add(new Mov(rd, zero, ARMMovType.MOV));
    list.add(new Mov(rd, one, Mov.binOpMovMap.get(b)));

    return list;
  };

  public static final Map<Binop, BinopAssemble> binopInstruction = Map.ofEntries(
      new AbstractMap.SimpleEntry<Binop, BinopAssemble>(Binop.PLUS, BasicBinopAsm),
      new AbstractMap.SimpleEntry<Binop, BinopAssemble>(Binop.MINUS, BasicBinopAsm),
      new AbstractMap.SimpleEntry<Binop, BinopAssemble>(Binop.MUL, BasicBinopAsm),
      new AbstractMap.SimpleEntry<Binop, BinopAssemble>(Binop.AND, BasicBinopAsm),
      new AbstractMap.SimpleEntry<Binop, BinopAssemble>(Binop.OR, BasicBinopAsm),
      new AbstractMap.SimpleEntry<Binop, BinopAssemble>(Binop.DIV, DivModAsm),
      new AbstractMap.SimpleEntry<Binop, BinopAssemble>(Binop.MOD, DivModAsm),
      new AbstractMap.SimpleEntry<Binop, BinopAssemble>(Binop.GREATER, CmpAsm),
      new AbstractMap.SimpleEntry<Binop, BinopAssemble>(Binop.GREATER_EQUAL, CmpAsm),
      new AbstractMap.SimpleEntry<Binop, BinopAssemble>(Binop.LESS, CmpAsm),
      new AbstractMap.SimpleEntry<Binop, BinopAssemble>(Binop.LESS_EQUAL, CmpAsm),
      new AbstractMap.SimpleEntry<Binop, BinopAssemble>(Binop.EQUAL, CmpAsm),
      new AbstractMap.SimpleEntry<Binop, BinopAssemble>(Binop.INEQUAL, CmpAsm)
  );

  public static final UnopAssemble ArrayLenAsm = (rd, rn) -> {
    List<ARMInstruction> list = new ArrayList<>();
    list.add(new LDR(rd, new AddressingMode2(AddrMode2.OFFSET, rd)));
    return list;
  };

  public static UnopAssemble NegationAsm = (rd, rn) -> {
    List<ARMInstruction> list = new ArrayList<>();
    list.add(new Rsb(rd, rn, new Operand2(new ARMImmediate(0, BitNum.CONST8))));
    return list;
  };

  public static UnopAssemble LogicNotAsm = (rd, rn) -> {
    List<ARMInstruction> list = new ArrayList<>();
    list.add(new Xor(rd, rn, new Operand2(new ARMImmediate(1, BitNum.CONST8))));
    return list;
  };

  public static UnopAssemble OrdAsm = (rd, rn) -> {
    List<ARMInstruction> list = new ArrayList<>();
    return list;
  };

  public static UnopAssemble ChrAsm = (rd, rn) -> {
    List<ARMInstruction> list = new ArrayList<>();
    return list;
  };

  public static final Map<Unop, UnopAssemble> unopInstruction = Map.ofEntries(
      new AbstractMap.SimpleEntry<Unop, UnopAssemble>(Unop.LEN, ArrayLenAsm),
      new AbstractMap.SimpleEntry<Unop, UnopAssemble>(Unop.MINUS, NegationAsm),
      new AbstractMap.SimpleEntry<Unop, UnopAssemble>(Unop.NOT, LogicNotAsm),
      new AbstractMap.SimpleEntry<Unop, UnopAssemble>(Unop.ORD, OrdAsm),
      new AbstractMap.SimpleEntry<Unop, UnopAssemble>(Unop.CHR, ChrAsm)
  );

  protected ARMArithmeticLogic(Register rd, Register rn, Operand2 operand2) {
    super(rd, rn, operand2);
  }
}
