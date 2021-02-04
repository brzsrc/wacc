import antlr.WACCParser;
import antlr.WACCParser.AndOrExprContext;
import antlr.WACCParser.Arg_listContext;
import antlr.WACCParser.Array_literContext;
import antlr.WACCParser.Array_typeContext;
import antlr.WACCParser.Assign_lhsContext;
import antlr.WACCParser.Assign_rhsContext;
import antlr.WACCParser.BoolExprContext;
import antlr.WACCParser.CharExprContext;
import antlr.WACCParser.CmpExprContext;
import antlr.WACCParser.End_statContext;
import antlr.WACCParser.EqExprContext;
import antlr.WACCParser.FuncContext;
import antlr.WACCParser.IdExprContext;
import antlr.WACCParser.IntExprContext;
import antlr.WACCParser.MulDivExprContext;
import antlr.WACCParser.Pair_elemContext;
import antlr.WACCParser.Pair_elem_typeContext;
import antlr.WACCParser.Pair_typeContext;
import antlr.WACCParser.ParamContext;
import antlr.WACCParser.Param_listContext;
import antlr.WACCParser.PlusMinExprContext;
import antlr.WACCParser.Stat_with_endContext;
import antlr.WACCParser.StrExprContext;
import antlr.WACCParser.TypeContext;
import antlr.WACCParser.UnopExprContext;
import antlr.WACCParserBaseVisitor;
import utils.SymbolTable;
import utils.Type.*;

import java.util.HashMap;
import java.util.Map;

import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

public class SemanticChecker extends WACCParserBaseVisitor<Type> {

  /* the current sysmbol table holded by the current node */
  private SymbolTable symbolTable = null;

  @Override
  public Type visitProgram(WACCParser.ProgramContext ctx) {
    /*
     * when visiting a new node, let the curent symbol table become the new node's
     * enclosing symTable
     */
    this.symbolTable = new SymbolTable(null);

    for (WACCParser.FuncContext funcCtx : ctx.func()) {
      visitFunc(funcCtx);
    }

    visitStat(ctx.stat());

    return null;
  }

  @Override
  public Type visitStat(WACCParser.StatContext ctx) {
    return null;
  }

  @Override
  public Type visitPairExpr(WACCParser.PairExprContext ctx) {
    return null;
  }

  @Override
  public Type visitArrayExpr(WACCParser.ArrayExprContext ctx) {
    return visitArray_elem(ctx.array_elem());
  }

  @Override
  public Type visitParenExpr(WACCParser.ParenExprContext ctx) {
    return visitChildren(ctx);
  }

  /**
   * visitors for array_elem
   */
  @Override
  public Type visitArray_elem(WACCParser.Array_elemContext ctx) {
    return super.visitArray_elem(ctx);
  }

  @Override
  public Type visitAndOrExpr(AndOrExprContext ctx) {
    // TODO Auto-generated method stub
    return super.visitAndOrExpr(ctx);
  }

  @Override
  public Type visitArg_list(Arg_listContext ctx) {
    // TODO Auto-generated method stub
    return super.visitArg_list(ctx);
  }

  @Override
  public Type visitArray_liter(Array_literContext ctx) {
    // TODO Auto-generated method stub
    return super.visitArray_liter(ctx);
  }

  @Override
  public Type visitArray_type(Array_typeContext ctx) {
    // TODO Auto-generated method stub
    return super.visitArray_type(ctx);
  }

  @Override
  public Type visitAssign_lhs(Assign_lhsContext ctx) {
    // TODO Auto-generated method stub
    return super.visitAssign_lhs(ctx);
  }

  @Override
  public Type visitAssign_rhs(Assign_rhsContext ctx) {
    // TODO Auto-generated method stub
    return super.visitAssign_rhs(ctx);
  }

  @Override
  public Type visitBoolExpr(BoolExprContext ctx) {
    // TODO Auto-generated method stub
    return super.visitBoolExpr(ctx);
  }

  @Override
  public Type visitCharExpr(CharExprContext ctx) {
    // TODO Auto-generated method stub
    return super.visitCharExpr(ctx);
  }

  @Override
  public Type visitCmpExpr(CmpExprContext ctx) {
    // TODO Auto-generated method stub
    return super.visitCmpExpr(ctx);
  }

  @Override
  public Type visitEnd_stat(End_statContext ctx) {
    // TODO Auto-generated method stub
    return super.visitEnd_stat(ctx);
  }

  @Override
  public Type visitEqExpr(EqExprContext ctx) {
    // TODO Auto-generated method stub
    return super.visitEqExpr(ctx);
  }

  @Override
  public Type visitFunc(FuncContext ctx) {
    // TODO Auto-generated method stub
    return super.visitFunc(ctx);
  }

  @Override
  public Type visitIdExpr(IdExprContext ctx) {
    // TODO Auto-generated method stub
    return super.visitIdExpr(ctx);
  }

  @Override
  public Type visitIntExpr(IntExprContext ctx) {
    // TODO Auto-generated method stub
    return super.visitIntExpr(ctx);
  }

  @Override
  public Type visitMulDivExpr(MulDivExprContext ctx) {
    // TODO Auto-generated method stub
    return super.visitMulDivExpr(ctx);
  }

  @Override
  public Type visitPair_elem(Pair_elemContext ctx) {
    // TODO Auto-generated method stub
    return super.visitPair_elem(ctx);
  }

  @Override
  public Type visitPair_elem_type(Pair_elem_typeContext ctx) {
    // TODO Auto-generated method stub
    return super.visitPair_elem_type(ctx);
  }

  @Override
  public Type visitPair_type(Pair_typeContext ctx) {
    // TODO Auto-generated method stub
    return super.visitPair_type(ctx);
  }

  @Override
  public Type visitParam(ParamContext ctx) {
    // TODO Auto-generated method stub
    return super.visitParam(ctx);
  }

  @Override
  public Type visitParam_list(Param_listContext ctx) {
    // TODO Auto-generated method stub
    return super.visitParam_list(ctx);
  }

  @Override
  public Type visitPlusMinExpr(PlusMinExprContext ctx) {
    // TODO Auto-generated method stub
    return super.visitPlusMinExpr(ctx);
  }

  @Override
  public Type visitStat_with_end(Stat_with_endContext ctx) {
    // TODO Auto-generated method stub
    return super.visitStat_with_end(ctx);
  }

  @Override
  public Type visitStrExpr(StrExprContext ctx) {
    // TODO Auto-generated method stub
    return super.visitStrExpr(ctx);
  }

  @Override
  public Type visitType(TypeContext ctx) {
    // TODO Auto-generated method stub
    return super.visitType(ctx);
  }

  @Override
  public Type visitUnopExpr(UnopExprContext ctx) {
    // TODO Auto-generated method stub
    return super.visitUnopExpr(ctx);
  }

  @Override
  protected Type aggregateResult(Type aggregate, Type nextResult) {
    // TODO Auto-generated method stub
    return super.aggregateResult(aggregate, nextResult);
  }

  @Override
  protected Type defaultResult() {
    // TODO Auto-generated method stub
    return super.defaultResult();
  }

  @Override
  protected boolean shouldVisitNextChild(RuleNode node, Type currentResult) {
    // TODO Auto-generated method stub
    return super.shouldVisitNextChild(node, currentResult);
  }

  @Override
  public Type visit(ParseTree tree) {
    // TODO Auto-generated method stub
    return super.visit(tree);
  }

  @Override
  public Type visitChildren(RuleNode arg0) {
    // TODO Auto-generated method stub
    return super.visitChildren(arg0);
  }

  @Override
  public Type visitErrorNode(ErrorNode node) {
    // TODO Auto-generated method stub
    return super.visitErrorNode(node);
  }

  @Override
  public Type visitTerminal(TerminalNode node) {
    // TODO Auto-generated method stub
    return super.visitTerminal(node);
  }
  
}