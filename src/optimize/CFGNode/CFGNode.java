package optimize.CFGNode;

import frontend.node.stat.StatNode;

import java.util.ArrayList;
import java.util.List;

public class CFGNode {
  private List<CFGNode> preNodes, postNodes;
  private List<StatNode> statNodes;

  public CFGNode() {
    preNodes = new ArrayList<>();
    postNodes = new ArrayList<>();
    statNodes = new ArrayList<>();
  }

  public void addPreNode(CFGNode pre) {
    preNodes.add(pre);
  }

  public void addPostNode(CFGNode post) {
    postNodes.add(post);
  }

  public void addBody(StatNode stat) {
    statNodes.add(stat);
  }
}
