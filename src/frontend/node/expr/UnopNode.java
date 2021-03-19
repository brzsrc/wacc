package frontend.node.expr;

import frontend.type.BasicType;
import frontend.type.BasicTypeEnum;
import utils.NodeVisitor;
import utils.Utils.AssemblyArchitecture;

public class UnopNode extends ExprNode {

  /**
   * Represent a unary operator, with the <expr> recorded
   * Example: -3, len(arr), ord('a'), chr(65), !true
   */

  public enum Unop {
    NOT, MINUS, LEN, ORD, CHR, BITNOT
  }

  ExprNode expr;
  Unop operator;

  public UnopNode(ExprNode expr, Unop operator, AssemblyArchitecture arch) {
    this.expr = expr;
    this.operator = operator;
    switch (operator) {
      case NOT:
        type = new BasicType(BasicTypeEnum.BOOL, arch);
        break;
      case LEN:
      case MINUS:
      case ORD:
        type = new BasicType(BasicTypeEnum.INT, arch);
        break;
      case CHR:
        type = new BasicType(BasicTypeEnum.CHAR, arch);
        break;
      case BITNOT:
        type = new BasicType(BasicTypeEnum.INT, arch);
        break;
    }
  }

  public Unop getOperator() {
    return operator;
  }

  public ExprNode getExpr() {
    return expr;
  }

  @Override
  public <T> T accept(NodeVisitor<T> visitor) {
    return visitor.visitUnopNode(this);
  }

  public boolean isImmediate() {
    return expr.isImmediate();
  }
}
