package node.expr;

import node.Node;
import type.Type;
import utils.SymbolTable;

public abstract class ExprNode implements Node {
    protected Type type;

    // todo: maybe delete this field, allow different Node take different value type
    //       since even if you allow any expr can return a value, this still require knowing what is the data type
    protected String value;

    public ExprNode(String value) {
        this.value = value;
    }

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

    // todo: check is explicitely implemented in semantic checker
    //       exprNode should be as simple as possible, otherwise backend may misuse field
    public boolean check() {
        return false;
    }
}
