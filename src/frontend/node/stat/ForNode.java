package frontend.node.stat;

import java.util.List;

import frontend.node.expr.ExprNode;
import utils.NodeVisitor;

public class ForNode extends StatNode {

    private final StatNode init;
    private final ExprNode cond;
    private final StatNode increment;
    private final StatNode body;

    public ForNode(StatNode init, ExprNode cond, StatNode increment, StatNode body) {
        this.init = init;
        this.cond = cond;
        this.increment = increment;
        this.body = body;
    }

    public StatNode getInit() {
        return init;
    }

    public ExprNode getCond() { return cond; }

    public StatNode getIncrement() {
        return increment;
    }

    public StatNode getBody() {
        return body;
    }

    @Override
    public <T> T accept(NodeVisitor<T> visitor) {
        return visitor.visitForNode(this);
    }
}
