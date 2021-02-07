package Node.Stat;

import Node.Expr.ExprNode;

public class AssignmentNode implements StatNode {
    private ExprNode lhs, rhs;

    public AssignmentNode(ExprNode lhs, ExprNode rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

}
