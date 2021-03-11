package backend.intel;

import static utils.Utils.MAIN_BODY_NAME;
import static utils.backend.register.arm.ARMConcreteRegister.LR;
import static utils.backend.register.arm.ARMConcreteRegister.PC;
import static utils.backend.register.arm.ARMConcreteRegister.r0;

import backend.InstructionGenerator;
import backend.intel.instructions.IntelInstruction;
import backend.intel.instructions.Label;
import backend.intel.instructions.Push;
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

public class IntelInstructionGenerator extends InstructionGenerator<IntelInstruction> {

  /* label generators for data section and branches */
  private final LabelGenerator<Label> branchLabelGenerator;
  private final LabelGenerator<Label> dataLabelGenerator;

  public IntelInstructionGenerator() {
    branchLabelGenerator = new LabelGenerator<>(".L", Label.class);
    dataLabelGenerator = new LabelGenerator<>(".LC", Label.class);
  }

  @Override
  public Void visitArrayElemNode(ArrayElemNode node) {
    return null;
  }

  @Override
  public Void visitArrayNode(ArrayNode node) {
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
    /* 3 PUSH {lr} */
    instructions.add(new Push(Collections.singletonList(LR)));

    /* 4 main body */
    visit(node.getBody());

    /* 5 set exit value */
//    instructions.add(new LDR(r0, new ImmedAddress(0)));

    /* 6 POP {PC} .ltorg */
//    instructions.add(new ));
    instructions.add(new CFIEndProc());
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
