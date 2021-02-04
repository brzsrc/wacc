package utils.Node.Expr;

public class BoolNode extends ExprNode<Boolean> {
    
    public BoolNode(boolean value) {
        this.value = value;
    }

    @Override
    public boolean check() {
        return false;
    }
}
