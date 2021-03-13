package frontend.node;

import utils.NodeVisitor;
import java.util.Map;

import frontend.node.stat.StatNode;

public class ProgramNode implements Node {

  /**
   * Represent a full program, with its functions and program body recorded
   */

  private final Map<String, FuncNode> functions;
  private final StatNode body;
  private final Map<String, StructDeclareNode> structTable;

  public ProgramNode(Map<String, FuncNode> globalFuncTable, Map<String, StructDeclareNode> globalStructTable, StatNode body) {
    this.functions = globalFuncTable;
    this.structTable = globalStructTable;
    this.body = body;
  }

  public Map<String, StructDeclareNode> getStructTable() {
    return structTable;
  }

  public Map<String, FuncNode> getFunctions() {
    return functions;
  }

  public StatNode getBody() {
    return body;
  }

  @Override
  public <T> T accept(NodeVisitor<T> visitor) {
    return visitor.visitProgramNode(this);
  }
}
