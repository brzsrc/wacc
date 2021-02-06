package utils;

import Node.Stat.FuncNode;
import Node.Stat.ScopeNode;
import Node.Stat.StatNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ControlFlowGraph {

  private ScopeNode startNode;
  private Map<String, FuncNode> functions;

  public ControlFlowGraph(ScopeNode startNode) {
    this.startNode = startNode;
    this.functions = new HashMap<>();
  }
}
