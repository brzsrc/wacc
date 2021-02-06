package Node.Stat;

import Node.Expr.ExprNode;

public class AssignmentNode implements StatNode {
    private ExprNode lhs, rhs;

    // an assignment can be added as one node in a sequence
    @Override
    public boolean isSeq() {
        return true;
    }

    @Override
    public boolean hasEnd() { return false; }

    @Override
    public boolean isReturn() { return false; }

}
