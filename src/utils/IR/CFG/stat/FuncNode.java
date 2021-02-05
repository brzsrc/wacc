package utils.IR.CFG.stat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import utils.IR.CFG.StatNode;
import utils.Node.Expr.FuncParamNode;
import utils.Type.Type;

public class FuncNode {
  public Type returnType;
  public List<FuncParamNode> parameters;
  public List<StatNode> functionBody;

  public FuncNode(Type returnType, List<StatNode> functionBody, FuncParamNode params) {
    this.returnType = returnType;
    this.functionBody = functionBody;
    this.parameters = new ArrayList<>(Arrays.asList(params));
  }
}
