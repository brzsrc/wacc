package frontend;

import frontend.node.*;
import frontend.node.expr.*;
import frontend.node.stat.*;
import frontend.node.stat.SwitchNode.CaseStat;
import utils.NodeVisitor;

public class ASTPrinter implements NodeVisitor<Void> {

  private final int INDENT_SIZE = 2;
  private int leadingSpace = 0;

  private void appendLeadingSpace() {
    for (int i = 0; i < leadingSpace; i++) {
      System.out.print(" ");
    }
  }


  @Override
  public Void visitStructElemNode(StructElemNode node) {
    System.out.print(node.getName());
    for(String elemName : node.getElemNames()) {
      System.out.print("." + elemName);
    }
    return null;
  }

  @Override
  public Void visitStructNode(StructNode node) {
    if (!node.isInitialised()) {
      System.out.print("empty");
      return null;
    }

    System.out.print("new " + node.getName() + " {");
    for (int i = 0; i < node.getElemCount(); i++) {
      visit(node.getElem(i));
      System.out.print((i == node.getElemCount() - 1) ? "}\n" : ", ");
    }

    return null;
  }

  @Override
  public Void visitStructDeclareNode(StructDeclareNode node) {
    System.out.print("struct " + node.getName() + " {");
    for(int i = 0; i < node.getElemCount(); i++) {
      visitIdentNode(node.getElem(i));
      System.out.print((i == node.getElemCount() - 1) ? "}\n" : ", ");
    }
    return null;
  }

  @Override
  public Void visitArrayElemNode(ArrayElemNode node) {
    visit(node.getArray());
    for (ExprNode e : node.getIndex()) {
      System.out.print("[");
      visit(e);
      System.out.print("]");
    }
    return null;
  }

  @Override
  public Void visitArrayNode(ArrayNode node) {
    System.out.print("[");
    for(ExprNode e : node.getContent()) {
      visit(e);
      System.out.print(", ");
    }
    System.out.print("]");

    return null;
  }

  @Override
  public Void visitBinopNode(BinopNode node) {
    visit(node.getExpr1());
    System.out.print(" " + node.getOperator() + " ");
    visit(node.getExpr2());

    return null;
  }

  @Override
  public Void visitBoolNode(BoolNode node) {
    System.out.print(node.getVal());

    return null;

  }

  @Override
  public Void visitCharNode(CharNode node) {
    if (node.getAsciiValue() == '\0') {
      System.out.print("\\0");
      return null;
    }
    System.out.print("<char:" + node.getAsciiValue() + ">");
    return null;

  }

  @Override
  public Void visitFunctionCallNode(FunctionCallNode node) {
    System.out.print(node.getFunction().getFunctionName());
    System.out.print("(");
    for (ExprNode e : node.getParams()) {
      visit(e);
      System.out.print(", ");
    }
    System.out.print(")");
    return null;

  }

  @Override
  public Void visitIdentNode(IdentNode node) {
    System.out.print(node.getName());
    if (node.getSymbol() != null) {
      System.out.print(" (" + node.getSymbol().getStackOffset() + ")");
    }
    return null;
  }

  @Override
  public Void visitIntegerNode(IntegerNode node) {
    System.out.print(node.getVal());
    return null;

  }

  @Override
  public Void visitPairElemNode(PairElemNode node) {
    System.out.print(node.isFirst() ? "fst " : "snd ");
    visit(node.getPair());
    return null;

  }

  @Override
  public Void visitPairNode(PairNode node) {
    System.out.print("pair<");
    visitPairChild(node.getFst());
    System.out.print(", ");
    visitPairChild(node.getSnd());
    System.out.print(">");
    return null;

  }

  /* Pair Visitor Helper */
  private Void visitPairChild(ExprNode child) {
    if (child == null) {
      System.out.print("null");
    } else {
      visit(child);
    }
    return null;

  }

  @Override
  public Void visitStringNode(StringNode node) {
    System.out.print(node.getString());
    return null;

  }

  @Override
  public Void visitUnopNode(UnopNode node) {
    System.out.print(node.getOperator());
    visit(node.getExpr());
    return null;

  }

  @Override
  public Void visitAssignNode(AssignNode node) {
    appendLeadingSpace();
    visit(node.getLhs());
    System.out.print(" = ");
    visit(node.getRhs());
    System.out.println();
    return null;

  }

  @Override
  public Void visitDeclareNode(DeclareNode node) {
    appendLeadingSpace();
    node.getRhs().getType().showType();
    System.out.print(" " + node.getIdentifier() + " = ");
    visit(node.getRhs());
    System.out.println();
    return null;

  }

  @Override
  public Void visitExitNode(ExitNode node) {
    appendLeadingSpace();
    System.out.print("exit ");
    visit(node.getValue());
    System.out.println();
    return null;

  }

  @Override
  public Void visitFreeNode(FreeNode node) {
    appendLeadingSpace();
    System.out.print("free ");
    visit(node.getExpr());
    System.out.println();
    return null;

  }

  @Override
  public Void visitIfNode(IfNode node) {
    boolean isIfElse = node.getElseBody() != null;

    /* if EXPR : */
    appendLeadingSpace();
    System.out.print("if ");
    visit(node.getCond());
    System.out.println(" :");

    /* show if body */
    leadingSpace += INDENT_SIZE;
    visit(node.getIfBody());
    leadingSpace -= INDENT_SIZE;

    if (isIfElse) {
      /* else */
      appendLeadingSpace();
      System.out.println("else");

      /* show else body */
      leadingSpace += INDENT_SIZE;
      visit(node.getElseBody());
      leadingSpace -= INDENT_SIZE;
    }

    /*\n */
    appendLeadingSpace();
    System.out.println();
    return null;

  }

  @Override
  public Void visitPrintlnNode(PrintlnNode node) {
    appendLeadingSpace();
    System.out.print("println ");
    visit(node.getExpr());
    System.out.println();
    return null;

  }

  @Override
  public Void visitPrintNode(PrintNode node) {
    appendLeadingSpace();
    System.out.print("print ");
    visit(node.getExpr());
    System.out.println();
    return null;

  }

  @Override
  public Void visitReadNode(ReadNode node) {
    appendLeadingSpace();
    System.out.print("read ");
    visit(node.getInputExpr());
    System.out.println();
    return null;

  }

  @Override
  public Void visitReturnNode(ReturnNode node) {
    appendLeadingSpace();
    System.out.print("return ");
    visit(node.getExpr());
    System.out.println();
    return null;

  }

  @Override
  public Void visitScopeNode(ScopeNode node) {
    /* { */
    appendLeadingSpace();
    System.out.println("{scope size = " + node.getStackSize() +
            " current scope = " + node.getScope() +
            " parent = " + node.getScope().getParentSymbolTable());

    /* stat body */
    leadingSpace += INDENT_SIZE;
    for (StatNode s : node.getBody()) {
      visit(s);
    }
    leadingSpace -= INDENT_SIZE;

    /* } */
    appendLeadingSpace();
    System.out.println("}");

    return null;

  }

  @Override
  public Void visitSkipNode(SkipNode node) {
    /* do nothing */
    return null;

  }

  @Override
  public Void visitWhileNode(WhileNode node) {
    /* while COND : */
    appendLeadingSpace();
    if (node.isDoWhile()) {
      System.out.println("do ");
      /* body */
      leadingSpace += INDENT_SIZE;
      visit(node.getBody());
      leadingSpace -= INDENT_SIZE;

      appendLeadingSpace();
      System.out.print("while ");
      visit(node.getCond());
      System.out.println(" :");

    } else {
      System.out.print("while ");
      visit(node.getCond());
      System.out.println(" :");

      /* body */
      leadingSpace += INDENT_SIZE;
      visit(node.getBody());
      leadingSpace -= INDENT_SIZE;
    }

    return null;

  }

  @Override
  public Void visitFuncNode(FuncNode node) {
    node.getReturnType().showType();
    System.out.print(" " + node.getFunctionName() + "(");
    for(IdentNode i : node.getParamList()) {
      visitIdentNode(i);
      System.out.print(" ");
    }
    System.out.println(") :");

    leadingSpace += INDENT_SIZE;
    visit(node.getFunctionBody());
    leadingSpace -= INDENT_SIZE;

    System.out.println();
    return null;

  }

  @Override
  public Void visitProgramNode(ProgramNode node) {
    for(FuncNode func : node.getFunctions().values()) {
      visitFuncNode(func);
    }

    visit(node.getBody());
    return null;

  }

  @Override
  public Void visitForNode(ForNode node) {
    /* for 3 expressions : */
    appendLeadingSpace();
    System.out.print("for ");

    leadingSpace += INDENT_SIZE;
    visit(node.getInit());
    appendLeadingSpace();
    visit(node.getCond());
    visit(node.getIncrement());
    leadingSpace -= INDENT_SIZE;

    appendLeadingSpace();
    System.out.println(":");

    /* body */
    leadingSpace += INDENT_SIZE;
    visit(node.getBody());
    leadingSpace -= INDENT_SIZE;
    return null;
  }

  @Override
  public Void visitJumpNode(JumpNode node) {
    System.out.println(node.getJumpType().name());
    return null;
  }

  @Override
  public Void visitSwitchNode(SwitchNode node) {
    /* switch statement */
    appendLeadingSpace();
    System.out.print("switch ");
    visit(node.getExpr());
    System.out.println();

    /* switch body */
    leadingSpace += INDENT_SIZE;

    for (CaseStat c : node.getCases()) {
      appendLeadingSpace();
      System.out.print("case ");
      visit(c.getExpr());
      System.out.println();

      /* case body */
      leadingSpace += INDENT_SIZE;
      visit(c.getBody());
      leadingSpace -= INDENT_SIZE;
    }

    if (node.getDefault() != null) {
      appendLeadingSpace();
      System.out.println("default");
      leadingSpace += INDENT_SIZE;
      visit(node.getDefault());
    }

    leadingSpace -= 2 * INDENT_SIZE;

    return null;
  }
}
