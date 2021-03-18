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

  private final List<StatNode> body;
  private boolean avoidSubStack = false;
  private boolean isBeginEnd = false;

  public ScopeNode(StatNode node) {
    body = new ArrayList<>();
    if (node instanceof ScopeNode) {
      body.addAll(((ScopeNode) node).body);
    } else {
      body.add(node);
    }
    setLeaveAtEnd(getEndValue());
    isBeginEnd = true;
    setScope(node.getScope());
    this.minStackSpace = node.minStackSpace;
  }

  /* Handle the sequential statement */
  public ScopeNode(StatNode before, StatNode after) {
    body = new ArrayList<>();
    mergeScope(before);
    mergeScope(after);
    setLeaveAtEnd(getEndValue());
    this.minStackSpace = before.minStackSpace + after.minStackSpace;
  }

  /* constructor used by optimisation */
  public ScopeNode(ScopeNode cloneSrc, List<StatNode> body) {
    this.body = body;
    this.isBeginEnd = cloneSrc.isBeginEnd;
    this.avoidSubStack = cloneSrc.avoidSubStack;
    this.scope = cloneSrc.scope;
    this.minStackSpace = body.stream()
            .map(StatNode::minStackRequired)
            .reduce(Integer::sum).orElse(0);
  }

  private void mergeScope(StatNode s) {
    if (s instanceof ScopeNode && !((ScopeNode) s).isBeginEnd) {
      ScopeNode scope = (ScopeNode) s;
      body.addAll(scope.body);
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

  public void setAvoidSubStack() {
    avoidSubStack = true;
  }

  public boolean isAvoidSubStack() {
    return avoidSubStack;
  }

  public int getStackSize() {
    if (avoidSubStack) {
      return 0;
    }
    return scope.getSize();
  }

//  public int getMaxAccumulativeDepth() {
//    return maxAccumulativeDepth;
//  }
}
