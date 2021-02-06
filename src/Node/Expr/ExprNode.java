package Node.Expr;

import Type.Type;

public abstract class ExprNode<T> {
    protected Type type;
    protected String rawLiterals;
    protected T value;

    public Type getType() {
        return this.type;
    }

    public void setType(Type type) {
        // Need to check if types are coleaceable
        this.type = type;
    }

    public T getValue() {
        return this.value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public abstract boolean check();
}
