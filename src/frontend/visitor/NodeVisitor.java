package frontend.visitor;

import frontend.node.*;
import frontend.node.expr.*;
import frontend.node.stat.*;

public interface NodeVisitor {

  default void visit(Node node) {
    node.accept(this);
  }

  void visitArrayElemNode(ArrayElemNode node);

  void visitArrayNode(ArrayNode node);

  void visitBinopNode(BinopNode node);

  void visitBoolNode(BoolNode node);

  void visitCharNode(CharNode node);

  void visitFunctionCallNode(FunctionCallNode node);

  void visitIdentNode(IdentNode node);

  void visitIntegerNode(IntegerNode node);

  void visitPairElemNode(PairElemNode node);

  void visitPairNode(PairNode node);

  void visitStringNode(StringNode node);

  void visitUnopNode(UnopNode node);

  void visitAssignNode(AssignNode node);

  void visitDeclareNode(DeclareNode node);

  void visitExitNode(ExitNode node);

  void visitFreeNode(FreeNode node);

  void visitIfNode(IfNode node);

  void visitPrintlnNode(PrintlnNode node);

  void visitPrintNode(PrintNode node);

  void visitReadNode(ReadNode node);

  void visitReturnNode(ReturnNode node);

  void visitScopeNode(ScopeNode node);

  void visitSkipNode(SkipNode node);

  void visitWhileNode(WhileNode node);

  void visitFuncNode(FuncNode node);

  void visitProgramNode(ProgramNode node);

}
