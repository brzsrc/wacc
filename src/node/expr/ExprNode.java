package node.expr;

import node.Node;
import type.Type;
import utils.SymbolTable;

public abstract class ExprNode implements Node {
    protected Type type;

    public Type getType() {
        return this.type;
    }

    public void setType(Type type) {
        this.type = type;
    }
    
    public boolean check() {
        return false;
    }

    @Override
    public ExprNode asExprNode() {
        return this;
    }
}
