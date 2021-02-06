package utils;

import Node.Stat.FuncNode;
import Node.Stat.StatNode;

import java.util.ArrayList;
import java.util.List;

public class ControlFlowGraph {

  private StatNode startNode;
  private List<FuncNode> functions;


  public ControlFlowGraph() {
    this.functions = new ArrayList<>();
  }
}
