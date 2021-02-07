package Node.Stat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import Node.Expr.ExprNode;
import utils.SymbolTable;

public class ScopeNode implements StatNode {

  private final List<StatNode> body;
  private final SymbolTable symbolTable;

  public ScopeNode() {
    this.body = new ArrayList<>();
    this.symbolTable = new SymbolTable();
  }

  /**
   * substitute old add function
   * used in stat ; stat rule visit, both branch return ScopeNode
   * mergeScope change this ScopeNode as ScopeNode merged the two symbolTable and statement body */
  public void mergeScope(ScopeNode otherScope) {
    body.addAll(otherScope.body);
    for(Map.Entry<String, ExprNode> entry : otherScope.symbolTable.getDictionary().entrySet()) {
      // use add one by one in order to catch repeat definition error using error detection in add() function
      symbolTable.add(entry.getKey(), entry.getValue());
    }
  }

  public List<StatNode> getAllStat() {
    return body;
  }

  public SymbolTable getSymbolTable() {
    return symbolTable;
  }

  @Override
  public boolean hasEnd() {
    return body.get(body.size() - 1).hasEnd();
  }

  @Override
  public boolean hasReturn() {
    // todo: change to iterate from end of scope, as return or exit are expected as the last instruction of scope
    for (StatNode stat : body) {
      if (stat.hasReturn()) {
        return true;
      }
    }
    return false;
  }
}
