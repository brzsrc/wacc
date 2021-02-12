package node;

import java.util.Map;

import node.stat.StatNode;

public class ProgramNode implements Node {

  /**
   * Represent a full program, with its functions and program body recorded
   */

  private final Map<String, FuncNode> functions;
  private final StatNode body;

  public ProgramNode(Map<String, FuncNode> functions, StatNode body) {
    this.functions = functions;
    this.body = body;
  }
}
