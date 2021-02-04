import antlr.WACCParser;
import antlr.WACCParserBaseVisitor;
import utils.ExprTypes;
import utils.TypeSystem.*;
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

  /**
   * visitors for expr */
  @Override
  public TypeSystem visitIntExpr(WACCParser.IntExprContext ctx) {
    int val = 0;
    try {
      val = Integer.parseInt(ctx.INT_LITER().getText());
    } catch ( NumberFormatException e) {
      System.err.println("bad format of integer " + ctx.INT_LITER().getText());
    }
    return new IntegerType(val);
  }

  @Override
  public TypeSystem visitBoolExpr(WACCParser.BoolExprContext ctx) {
    boolean bVal;
    String text = ctx.BOOL_LITER().getText();
    assert (text.equals("true") || text.equals("false"));
    bVal = text.equals("true");
    return new BoolType(bVal);
  }

  @Override
  public TypeSystem visitCharExpr(WACCParser.CharExprContext ctx) {
    return new CharType(ctx.CHAR_LITER().getText()[0]);
  }

  @Override
  public TypeSystem visitStrExpr(WACCParser.StrExprContext ctx) {
    return new StringType(ctx.STR_LITER().getText());
  }

  @Override
  public TypeSystem visitPairExpr(WACCParser.PairExprContext ctx) {
    // todo: unimplemented
  }

  @Override
  public TypeSystem visitIdExpr(WACCParser.IdExprContext ctx) {
    // treat id as String, avoid creating a new class
    return new StringType(ctx.STR_LITER().getText());
  }

  @Override
  public TypeSystem visitArrayExpr(WACCParser.ArrayExprContext ctx) {
    return visitArray_elem(ctx.array_elem());
  }

  @Override
  public TypeSystem visitUnopExpr(WACCParser.UnopExprContext ctx) {
    String unop = ctx.uop.getText();
    TypeSystem type = visitChildren(ctx);
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
  public TypeSystem visitMulDivExpr(WACCParser.MulDivExprContext ctx) {

  }


  @Override
  public TypeSystem visitParenExpr(WACCParser.ParenExprContext ctx) {
    return visitChildren(ctx);
  }

  /**
   * visitors for array_elem */
  @Override
  public TypeSystem visitArray_elem(WACCParser.Array_elemContext ctx) {
    return super.visitArray_elem(ctx);
  }
  /**
   * visitors for array_liter */
}