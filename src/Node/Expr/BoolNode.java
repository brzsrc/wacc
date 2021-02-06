package Node.Expr;

public class BoolNode extends ExprNode {
    
    public BoolNode(String value) {
        this.value = value;
    }

    @Override
    public boolean check() {
        return false;
    }
}
