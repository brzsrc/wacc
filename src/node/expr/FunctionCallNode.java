package node.expr;

import node.FuncNode;
import type.Type;
import utils.SymbolTable;

import java.util.List;

public class FunctionCallNode extends ExprNode {

  private FuncNode function;
  private List<ExprNode> params;

  public FunctionCallNode(FuncNode function, List<ExprNode> params) {
    super("");
    this.function = function;
    this.params = params;
  }

  @Override
  public Type getType(SymbolTable symbolTable) {
    return function.getReturnType();
  }

  @Override
  public void setValue(String value) {
    throw new UnsupportedOperationException("ArrayNode does not support setting value by using raw string literals. Please pass ExprNode as the input!");
  }

  @Override
  public String getValue() {
    throw new UnsupportedOperationException("ArrayNode does not support getting value in the form of string. Please specify an index!");
  }
}
