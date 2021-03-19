package frontend.node;

import frontend.node.expr.IdentNode;
import frontend.node.stat.StatNode;
import frontend.type.Type;
import utils.NodeVisitor;
import java.util.List;

public class FuncNode implements Node {

  /**
   * Represent a function declarationm, with returnType, parameters, and function body statement
   * being recorded
   *
   * FuncNode does not need to extend StatNode as it is an organic collection of statement rather
   * than a simple statement
   */

  private final String functionName;
  private final Type returnType;
  private final List<IdentNode> parameters;
  private StatNode functionBody;
  /* for function overload */
  private String overloadName;

  public FuncNode(String functionName, Type returnType, List<IdentNode> params) {
    this.returnType = returnType;
    this.functionBody = null;
    this.parameters = params;
    this.functionName = functionName;
    this.overloadName = null;
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

  public int paramListStackSize() {
    int size = 0;
    for (IdentNode ident : parameters) {
      size += ident.getType().getSize();
    }
    return size;
  }

  public String getFunctionName() {
    return functionName;
  }

  public void setOverloadName(String overloadName) {
    this.overloadName = overloadName;
  }

  public String getOverloadName() {
    return overloadName;
  }

  @Override
  public <T> T accept(NodeVisitor<T> visitor) {
    return visitor.visitFuncNode(this);
  }
}
