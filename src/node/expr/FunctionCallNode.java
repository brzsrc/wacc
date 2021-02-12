package node.expr;

import node.FuncNode;
import utils.SymbolTable;

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

}
