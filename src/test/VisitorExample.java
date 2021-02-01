package test;

import antlr.FullParser;
import antlr.FullParserBaseVisitor;

import java.util.HashMap;
import java.util.Map;

public class VisitorExample extends FullParserBaseVisitor<TypeSystem> {
  final VisitorExample Visitor = new VisitorExample();

  private void check(TypeSystem type, Class target) {
    if (type.getClass() != target) {
      throw new IllegalArgumentException("Semantic check: type failed to match");
    }
  }

  @Override
  public TypeSystem visitIntExpr(FullParser.IntExprContext ctx) {
    int val = 0;
    try {
      val = Integer.parseInt(ctx.INT_LITER().getText());
    } catch ( NumberFormatException e) {
      System.err.println("bad format of integer " + ctx.INT_LITER().getText());
    }
    return new ExprTypes.IntegerType(val);
  }

  @Override
  public TypeSystem visitBoolExpr(FullParser.BoolExprContext ctx) {
    boolean bVal;
    String text = ctx.BOOL_LITER().getText();
    assert (text.equals("true") || text.equals("false"));
    bVal = text.equals("true");
    return new ExprTypes.BoolType(bVal);
  }

  @Override
  public TypeSystem visitUnopExpr(FullParser.UnopExprContext ctx) {
    String unop = ctx.UNOP().getText();
    TypeSystem type = visitChildren(ctx.expr());
    switch (unop) {
      case "not":
        check(type, ExprTypes.BoolType.class);
        return new ExprTypes.BoolType(((ExprTypes.BoolType) type).bVal);
      case "len":
        check(type, ExprTypes.ArrayType.class);
        return new ExprTypes.ArrayType();
      case "ord":
        check(type, ExprTypes.CharType.class);
        return new ExprTypes.CharType();
      case "chr":
        check(type, ExprTypes.IntegerType.class);
        return new ExprTypes.IntegerType(((ExprTypes.IntegerType) type).val);
    }
    System.err.println("fail to match unop" + unop + " in semantic check, error in parser or lexer");
    return null;
  }
}