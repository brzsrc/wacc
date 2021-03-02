package backend.instructions.arithmeticLogic;

import backend.instructions.LDR;
import backend.instructions.addressing.addressingMode2.AddressingMode2;
import backend.instructions.addressing.addressingMode2.AddressingMode2.AddrMode2;
import frontend.node.expr.UnopNode.Unop;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import backend.instructions.BL;
import backend.instructions.Cmp;
import backend.instructions.Instruction;
import backend.instructions.Mov;
import backend.instructions.operand.Immediate;
import backend.instructions.operand.Operand2;
import backend.instructions.operand.Immediate.BitNum;
import frontend.node.expr.BinopNode.Binop;
import utils.backend.ARMConcreteRegister;
import utils.backend.ARMRegisterLabel;
import utils.backend.Cond;
import utils.backend.Register;

public abstract class ArithmeticLogic extends Instruction {

  public static final ArithmeticLogicAssemble BasicBinopAsm = (rd, rn, op2, b) -> {
    Map<Binop, Instruction> m = Map.of(
      Binop.PLUS, new Add(rd, rn, op2, Cond.S),
      Binop.MINUS, new Sub(rd, rn, op2, Cond.S),
      Binop.MUL, new SMull(rd, rn, op2),
      Binop.AND, new And(rd, rn, op2),
      Binop.OR, new Or(rd, rn, op2)
    );
    List<Instruction> list = new ArrayList<>(List.of(m.get(b)));;
    if (b == Binop.MUL) {
      list.add(new Cmp(rn, new Operand2(rd, Operand2.Operand2Operator.ASR, new Immediate(31, BitNum.CONST8))));
    }
    return list;
  };

  public static final ArithmeticLogicAssemble DivModAsm = (rd, rn, op2, b) -> {
    /* op2 will not be used here */
    List<Instruction> list = new ArrayList<>();

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

  public static final ArithmeticLogicAssemble CmpAsm = (rd, rn, op2, b) -> {
    List<Instruction> list = new ArrayList<>();

    Operand2 one = new Operand2(new Immediate(1, BitNum.CONST8));
    Operand2 zero = new Operand2(new Immediate(0, BitNum.CONST8));
    
    list.add(new Cmp(rd, op2));
    /* default as false, set as true in following check */
    list.add(new Mov(rd, zero, MovType.MOV));
    list.add(new Mov(rd, one, Mov.binOpMovMap.get(b)));

    /* this compare is for checking the result is true or false, 
     * true or false value is set by previous 3 operations */
    list.add(new Cmp(rd, op2));

    return list;
  };

  public static final Map<Binop, ArithmeticLogicAssemble> binopInstruction = Map.ofEntries(
    new AbstractMap.SimpleEntry<Binop, ArithmeticLogicAssemble>(Binop.PLUS, BasicBinopAsm),
    new AbstractMap.SimpleEntry<Binop, ArithmeticLogicAssemble>(Binop.MINUS, BasicBinopAsm),
    new AbstractMap.SimpleEntry<Binop, ArithmeticLogicAssemble>(Binop.MUL, BasicBinopAsm),
    new AbstractMap.SimpleEntry<Binop, ArithmeticLogicAssemble>(Binop.AND, BasicBinopAsm), 
    new AbstractMap.SimpleEntry<Binop, ArithmeticLogicAssemble>(Binop.OR, BasicBinopAsm),
    new AbstractMap.SimpleEntry<Binop, ArithmeticLogicAssemble>(Binop.DIV, DivModAsm),
    new AbstractMap.SimpleEntry<Binop, ArithmeticLogicAssemble>(Binop.MOD, DivModAsm),
    new AbstractMap.SimpleEntry<Binop, ArithmeticLogicAssemble>(Binop.GREATER, CmpAsm), 
    new AbstractMap.SimpleEntry<Binop, ArithmeticLogicAssemble>(Binop.GREATER_EQUAL, CmpAsm),
    new AbstractMap.SimpleEntry<Binop, ArithmeticLogicAssemble>(Binop.LESS, CmpAsm),
    new AbstractMap.SimpleEntry<Binop, ArithmeticLogicAssemble>(Binop.LESS_EQUAL, CmpAsm),
    new AbstractMap.SimpleEntry<Binop, ArithmeticLogicAssemble>(Binop.EQUAL, CmpAsm),
    new AbstractMap.SimpleEntry<Binop, ArithmeticLogicAssemble>(Binop.INEQUAL, CmpAsm)
  );

  public static final UnopAssemble ArrayLenAsm = (rd, rn) -> {
    List<Instruction> list = new ArrayList<>();
    list.add(new LDR(rd, new AddressingMode2(AddrMode2.OFFSET, rd)));
    return list;
  };

  public static UnopAssemble NegationAsm = (rd, rn) -> {
    List<Instruction> list = new ArrayList<>();
    list.add(new Rsb(rd, rn, new Operand2(new Immediate(0, BitNum.CONST8))));
    return list;
  };

  public static UnopAssemble LogicNotAsm = (rd, rn) -> {
    List<Instruction> list = new ArrayList<>();
    list.add(new Xor(rd, rn, new Operand2(new Immediate(1, BitNum.CONST8))));
    return list;
  };

  public static UnopAssemble OrdAsm = (rd, rn) -> {
    List<Instruction> list = new ArrayList<>();
    return list;
  };

  public static UnopAssemble ChrAsm = (rd, rn) -> {
    List<Instruction> list = new ArrayList<>();
    return list;
  };

  public static final Map<Unop, UnopAssemble> unopInstruction = Map.ofEntries(
    new AbstractMap.SimpleEntry<Unop, UnopAssemble>(Unop.LEN, ArrayLenAsm),
    new AbstractMap.SimpleEntry<Unop, UnopAssemble>(Unop.MINUS, NegationAsm),
    new AbstractMap.SimpleEntry<Unop, UnopAssemble>(Unop.NOT, LogicNotAsm),
    new AbstractMap.SimpleEntry<Unop, UnopAssemble>(Unop.ORD, OrdAsm),
    new AbstractMap.SimpleEntry<Unop, UnopAssemble>(Unop.CHR, ChrAsm)
  );

  protected Register Rd, Rn;
  protected Operand2 operand2;

  protected ArithmeticLogic(Register rd, Register rn, Operand2 operand2) {
    Rd = rd;
    Rn = rn;
    this.operand2 = operand2;
  }
}
