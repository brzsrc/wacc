package frontend.node;

import frontend.node.expr.IdentNode;
import frontend.node.stat.StatNode;
import java.util.List;

import frontend.type.Type;

public class FuncNode implements Node {

  /**
   * Represent a function declarationm, with returnType, parameters, and function body statement
   * being recorded
   *
   * FuncNode does not need to extend StatNode as it is an organic collection of statement rather
   * than a simple statement
   */

  private final Type returnType;
  private final List<IdentNode> parameters;
  private StatNode functionBody;

  public FuncNode(Type returnType, List<IdentNode> params) {
    this(returnType, null, params);
  }

  public FuncNode(Type returnType, StatNode functionBody, List<IdentNode> params) {
    this.returnType = returnType;
    this.functionBody = functionBody;
    this.parameters = params;
  }

  public StatNode getFunctionBody() {
    return functionBody;
  }

  public void setFunctionBody(StatNode functionBody) {
    this.functionBody = functionBody;
  }

  public Type getReturnType() {
    return returnType;
  }

  public List<IdentNode> getParamList() {
    return parameters;
  }
}
