package frontend.node.stat;

import frontend.node.expr.ExprNode;
import utils.NodeVisitor;

public class JumpNode extends StatNode {
    
    public enum JumpType { BREAK, CONTINUE }
    public enum JumpContext { FOR, WHILE, SWITCH }

    private final JumpType type;
    private final StatNode forIncrement;
    private final JumpContext context;

    public JumpNode(JumpType type, StatNode forIncrement, JumpContext context) {
        this.type = type;
        this.forIncrement = forIncrement;
        this.context = context;
    }

    public JumpType getJumpType() {
        return this.type;
    }

    public StatNode getForIncrement() { return this.forIncrement; }

    public JumpContext getJumpContext() {
        return context;
    }

    @Override
    public <T> T accept(NodeVisitor<T> visitor) {
        return visitor.visitJumpNode(this);
    }
}
