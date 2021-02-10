package node.expr;

import node.FuncNode;
import type.Type;
import utils.SymbolTable;

import java.util.List;

public class FunctionCallNode extends ExprNode {

  private FuncNode function;
  private List<ExprNode> params;
  private SymbolTable funcSymbolTable;

  public FunctionCallNode(FuncNode function, List<ExprNode> params, SymbolTable currScope) {
    this.function = function;
    this.params = params;
    this.funcSymbolTable = new SymbolTable(currScope);
    this.type = function.getReturnType();
  }

}
