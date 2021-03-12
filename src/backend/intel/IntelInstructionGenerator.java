package backend.intel;


import backend.InstructionGenerator;
import backend.arm.instructions.BL;
import backend.arm.instructions.LDR;
import backend.arm.instructions.STR;
import backend.arm.instructions.STR.StrMode;
import backend.arm.instructions.addressing.AddressingMode2;
import backend.arm.instructions.addressing.ImmedAddress;
import backend.arm.instructions.addressing.Operand2;
import backend.intel.instructions.Call;
import backend.intel.instructions.IntelInstruction;
import backend.intel.instructions.Label;
import backend.intel.instructions.Mov;
import backend.intel.instructions.Pop;
import backend.intel.instructions.Push;
import backend.intel.instructions.Ret;
import backend.intel.instructions.address.IntelAddress;
import backend.intel.instructions.address.IntelImmediate;
import backend.intel.instructions.arithmetic.Add;
import backend.intel.instructions.directives.CFIEndProc;
import frontend.node.FuncNode;
import frontend.node.ProgramNode;
import frontend.node.StructDeclareNode;
import frontend.node.expr.ArrayElemNode;
import frontend.node.expr.ArrayNode;
import frontend.node.expr.BinopNode;
import frontend.node.expr.BoolNode;
import frontend.node.expr.CharNode;
import frontend.node.expr.FunctionCallNode;
import frontend.node.expr.IdentNode;
import frontend.node.expr.IntegerNode;
import frontend.node.expr.PairElemNode;
import frontend.node.expr.PairNode;
import frontend.node.expr.StringNode;
import frontend.node.expr.StructElemNode;
import frontend.node.expr.StructNode;
import frontend.node.expr.UnopNode;
import frontend.node.stat.AssignNode;
import frontend.node.stat.DeclareNode;
import frontend.node.stat.ExitNode;
import frontend.node.stat.ForNode;
import frontend.node.stat.FreeNode;
import frontend.node.stat.IfNode;
import frontend.node.stat.JumpNode;
import frontend.node.stat.PrintNode;
import frontend.node.stat.PrintlnNode;
import frontend.node.stat.ReadNode;
import frontend.node.stat.ReturnNode;
import frontend.node.stat.ScopeNode;
import frontend.node.stat.SkipNode;
import frontend.node.stat.SwitchNode;
import frontend.node.stat.WhileNode;
import java.util.Collections;
import utils.backend.LabelGenerator;
import utils.backend.register.Register;
import utils.backend.register.intel.IntelConcreteRegister;
import utils.backend.register.intel.IntelConcreteRegisterAllocator;

import static backend.arm.instructions.STR.StrMode.STR;
import static backend.arm.instructions.STR.StrMode.STRB;
import static backend.arm.instructions.addressing.AddressingMode2.AddrMode2.OFFSET;
import static utils.Utils.POINTER_SIZE;
import static utils.Utils.SystemCallInstruction.MALLOC;
import static utils.Utils.WORD_SIZE;
import static utils.backend.register.arm.ARMConcreteRegister.r0;
import static utils.backend.register.intel.IntelConcreteRegister.*;

public class IntelInstructionGenerator extends InstructionGenerator<IntelInstruction> {

  /* label generators for data section and branches */
  private final LabelGenerator<Label> branchLabelGenerator;
  private final LabelGenerator<Label> dataLabelGenerator;

  /* intel regsiter allocator */
  private final IntelConcreteRegisterAllocator intelRegAllocator;

  public IntelInstructionGenerator() {
    branchLabelGenerator = new LabelGenerator<>(".L", Label.class);
    dataLabelGenerator = new LabelGenerator<>(".LC", Label.class);
    intelRegAllocator = new IntelConcreteRegisterAllocator();
  }

  @Override
  public Void visitArrayElemNode(ArrayElemNode node) {
    return null;
  }

  @Override
  public Void visitArrayNode(ArrayNode node) {
    /* get the total number of bytes needed to allocate enough space for the array */
    int size = node.getType() == null ? 0 : node.getContentSize() * node.getLength();

    /* load edi with the number of bytes needed and malloc */
    instructions.add(new Mov(new IntelAddress(size), rdi.withSize(IntelInstructionSize.L)));
    instructions.add(new Call("malloc@PLT"));

    /* then MOV the result pointer to another register */
    IntelConcreteRegister addrReg = intelRegAllocator.allocate();
    instructions.add(new Mov(rax, addrReg));

    for (int i = 0; i < node.getLength(); i++) {
      visit(node.getElem(i));
      int indexOffset = i * node.getContentSize();
      instructions.add(new Mov(intelRegAllocator.curr(), new IntelAddress(addrReg)));
      instructions.add(new Add(new IntelImmediate(indexOffset), addrReg));
      intelRegAllocator.free();
    }

    intelRegAllocator.free();

    return null;
  }

  @Override
  public Void visitBinopNode(BinopNode node) {
    return null;
  }

  @Override
  public Void visitBoolNode(BoolNode node) {
    return null;
  }

  @Override
  public Void visitCharNode(CharNode node) {
    return null;
  }

  @Override
  public Void visitFunctionCallNode(FunctionCallNode node) {
    return null;
  }

  @Override
  public Void visitIdentNode(IdentNode node) {
    return null;
  }

  @Override
  public Void visitIntegerNode(IntegerNode node) {
    return null;
  }

  @Override
  public Void visitPairElemNode(PairElemNode node) {
    return null;
  }

  @Override
  public Void visitPairNode(PairNode node) {
    return null;
  }

  @Override
  public Void visitStringNode(StringNode node) {
    return null;
  }

  @Override
  public Void visitUnopNode(UnopNode node) {
    return null;
  }

  @Override
  public Void visitAssignNode(AssignNode node) {
    return null;
  }

  @Override
  public Void visitDeclareNode(DeclareNode node) {
    return null;
  }

  @Override
  public Void visitExitNode(ExitNode node) {
    return null;
  }

  @Override
  public Void visitFreeNode(FreeNode node) {
    return null;
  }

  @Override
  public Void visitIfNode(IfNode node) {
    return null;
  }

  @Override
  public Void visitPrintlnNode(PrintlnNode node) {
    return null;
  }

  @Override
  public Void visitPrintNode(PrintNode node) {
    return null;
  }

  @Override
  public Void visitReadNode(ReadNode node) {
    return null;
  }

  @Override
  public Void visitReturnNode(ReturnNode node) {
    return null;
  }

  @Override
  public Void visitScopeNode(ScopeNode node) {
    return null;
  }

  @Override
  public Void visitSkipNode(SkipNode node) {
    return null;
  }

  @Override
  public Void visitWhileNode(WhileNode node) {
    return null;
  }

  @Override
  public Void visitForNode(ForNode node) {
    return null;
  }

  @Override
  public Void visitJumpNode(JumpNode node) {
    return null;
  }

  @Override
  public Void visitSwitchNode(SwitchNode node) {
    return null;
  }

  @Override
  public Void visitFuncNode(FuncNode node) {
    return null;
  }

  @Override
  public Void visitProgramNode(ProgramNode node) {
    /* 1 translate all functions */
    for (FuncNode func : node.getFunctions().values()) {
      visitFuncNode(func);
    }

    /* 2 start of main */
    Label mainLabel = new Label("main");
    instructions.add(mainLabel);
    /* 3 PUSH rsp and MOV rsp to rbp */
    instructions.add(new Push(Collections.singletonList(rbp)));
    instructions.add(new Mov(rsp, rbp));

    /* 4 main body */
    visit(node.getBody());

    /* 5 set return value and return */
    instructions.add(new Mov(new IntelAddress(0), rax.withSize(IntelInstructionSize.L)));
    instructions.add(new Pop(rbp));
    instructions.add(new CFIEndProc());
    instructions.add(new Ret());
    return null;
  }

  @Override
  public Void visitStructElemNode(StructElemNode node) {
    return null;
  }

  @Override
  public Void visitStructNode(StructNode node) {
    return null;
  }

  @Override
  public Void visitStructDeclareNode(StructDeclareNode node) {
    return null;
  }
}
