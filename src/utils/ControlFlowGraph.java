package utils;

import node.FuncNode;
import node.stat.ScopeNode;

import java.util.HashMap;
import java.util.Map;

public class ControlFlowGraph {

  private ScopeNode startNode;
  private Map<String, FuncNode> functions;

  public ControlFlowGraph(ScopeNode startNode) {
    this.startNode = startNode;
    this.functions = new HashMap<>();
  }
}
