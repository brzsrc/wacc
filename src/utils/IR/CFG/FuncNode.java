package utils.IR.CFG;

// implemented in stat/FuncNode, may be deleted later
public class FuncNode implements Node {

  //for now, ignore func name and param
  private final StatNode body;

  public FuncNode(StatNode body) {
    this.body = body;
  }

  public StatNode getBody() {
    return body;
  }
}
