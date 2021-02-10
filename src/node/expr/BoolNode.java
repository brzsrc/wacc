package node.expr;

import type.BasicType;
import type.BasicTypeEnum;

public class BoolNode extends ExprNode {

    private boolean val;
    
    public BoolNode(boolean val) {
        this.val = val;
        this.type = new BasicType(BasicTypeEnum.BOOLEAN);
    }

}
