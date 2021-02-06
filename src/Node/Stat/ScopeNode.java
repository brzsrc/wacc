package Node.Stat;

import java.util.ArrayList;
import java.util.List;
import utils.SymbolTable;

public class ScopeNode implements StatNode {

  private final List<StatNode> body;
  private final SymbolTable symbolTable;

  public ScopeNode() {
    this.body = new ArrayList<>();
    this.symbolTable = new SymbolTable();
  }

  public void add(StatNode stat) {
    body.add(stat);
  }

  @Override
  public boolean hasEnd() {
    return false;
  }
}
