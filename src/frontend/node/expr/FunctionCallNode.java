package frontend.node.expr;

import frontend.node.FuncNode;
import frontend.utils.SymbolTable;

import java.util.List;

public class FunctionCallNode extends ExprNode {

  /**
   * Represent a function call, with FuncNode and parameter list, as well as its SymbolTable
   * recorded
   * Example; int a = call myFunc(), c = call myFunc(something)
   */

  private final FuncNode function;
  private final List<ExprNode> params;
  private final SymbolTable funcSymbolTable;

  public FunctionCallNode(FuncNode function, List<ExprNode> params, SymbolTable currScope) {
    this.function = function;
    this.params = params;
    this.funcSymbolTable = new SymbolTable(currScope);
    this.type = function.getReturnType();
  }

  @Override
  public void showNode(int indent) {
    System.out.print(function.getFunctionName());
    System.out.print("(");
    for (ExprNode node : params) {
      node.showNode(0);
      System.out.print(", ");
    }
    System.out.print(")");
  }
}
