package node.expr;

import type.BasicType;
import type.BasicTypeEnum;

public class UnopNode extends ExprNode {

  /**
   * Represent a unary operator, with the <expr> recorded
   * Example: -3, len(arr), ord('a'), chr(65), !true
   */

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
    }
  }

}
