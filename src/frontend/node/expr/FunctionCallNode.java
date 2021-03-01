package frontend.node.expr;

import frontend.node.FuncNode;
import utils.frontend.symbolTable.SymbolTable;

import utils.NodeVisitor;
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
    this.weight = 1;
  }

  public FuncNode getFunction() {
    return function;
  }

  public List<ExprNode> getParams() {
    return params;
  }

  @Override
  public <T> T accept(NodeVisitor<T> visitor) {
    return visitor.visitFunctionCallNode(this);
  }
}
