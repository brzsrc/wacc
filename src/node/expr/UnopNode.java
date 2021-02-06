package node.expr;

import type.BasicType;
import type.BasicTypeEnum;
import type.Type;
import utils.SymbolTable;



public class UnopNode extends ExprNode {

    public enum Unop {
        NOT, MINUS, LEN, ORD, CHR
    }
    
    ExprNode expr;
    Unop operator;

    public UnopNode(ExprNode expr, Unop operator) {
        super("");
        this.expr = expr;
        this.operator = operator;
    }

    @Override
    public Type getType(SymbolTable symbolTable) {
        switch (operator) {
            case NOT:
                return new BasicType(BasicTypeEnum.BOOLEAN);
            case LEN:
            case MINUS:
            case ORD:
                return new BasicType(BasicTypeEnum.INTEGER);
            case CHR:
                return new BasicType(BasicTypeEnum.CHAR);
            default:
                throw new IllegalArgumentException("operator field in Unop class not setup properly, with emun value: " + operator);
        }
    }

    @Override
    public void setValue(String value) {
        throw new UnsupportedOperationException("Binop does not support setting its value. Please pass ExprNode as the input!");
    }

    @Override
    public String getValue() {
        throw new UnsupportedOperationException("Binop does not support return of its value yet. Please specify an index!");
    }

}
