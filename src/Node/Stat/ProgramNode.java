package Node.Stat;

import utils.IR.CFG.FuncNode;
import Node.Node;
import Node.StatNode;

import java.util.List;

public class ProgramNode implements Node {

  private final List<utils.IR.CFG.FuncNode> functions;
  private final List<StatNode> body;

  public ProgramNode(List<utils.IR.CFG.FuncNode> functions, List<StatNode> body) {
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
