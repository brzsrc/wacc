import antlr.WACCParser;
import antlr.WACCParserBaseVisitor;
import utils.ExprTypes;
import utils.SymbolTable;
import utils.Type.*;

import java.util.HashMap;
import java.util.Map;

import static utils.Utils.check;

public class SemanticChecker extends WACCParserBaseVisitor<WACCType> {

  /* the current sysmbol table holded by the current node */
  private SymbolTable symbolTable = null;

  @Override
  public WACCType visitProgram(WACCParser.ProgramContext ctx) {
    /* when visiting a new node, let the curent symbol table 
       become the new node's enclosing symTable */
    this.symbolTable = new SymbolTable(null);

    for (WACCParser.FuncContext funcCtx : ctx.func()) {
      visitFunc(funcCtx);
    }

    visitStat(ctx.stat());
    
    return null;
  }

  @Override 
  public WACCType visitStat(WACCParser.StatContext ctx) { 
    return null;
  }

  /**
   * visitors for expr */
  @Override
  public WACCType visitIntExpr(WACCParser.IntExprContext ctx) {
    int val = 0;
    try {
      val = Integer.parseInt(ctx.INT_LITER().getText());
    } catch ( NumberFormatException e) {
      System.err.println("bad format of integer " + ctx.INT_LITER().getText());
    }
    return new IntegerType(val);
  }

  @Override
  public WACCType visitBoolExpr(WACCParser.BoolExprContext ctx) {
    boolean bVal;
    String text = ctx.BOOL_LITER().getText();
    assert (text.equals("true") || text.equals("false"));
    bVal = text.equals("true");
    return new BoolType(bVal);
  }

  @Override
  public WACCType visitCharExpr(WACCParser.CharExprContext ctx) {
    return new CharType(ctx.CHAR_LITER().getText()[0]);
  }

  @Override
  public WACCType visitStrExpr(WACCParser.StrExprContext ctx) {
    return new StringType(ctx.STR_LITER().getText());
  }

  @Override
  public WACCType visitPairExpr(WACCParser.PairExprContext ctx) {
    ctx.
  }

  @Override
  public WACCType visitIdExpr(WACCParser.IdExprContext ctx) {
    // treat id as String, avoid creating a new class
    return new StringType(ctx.STR_LITER().getText());
  }

  @Override
  public WACCType visitArrayExpr(WACCParser.ArrayExprContext ctx) {
    return visitArray_elem(ctx.array_elem());
  }

  @Override
  public WACCType visitUnopExpr(WACCParser.UnopExprContext ctx) {
    String unop = ctx.uop.getText();
    WACCType type = visitChildren(ctx);
    switch (unop) {
      case "-":
        check(type, IntegerType.class);
      case "!":
        check(type, BoolType.class);
        return new BoolType(((BoolType) type).bVal);
      case "len":
        check(type, ArrayType.class);
        return new ArrayType();
      case "ord":
        check(type, CharType.class);
        return FuncType.ord(((CharType) type));
      case "chr":
        check(type, IntegerType.class);
        return FuncType.chr(((IntegerType) type));
    }
    System.err.println("fail to match unop" + unop + " in semantic check, error in parser or lexer");
    return null;
  }

  @Override
  public WACCType visitMulDivExpr(WACCParser.MulDivExprContext ctx) {

  }


  @Override
  public WACCType visitParenExpr(WACCParser.ParenExprContext ctx) {
    return visitChildren(ctx);
  }

  /**
   * visitors for array_elem */
  @Override
  public WACCType visitArray_elem(WACCParser.Array_elemContext ctx) {
    return super.visitArray_elem(ctx);
  }
  /**
   * visitors for array_liter */
}