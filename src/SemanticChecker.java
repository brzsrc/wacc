import antlr.WACCParser;
import antlr.WACCParserBaseVisitor;
import utils.ExprTypes;
import utils.TypeSystem;
import utils.SymbolTable;

import java.util.HashMap;
import java.util.Map;

import static utils.Utils.check;

public class SemanticChecker extends WACCParserBaseVisitor<TypeSystem> {

  /* the current sysmbol table holded by the current node */
  private SymbolTable curSymTable = null;

  @Override
  public TypeSystem visitProgram(WACCParser.ProgramContext ctx) {
    /* when visiting a new node, let the curent symbol table 
       become the new node's enclosing symTabel */
    SymbolTable newSymTable = new SymbolTable(curSymTable);
    /* sign the new node's symTable to the current symTable */
    curSymTable = newSymTable;
    for(WACCParser.FuncContext funcCtx: ctx.func()) {
      visitFunc(funcCtx);
    }
    visitStat(ctx.stat());
    return null;
  }

  @Override 
  public TypeSystem visitStat(WACCParser.StatContext ctx) { 
    return null;
  }

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
}