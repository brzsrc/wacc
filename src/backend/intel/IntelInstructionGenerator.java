package backend.intel;

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
import utils.NodeVisitor;

public class IntelInstructionGenerator implements NodeVisitor<Void> {

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
    return null;
  }
}
