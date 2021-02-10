package node.expr;

import type.Type;

public class PairElemNode extends ExprNode {
    private boolean isFirst;
    private ExprNode pair;

    public PairElemNode(ExprNode pair, boolean isFirst, Type type) {
        this.pair = pair;
        this.isFirst = isFirst;
        this.type = type;
    }
}
