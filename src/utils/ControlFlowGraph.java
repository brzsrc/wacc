package utils;

import utils.IR.CFG.FuncNode;
import Node.Expr.StatNode;

import java.util.ArrayList;
import java.util.List;

public class ControlFlowGraph {

  private StatNode startNode;
  private List<FuncNode> functions;


  public ControlFlowGraph() {
    this.functions = new ArrayList<>();
  }
}
