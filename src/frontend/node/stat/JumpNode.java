package frontend.node.stat;

import frontend.node.expr.ExprNode;
import utils.NodeVisitor;

public class JumpNode extends StatNode {
    
    public enum JumpType { BREAK, CONTINUE }

    private final JumpType type;
    private final StatNode forIncrement;

    public JumpNode(JumpType type, StatNode forIncrement) {
        this.type = type;
        this.forIncrement = forIncrement;
    }

    public JumpType getJumpType() {
        return this.type;
    }

    public StatNode getForIncrement() { return this.forIncrement; }

    @Override
    public <T> T accept(NodeVisitor<T> visitor) {
        return visitor.visitJumpNode(this);
    }
}
