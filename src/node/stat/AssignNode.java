package node.stat;

import node.expr.ExprNode;

public class AssignNode extends StatNode {
    private ExprNode lhs, rhs;

    public AssignNode(ExprNode lhs, ExprNode rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

}
