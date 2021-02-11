package node.expr;

import type.Type;

public class PairElemNode extends ExprNode {

    private ExprNode pair;

    public PairElemNode(ExprNode pair, Type type) {
        this.pair = pair;
        this.type = type;
    }
}
