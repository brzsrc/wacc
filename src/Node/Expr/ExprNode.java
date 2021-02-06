package Node.Expr;

import Node.Node;
import Type.Type;

public abstract class ExprNode implements Node {
    protected Type type;
    protected String value;

    public Type getType() {
        return this.type;
    }

    public void setType(Type type) {
        // Need to check if types are coleaceable
        this.type = type;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public abstract boolean check();
}
