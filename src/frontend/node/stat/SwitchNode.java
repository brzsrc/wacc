package frontend.node.stat;

import java.util.List;

import frontend.node.expr.ExprNode;
import utils.NodeVisitor;

public class SwitchNode extends StatNode {

    public static class CaseStat {
        private final ExprNode expr;
        private final StatNode stat;
    
        public CaseStat(ExprNode expr, StatNode stat) {
            this.expr = expr;
            this.stat = stat;
        }
    
        public ExprNode getExpr() {
            return expr;
        }
    
        public StatNode getBody() {
            return stat;
        }
    }

    private final ExprNode expr;
    private final List<CaseStat> cases;
    private final StatNode defaultCase;

    public SwitchNode(ExprNode expr, List<CaseStat> cases, StatNode defaultCase) {
        this.expr = expr;
        this.cases = cases;
        this.defaultCase = defaultCase;
        this.minStackSpace = cases.stream()
                .map(CaseStat::getBody)
                .map(StatNode::minStackRequired)
                .reduce(Integer::max)
                .orElse(0);
        this.minStackSpace = Math.max(minStackSpace, defaultCase.minStackSpace);
    }

    public ExprNode getExpr() {
        return expr;
    }

    public List<CaseStat> getCases() {
        return cases;
    }

    public StatNode getDefault() {
        return defaultCase;
    }

    @Override
    public <T> T accept(NodeVisitor<T> visitor) {
        return visitor.visitSwitchNode(this);
    }
}
