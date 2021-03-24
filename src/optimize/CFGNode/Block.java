package optimize.CFGNode;

import frontend.node.stat.ScopeNode;
import frontend.node.stat.StatNode;
import utils.NodeVisitor;

import java.util.ArrayList;
import java.util.List;

public class Block extends StatNode {

  /* block is same as a Scope with no branch inside
  *  if a scope node contain if/loop/switch, scope is split into 3 parts:
  *      code before IF -- ifBody   -- code after IF
  *                     +- elseBody -+
  *  loop/switch follow similar split
  *  creation of blockCode use clone of scopeNode
  *  after splitting as shown above, original scopeNode is changed to
  *      a scopeNode containing 3 statements: 2 blocks and 1 ifNode */
  private ScopeNode blockCode;

  public Block(ScopeNode srcNode, List<StatNode> code) {
    /* clone a scopeNode with same scope, only different code */
    blockCode = new ScopeNode(srcNode, code);
  }

  // todo: still need to use whileCFGNode ifCFGNode... to reduce need of changing front end nodes

  @Override
  public <T> T accept(NodeVisitor<T> visitor) {
    throw new IllegalArgumentException("visiting a block statement, should nave already been transferred back to plane code");
  }
}
