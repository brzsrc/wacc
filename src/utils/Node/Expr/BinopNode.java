package utils.Node.Expr;

enum Binops {
    PLUS, MINUS, MUL, DIV, MOD, GREATER, GREATER_EQUAL, LESS, LESS_EQUAL, EQUAL, UNEQUAL, AND, OR
}

public class BinopNode<T> extends ExprNode<T> {

    private ExprNode<T> expr1;
    private ExprNode<T> expr2;
    private Binops operator;

    @Override
    public boolean check() {
        // TODO Auto-generated method stub
        return false;
    }

    public ExprNode<T> getExpr1() {
        return expr1;
    }

    public void setExpr1(ExprNode<T> expr1) {
        this.expr1 = expr1;
    }

    public ExprNode<T> getExpr2() {
        return expr2;
    }

    public void setExpr2(ExprNode<T> expr2) {
        this.expr2 = expr2;
    }

    public Binops getOperator() {
        return operator;
    }

    public void setOperator(Binops operator) {
        this.operator = operator;
    }

}
