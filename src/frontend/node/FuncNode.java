package frontend.node;

import frontend.node.expr.IdentNode;
import frontend.node.stat.StatNode;
import frontend.type.Type;
import utils.NodeVisitor;
import java.util.List;

import static utils.Utils.POINTER_SIZE;

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

  public FuncNode(String functionName, Type returnType, List<IdentNode> params) {
    this(functionName, returnType, null, params);
  }

  public FuncNode(String functionName, Type returnType, StatNode functionBody, List<IdentNode> params) {
    this.returnType = returnType;
    this.functionBody = functionBody;
    this.parameters = params;
    this.functionName = functionName;
  }

  public StatNode getFunctionBody() {
    return functionBody;
  }

  public void setFunctionBody(StatNode functionBody) {
    int bodyStackSize = functionBody.getScope().getSize() - paramListStackSize() + POINTER_SIZE;
    for (IdentNode param : parameters) {
       functionBody.getScope().pushIdentStackOffset(bodyStackSize, param.getName());
    }
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

  @Override
  public <T> T accept(NodeVisitor<T> visitor) {
    return visitor.visitFuncNode(this);
  }
}
