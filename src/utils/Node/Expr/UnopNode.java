package utils.Node.Expr;

enum Unop {
    NOT, MINUX, LEN, ORD, CHR
}

public class UnopNode<T> extends ExprNode<T> {

    ExprNode<T> expr;
    Unop operator;

    public UnopNode(ExprNode<T> expr, Unop operator) {
        this.expr = expr;
        this.operator = operator;
    }

    @Override
    public boolean check() {
        return false;
    }

}
