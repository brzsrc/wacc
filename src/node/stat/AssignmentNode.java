package node.stat;

import node.expr.ExprNode;

public class AssignmentNode extends StatNode {
    private ExprNode lhs, rhs;

    public AssignmentNode(ExprNode lhs, ExprNode rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

}
