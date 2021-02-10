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
        this.expr = expr;
        this.operator = operator;
        switch (operator) {
            case NOT:
                type = new BasicType(BasicTypeEnum.BOOLEAN);
                break;
            case LEN:
            case MINUS:
            case ORD:
                type = new BasicType(BasicTypeEnum.INTEGER);
                break;
            case CHR:
                type = new BasicType(BasicTypeEnum.CHAR);
                break;
            default:
                throw new IllegalArgumentException("operator field in Unop class not setup properly, with emun value: " + operator);
        }
    }

}
