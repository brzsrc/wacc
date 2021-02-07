package Node.Expr;

import Node.Node;
import Type.Type;
import utils.SymbolTable;

public abstract class ExprNode implements Node {
    protected Type type;

    // todo: maybe delete this field, allow different Node take different value type
    //       since even if you allow any expr can return a value, this still require knowing what is the data type
    protected String value;

    public Type getType(SymbolTable symbolTable) {
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
