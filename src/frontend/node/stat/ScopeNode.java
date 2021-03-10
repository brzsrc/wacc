package frontend.node.stat;

import utils.NodeVisitor;
import utils.frontend.symbolTable.SymbolTable;

import java.util.ArrayList;
import java.util.List;

public class ScopeNode extends StatNode {

  /**
   * Represent BEGIN <stat> END scope statement, as well as
   * <stat> ; <stat> sequential statement
   */

  private final List<StatNode> body = new ArrayList<>();
  private boolean isFuncBody = false;
  private boolean isBeginEnd = false;
  private boolean isForStat  = false;

  public ScopeNode(StatNode node) {
    if (node instanceof ScopeNode) {
      body.addAll(((ScopeNode) node).body);
    } else {
      body.add(node);
    }
    setLeaveAtEnd(getEndValue());
    isBeginEnd = true;
    setScope(node.getScope());
  }

  /* Handle the sequential statement */
  public ScopeNode(StatNode before, StatNode after) {
    mergeScope(before);
    mergeScope(after);
    setLeaveAtEnd(getEndValue());
  }

  private void mergeScope(StatNode s) {
    if (s instanceof ScopeNode && !((ScopeNode) s).isBeginEnd) {
      body.addAll(((ScopeNode) s).body);
    } else if (!(s instanceof SkipNode)) {
      body.add(s);
    }
  }

  /* This will help to determine whether there is a return statement at the end of a sequence */
  private boolean getEndValue() {
    return !body.isEmpty() && body.get(body.size() - 1).leaveAtEnd();
  }

  public List<StatNode> getBody() {
    return body;
  }

  @Override
  public <T> T accept(NodeVisitor<T> visitor) {
    return visitor.visitScopeNode(this);
  }

  public void setFuncBody() {
    isFuncBody = true;
  }

  public void setForStat() {
    isForStat = true;
  }

  public int getStackSize() {
    if (isFuncBody) {
      return 0;
    }
    return scope.getSize();
  }
}
