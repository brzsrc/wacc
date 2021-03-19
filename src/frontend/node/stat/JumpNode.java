package frontend.node.stat;

import frontend.node.expr.ExprNode;
import utils.NodeVisitor;

public class JumpNode extends StatNode {
    
    public enum JumpType { BREAK, CONTINUE }
    public enum JumpContext { FOR, WHILE, SWITCH }

    private final JumpType type;

    public JumpNode(JumpType type) {
        this.type = type;
    }

    public JumpType getJumpType() {
        return this.type;
    }

    @Override
    public <T> T accept(NodeVisitor<T> visitor) {
        return visitor.visitJumpNode(this);
    }
}
