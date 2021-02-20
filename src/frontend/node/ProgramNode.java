package frontend.node;

import frontend.visitor.NodeVisitor;
import java.util.Map;

import frontend.node.stat.StatNode;

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
