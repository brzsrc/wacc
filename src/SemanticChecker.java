import antlr.WACCParser.*;
import antlr.WACCParserBaseVisitor;
import java.util.ArrayList;
import java.util.List;

import node.StatNode;
import node.stat.SeqNode;
import utils.IR.SymbolTable;
import utils.Type.*;

import static utils.Utils.check;

public class SemanticChecker extends WACCParserBaseVisitor<Type> {

  private static SymbolTable symbolTable = new SymbolTable();

  @Override
  public Type visitProgram(ProgramContext ctx) {

    visitChildren(ctx);

    // program is topmost node, return nothing in semantic check
    return null;
  }

  @Override
  public Type visitFunc(FuncContext ctx) {
    symbolTable.createNewScope();
    Type returnType = visitType(ctx.type());
    List<Type> param_list = new ArrayList();
    for (ParamContext param : ctx.param_list().param()) {
      Type param_type = visitType(param.type());
      param_list.add(param_type);
      symbolTable.add(param.IDENT().getText(), param_type);
    }
    //  intend to only visit stat list to examine every statement in function definition is correct,
    //  call visit child, and on visit param or type child, result is discarded
    symbolTable.add(ctx.IDENT().getText(), new FuncType(returnType, param_list));

    // todo: introduce another IR: Control Flow Graph, implementation similar to symbol table,
    //   let visitStat modify that graph, in order to check return/exit statement, and generate IR in one run through parser tree
    //   i.e: all visitStat functions needs further implementation
    visitChildren(ctx);
    symbolTable.backtraceScope();

    // no need to return, as function type does not need to match with any other type
    return null;
  }

  @Override
  public Type visitSeqStat(SeqStatContext ctx) {
    // todo: CFG
    SeqNode node = new SeqNode();

    for (StatContext s : ctx.stat()) {

      node.add((StatNode) visit(s));
    }

    return null;
//    return node;
  }

  @Override
  public Type visitReadStat(ReadStatContext ctx) {
    Type targetType = visitAssign_lhs(ctx.assign_lhs());
    if (!targetType.equalToType(new IntegerType())
    && !targetType.equalToType(new CharType())) {
      throw new IllegalArgumentException("cannot read in type " + targetType.getTypeName());
    }
    // todo: CFG
    return null;
  }

  @Override
  public Type visitPrintlnStat(PrintlnStatContext ctx) {
    // print statement does not need to check semantic, can print any expr
    // todo: CFG
    return null;
  }

  @Override
  public Type visitAssignStat(AssignStatContext ctx) {
    // todo: CFG

    return super.visitAssignStat(ctx);
  }

  @Override
  public Type visitPrintStat(PrintStatContext ctx) {
    // same as println
    // todo: CFG
    return null;
  }

  @Override
  public Type visitIfStat(IfStatContext ctx) {
    //check the condition expr is bool or bool type
    return null;
//    return new IfNode((ExprNode) visit(ctx.expr()),
//        (StatNode) visit(ctx.stat(0)), (StatNode) visit(ctx.stat(1)));
  }

  @Override
  public Type visitFreeStat(FreeStatContext ctx) {
    return super.visitFreeStat(ctx);
  }

  @Override
  public Type visitSkipStat(SkipStatContext ctx) {
    return super.visitSkipStat(ctx);
  }

  @Override
  public Type visitWhileStat(WhileStatContext ctx) {
    //check the condition expr is bool or bool type
    return null;
//    return new WhileNode((ExprNode) visit(ctx.expr()), (StatNode) visit(ctx.stat()));
  }

  @Override
  public Type visitScopeStat(ScopeStatContext ctx) {
    return null;
//    return new ScopeNode((StatNode) visit(ctx.stat()));
  }

  @Override
  public Type visitDelcarAssignStat(DelcarAssignStatContext ctx) {
    return super.visitDelcarAssignStat(ctx);
  }

  @Override
  public Type visitParenExpr(ParenExprContext ctx) {
    return super.visitParenExpr(ctx);
  }

  @Override
  public Type visitArray_elem(Array_elemContext ctx) {
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
  public Type visitPairExpr(PairExprContext ctx) {
    return super.visitPairExpr(ctx);
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
  public Type visitEqExpr(EqExprContext ctx) {
    // TODO Auto-generated method stub
    return super.visitEqExpr(ctx);
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
    return new PairType<>(
            visitPair_elem_type(ctx.pair_elem_type(0)),
            visitPair_elem_type(ctx.pair_elem_type(1)));
  }

  @Override
  public Type visitParam(ParamContext ctx) {
    return visitType(ctx.type());
  }

  @Override
  public Type visitReturnStat(ReturnStatContext ctx) {
    return super.visitReturnStat(ctx);
  }

  @Override
  public Type visitExitStat(ExitStatContext ctx) {
    return super.visitExitStat(ctx);
  }

  @Override
  public Type visitParam_list(Param_listContext ctx) {
    throw new IllegalAccessException("visitParam_list should never be accessed, see implementation of visitFunction")
  }

  @Override
  public Type visitPlusMinExpr(PlusMinExprContext ctx) {
    // TODO Auto-generated method stub
    return super.visitPlusMinExpr(ctx);
  }

  @Override
  public Type visitArrayExpr(ArrayExprContext ctx) {
    return super.visitArrayExpr(ctx);
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
  public Type visitBase_type(Base_typeContext ctx) {
    switch (ctx.getRuleIndex()) {
      case 10:
        return new IntegerType();
      case 11:
        return new BoolType();
      case 12:
        return new CharType();
      case 13:
        return new StringType();
      default:
        throw new IllegalArgumentException("invalid rule index in visitBase_type: " + ctx.getRuleIndex());
    }
  }

  @Override
  public Type visitUnopExpr(UnopExprContext ctx) {
    String unop = ctx.uop.getText();
    Type type = visitChildren(ctx);
    switch (unop) {
      case "-":
      case "chr":
        check(type, IntegerType.class);
        return new IntegerType();
      case "!":
        check(type, BoolType.class);
        return new BoolType();
      case "len":
        check(type, ArrayType.class);
        ArrayType array = ((ArrayType) type);
        return symbolTable.lookUpAll(array.getId());
      case "ord":
        check(type, CharType.class);
        return new CharType();
      default:
        throw new IllegalArgumentException("invalid unary operator in visitUnopExpr: " + unop);
    }

  }
  
}