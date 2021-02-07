package node;

import java.util.List;
import node.stat.StatNode;

public class ProgramNode implements Node {

  private final List<FuncNode> functions;
  private final StatNode body;

  public ProgramNode(List<FuncNode> functions, StatNode body) {
    this.functions = functions;
    this.body = body;
  }
}
