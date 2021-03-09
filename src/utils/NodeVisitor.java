package utils;

import frontend.node.*;
import frontend.node.expr.*;
import frontend.node.stat.*;

public interface NodeVisitor<T> {

  default T visit(Node node) {
    node.accept(this);
    return null;
  }

  T visitStructElemNode(StructElemNode node);

  T visitStructNode(StructNode node);

  T visitStructDeclareNode(StructDeclareNode node);

  T visitArrayElemNode(ArrayElemNode node);

  T visitArrayNode(ArrayNode node);

  T visitBinopNode(BinopNode node);

  T visitBoolNode(BoolNode node);

  T visitCharNode(CharNode node);

  T visitFunctionCallNode(FunctionCallNode node);

  T visitIdentNode(IdentNode node);

  T visitIntegerNode(IntegerNode node);

  T visitPairElemNode(PairElemNode node);

  T visitPairNode(PairNode node);

  T visitStringNode(StringNode node);

  T visitUnopNode(UnopNode node);

  T visitAssignNode(AssignNode node);

  T visitDeclareNode(DeclareNode node);

  T visitExitNode(ExitNode node);

  T visitFreeNode(FreeNode node);

  T visitIfNode(IfNode node);

  T visitPrintlnNode(PrintlnNode node);

  T visitPrintNode(PrintNode node);

  T visitReadNode(ReadNode node);

  T visitReturnNode(ReturnNode node);

  T visitScopeNode(ScopeNode node);

  T visitSkipNode(SkipNode node);

  T visitWhileNode(WhileNode node);

  T visitFuncNode(FuncNode node);

  T visitProgramNode(ProgramNode node);

}
