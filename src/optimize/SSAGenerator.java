package optimize;

import frontend.node.FuncNode;
import frontend.node.ProgramNode;
import frontend.node.StructDeclareNode;
import frontend.node.expr.*;
import frontend.node.stat.*;
import optimize.CFGNode.CFGNode;
import utils.NodeVisitor;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class SSAGenerator implements NodeVisitor<SSAGenerator.Pair<CFGNode, CFGNode>> {
  /* node where BREAK or CONTINUE will have edge point to */
  Stack<CFGNode> breakPostNodes, continuePostNodes;

  public SSAGenerator() {
    breakPostNodes = new Stack<>();
    continuePostNodes = new Stack<>();
  }

  @Override
  public Pair<CFGNode, CFGNode> visitStructElemNode(StructElemNode node) {
    return null;
  }

  @Override
  public Pair<CFGNode, CFGNode> visitStructNode(StructNode node) {
    return null;
  }

  @Override
  public Pair<CFGNode, CFGNode> visitStructDeclareNode(StructDeclareNode node) {
    return null;
  }

  @Override
  public Pair<CFGNode, CFGNode> visitArrayElemNode(ArrayElemNode node) {
    return null;
  }

  @Override
  public Pair<CFGNode, CFGNode> visitArrayNode(ArrayNode node) {
    return null;
  }

  @Override
  public Pair<CFGNode, CFGNode> visitBinopNode(BinopNode node) {
    return null;
  }

  @Override
  public Pair<CFGNode, CFGNode> visitBoolNode(BoolNode node) {
    return null;
  }

  @Override
  public Pair<CFGNode, CFGNode> visitCharNode(CharNode node) {
    return null;
  }

  @Override
  public Pair<CFGNode, CFGNode> visitFunctionCallNode(FunctionCallNode node) {
    return null;
  }

  @Override
  public Pair<CFGNode, CFGNode> visitIdentNode(IdentNode node) {
    return null;
  }

  @Override
  public Pair<CFGNode, CFGNode> visitIntegerNode(IntegerNode node) {
    return null;
  }

  @Override
  public Pair<CFGNode, CFGNode> visitPairElemNode(PairElemNode node) {
    return null;
  }

  @Override
  public Pair<CFGNode, CFGNode> visitPairNode(PairNode node) {
    return null;
  }

  @Override
  public Pair<CFGNode, CFGNode> visitStringNode(StringNode node) {
    return null;
  }

  @Override
  public Pair<CFGNode, CFGNode> visitUnopNode(UnopNode node) {
    return null;
  }

  @Override
  public Pair<CFGNode, CFGNode> visitAssignNode(AssignNode node) {
    return null;
  }

  @Override
  public Pair<CFGNode, CFGNode> visitDeclareNode(DeclareNode node) {
    return null;
  }

  @Override
  public Pair<CFGNode, CFGNode> visitExitNode(ExitNode node) {
    return null;
  }

  @Override
  public Pair<CFGNode, CFGNode> visitFreeNode(FreeNode node) {
    return null;
  }

  @Override
  public Pair<CFGNode, CFGNode> visitIfNode(IfNode node) {
    return null;
  }

  @Override
  public Pair<CFGNode, CFGNode> visitPrintlnNode(PrintlnNode node) {
    return null;
  }

  @Override
  public Pair<CFGNode, CFGNode> visitPrintNode(PrintNode node) {
    return null;
  }

  @Override
  public Pair<CFGNode, CFGNode> visitReadNode(ReadNode node) {
    return null;
  }

  @Override
  public Pair<CFGNode, CFGNode> visitReturnNode(ReturnNode node) {
    return null;
  }

  @Override
  public Pair<CFGNode, CFGNode> visitScopeNode(ScopeNode node) {
    CFGNode currNode = new CFGNode();
    CFGNode scopeStartNode = currNode;
    for (StatNode stat : node.getBody()) {
      if (isLoop(stat)) {
        /*        <-------------------+
                                      |
           startNode -> loop main body -> endNode
                |
                +---------------------------->
           break also have edge point to endNode
           continue also have edge point to startNode
           edge shown here are only for while loop, for and do-while have different linking edge,
             different link implemented in each visitNode
        */
        CFGNode endNode = new CFGNode(),
                startNode = new CFGNode();
        breakPostNodes.push(endNode);
        continuePostNodes.push(startNode);
        visit(stat);
        breakPostNodes.pop();
        continuePostNodes.pop();

        /* link currNode with startNode */
        link(currNode, startNode);

        /* currentNode change to endNode, next block is written in endNode */
        currNode = endNode;
      } else if (stat instanceof IfNode) {
        /*      +----------------->
                |
           if(cond) -> ifBody   elseBody -> endNode
                           |
                           +------------------>
           break and continue does not jump to endNode, no need to push to stack
        */
        CFGNode endNode = new CFGNode();

        IfNode ifStat = (IfNode) stat;
        Pair<CFGNode, CFGNode> ifBody = visit(ifStat.getIfBody());
        Pair<CFGNode, CFGNode> elseBody = visit(ifStat.getElseBody());

        /* link 4 edge shown in graph above */
        link(currNode, ifBody.getFst());
        link(currNode, elseBody.getFst());
        link(ifBody.getSnd(), endNode);
        link(elseBody.getSnd(), endNode);

        currNode = endNode;

      } else if (stat instanceof JumpNode) {
        /* implement edge shown in loop part,
        *  continue always have edge to startNode
        *  break always have edge to endNode */
        if (((JumpNode) stat).getJumpType() == JumpNode.JumpType.BREAK) {
          link(currNode, breakPostNodes.peek());
        } else {
          link(currNode, continuePostNodes.peek());
        }
        /* return directly, so that CFG will not link this block to end of if/loop
        *  also eliminate junk after break/continue  */
        currNode.addBody(stat);
        return new Pair<>(scopeStartNode, currNode);
      } else if (stat instanceof SwitchNode) {
        /*
          switch(expr) -> expr       -> switchEnd
                       -> expr       |
                       -> expr       |
                       -> expr       |
        update break target to end of switch, no need to change continue target */
        CFGNode switchEnd = new CFGNode();

        breakPostNodes.push(switchEnd);
        Pair<CFGNode, CFGNode> switchStartEnd = visit(stat);
        breakPostNodes.pop();

        /* link currNode with start of switch */
        link(currNode, switchStartEnd.getFst());

        /* end of switch is next block */
        assert switchEnd == switchStartEnd.getSnd();
        currNode = switchEnd;
      } else {
        /* statement has no branch*/
        currNode.addBody(stat);
      }
    }
    return new Pair<>(scopeStartNode, currNode);
  }

  private boolean isLoop(StatNode stat) {
    return stat instanceof WhileNode ||
            stat instanceof ForNode;
  }

  private void link(CFGNode parent, CFGNode child) {
    parent.addPostNode(child);
    child.addPreNode(parent);
  }

  @Override
  public Pair<CFGNode, CFGNode> visitSkipNode(SkipNode node) {
    return null;
  }

  @Override
  public Pair<CFGNode, CFGNode> visitWhileNode(WhileNode node) {
    return null;
  }

  @Override
  public Pair<CFGNode, CFGNode> visitForNode(ForNode node) {
    return null;
  }

  @Override
  public Pair<CFGNode, CFGNode> visitJumpNode(JumpNode node) {
    return null;
  }

  @Override
  public Pair<CFGNode, CFGNode> visitSwitchNode(SwitchNode node) {
    return null;
  }

  @Override
  public Pair<CFGNode, CFGNode> visitFuncNode(FuncNode node) {
    return null;
  }

  @Override
  public Pair<CFGNode, CFGNode> visitProgramNode(ProgramNode node) {
    return null;
  }

  private class Pair<T, K> {
    public T t;
    public K k;

    public Pair(T t, K k) {
      this.t = t;
      this.k = k;
    }

    public T getFst() {
      return t;
    }

    public K getSnd() {
      return k;
    }
  }

}
