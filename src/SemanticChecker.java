import antlr.WACCParser.*;
import antlr.WACCParserBaseVisitor;
import java.util.ArrayList;
import java.util.List;
import node.ExprNode;
import node.FuncNode;
import node.Node;
import node.ProgramNode;
import node.StatNode;
import node.stat.IfNode;
import node.stat.ScopeNode;
import node.stat.SeqNode;
import node.stat.WhileNode;

public class SemanticChecker extends WACCParserBaseVisitor<Node> {

  @Override
  public Node visitProgram(ProgramContext ctx) {
    List<FuncNode> functions = new ArrayList<>();
    List<StatNode> body = new ArrayList<>();

    for (FuncContext f : ctx.func()) {
      functions.add((FuncNode) visitFunc(f));
    }
    body.add((StatNode) visit(ctx.stat()));

    return new ProgramNode(functions, body);
  }

  @Override
  public Node visitFunc(FuncContext ctx) {
    //add func name and param info to symbol table
    //for now, ignore func name and param
    return new FuncNode((StatNode) visit(ctx.stat()));
  }

  @Override
  public Node visitSeqStat(SeqStatContext ctx) {
    SeqNode node = new SeqNode();

    for (StatContext s : ctx.stat()) {
      node.add((StatNode) visit(s));
    }

    return node;
  }

  @Override
  public Node visitIfStat(IfStatContext ctx) {
    //check the condition expr is bool or bool type
    return new IfNode((ExprNode) visit(ctx.expr()),
        (StatNode) visit(ctx.stat(0)), (StatNode) visit(ctx.stat(1)));
  }

  @Override
  public Node visitWhileStat(WhileStatContext ctx) {
    //check the condition expr is bool or bool type
    return new WhileNode((ExprNode) visit(ctx.expr()), (StatNode) visit(ctx.stat()));
  }

  @Override
  public Node visitScopeStat(ScopeStatContext ctx) {
    return new ScopeNode((StatNode) visit(ctx.stat()));
  }



  /*
  @Override
  public TypeSystem visitIntExpr(WACCParser.IntExprContext ctx) {
    int val = 0;
    try {
      val = Integer.parseInt(ctx.INT_LITER().getText());
    } catch ( NumberFormatException e) {
      System.err.println("bad format of integer " + ctx.INT_LITER().getText());
    }
    return new ExprTypes.IntegerType(val);
  }

  @Override
  public TypeSystem visitBoolExpr(WACCParser.BoolExprContext ctx) {
    boolean bVal;
    String text = ctx.BOOL_LITER().getText();
    assert (text.equals("true") || text.equals("false"));
    bVal = text.equals("true");
    return new ExprTypes.BoolType(bVal);
  }

  @Override
  public TypeSystem visitUnopExpr(WACCParser.UnopExprContext ctx) {
    String unop = ctx.uop.getText();
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

   */
}