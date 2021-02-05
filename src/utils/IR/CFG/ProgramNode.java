package utils.IR.CFG;

import java.util.List;

public class ProgramNode implements Node {

  private final List<FuncNode> functions;
  private final List<StatNode> body;

  public ProgramNode(List<FuncNode> functions, List<StatNode> body) {
    this.functions = functions;
    this.body = body;
  }

  public boolean allFunctionsLeaveAtEnd() {
    for (FuncNode node : functions) {
      if (!node.getBody().hasEnd()) {
        return false;
      }
    }
    return true;
  }

}
