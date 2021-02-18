package frontend.visitor;

import frontend.node.*;
import frontend.node.expr.*;
import frontend.node.stat.*;

public class NodePainter implements NodeVisitor {

  private final int INDENT_SIZE = 2;
  private int leadingSpace;

  private void appendLeadingSpace() {
    for (int i = 0; i < leadingSpace; i++) {
      System.out.print(" ");
    }
  }

  @Override
  public void visitArrayElemNode(ArrayElemNode node) {
    visit(node.getArray());
    for (ExprNode e : node.getIndex()) {
      System.out.print("[");
      visit(e);
      System.out.print("]");
    }
  }

  @Override
  public void visitArrayNode(ArrayNode node) {
    System.out.print("[");
    for(ExprNode e : node.getContent()) {
      visit(e);
      System.out.print(", ");
    }
    System.out.print("]");
  }

  @Override
  public void visitBinopNode(BinopNode node) {
    visit(node.getExpr1());
    System.out.print(" " + node.getOperator() + " ");
    visit(node.getExpr2());
  }

  @Override
  public void visitBoolNode(BoolNode node) {
    System.out.print(node.getVal());
  }

  @Override
  public void visitCharNode(CharNode node) {
    if (node.getAsciiValue() == '\0') {
      System.out.print("\\0");
      return;
    }
    System.out.print(node.getAsciiValue());
  }

  @Override
  public void visitFunctionCallNode(FunctionCallNode node) {
    System.out.print(node.getFunction().getFunctionName());
    System.out.print("(");
    for (ExprNode e : node.getParams()) {
      visit(e);
      System.out.print(", ");
    }
    System.out.print(")");
  }

  @Override
  public void visitIdentNode(IdentNode node) {
    System.out.print(node.getName());
  }

  @Override
  public void visitIntegerNode(IntegerNode node) {
    System.out.print(node.getVal());
  }

  @Override
  public void visitPairElemNode(PairElemNode node) {
    System.out.print(node.isFist() ? "fst " : "snd ");
    visit(node.getPair());
  }

  @Override
  public void visitPairNode(PairNode node) {
    System.out.print("pair<");
    visitPairChild(node.getFst());
    System.out.print(", ");
    visitPairChild(node.getSnd());
    System.out.print(">");
  }

  /* Pair Visitor Helper */
  private void visitPairChild(ExprNode child) {
    if (child == null) {
      System.out.print("null");
    } else {
      visit(child);
    }
  }

  @Override
  public void visitStringNode(StringNode node) {
    System.out.print(node.getString());
  }

  @Override
  public void visitUnopNode(UnopNode node) {
    System.out.print(node.getOperator());
    visit(node.getExpr());
  }

  @Override
  public void visitAssignNode(AssignNode node) {
    appendLeadingSpace();
    visit(node.getLhs());
    System.out.print(" = ");
    visit(node.getRhs());
    System.out.println();
  }

  @Override
  public void visitDeclareNode(DeclareNode node) {
    appendLeadingSpace();
    node.getRhs().getType().showType();
    System.out.print(" " + node.getIdentifier() + " = ");
    visit(node.getRhs());
    System.out.println();
  }

  @Override
  public void visitExitNode(ExitNode node) {
    appendLeadingSpace();
    System.out.print("exit ");
    visit(node.getValue());
    System.out.println();
  }

  @Override
  public void visitFreeNode(FreeNode node) {
    appendLeadingSpace();
    System.out.print("free ");
    visit(node.getExpr());
    System.out.println();
  }

  @Override
  public void visitIfNode(IfNode node) {
    /* if EXPR : */
    appendLeadingSpace();
    System.out.print("if ");
    visit(node.getCond());
    System.out.println(" :");

    leadingSpace += INDENT_SIZE;

    /* show if body */
    visit(node.getIfBody());

    /* else */
    appendLeadingSpace();
    System.out.println("else");

    /* show else body */
    visit(node.getElseBody());

    /*\n */
    appendLeadingSpace();
    System.out.println();
  }

  @Override
  public void visitPrintlnNode(PrintlnNode node) {
    appendLeadingSpace();
    System.out.print("println ");
    visit(node.getExpr());
    System.out.println();
  }

  @Override
  public void visitPrintNode(PrintNode node) {
    appendLeadingSpace();
    System.out.print("print ");
    visit(node.getExpr());
    System.out.println();
  }

  @Override
  public void visitReadNode(ReadNode node) {
    appendLeadingSpace();
    System.out.print("read ");
    visit(node.getInputExpr());
    System.out.println();
  }

  @Override
  public void visitReturnNode(ReturnNode node) {
    appendLeadingSpace();
    System.out.print("return ");
    visit(node.getExpr());
    System.out.println();
  }

  @Override
  public void visitScopeNode(ScopeNode node) {
    /* { */
    appendLeadingSpace();
    System.out.println("{");

    /* stat body */
    leadingSpace += INDENT_SIZE;
    for (StatNode s : node.getBody()) {
      visit(s);
    }

    /* } */
    appendLeadingSpace();
    System.out.println("}");

  }

  @Override
  public void visitSkipNode(SkipNode node) {
    /* do nothing */
  }

  @Override
  public void visitWhileNode(WhileNode node) {
    /* while COND : */
    appendLeadingSpace();
    System.out.print("while ");
    visit(node.getCond());
    System.out.println(" :");

    /* body */
    leadingSpace += INDENT_SIZE;
    visit(node.getBody());

  }

  @Override
  public void visitFuncNode(FuncNode node) {
    node.getReturnType().showType();
    System.out.print(" " + node.getFunctionName() + "(");
    for(IdentNode i : node.getParamList()) {
      visitIdentNode(i);
      System.out.print(" ");
    }
    System.out.println(") :");
    leadingSpace = INDENT_SIZE;
    /* May let the function body be directly a ScopeNode */
    visit(node.getFunctionBody());
    System.out.println();
  }

  @Override
  public void visitProgramNode(ProgramNode node) {
    for(FuncNode func : node.getFunctions().values()) {
      leadingSpace = 0;
      visitFuncNode(func);
    }
    leadingSpace = 0;
    /* May let the main body be directly a ScopeNode */
    visit(node.getBody());
  }
}
