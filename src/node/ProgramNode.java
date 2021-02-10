package node;

import java.util.List;
import java.util.Map;

import node.stat.StatNode;

public class ProgramNode implements Node {

  private final Map<String, FuncNode> functions;
  private final StatNode body;

  public ProgramNode(Map<String, FuncNode> functions, StatNode body) {
    this.functions = functions;
    this.body = body;
  }
}
