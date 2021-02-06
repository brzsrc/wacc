import antlr.WACCParser.*;
import antlr.WACCParserBaseVisitor;
import java.util.ArrayList;
import java.util.List;

import Node.Node;
import Node.Stat.*;
import Node.Expr.*;
import utils.ErrorHandler;
import utils.SymbolTable;
import Type.*;

import static utils.Utils.check;

public class SemanticChecker extends WACCParserBaseVisitor<Node> {

  private static SymbolTable symbolTable = new SymbolTable();
  private static ErrorHandler errorHandler = new ErrorHandler();

  @Override
  public Node visitProgram(ProgramContext ctx) {

    // visitChildren(ctx);

    // program is topmost node, return nothing in semantic check
    return null;
  }

  @Override
  public Node visitFunc(FuncContext ctx) {
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
  public Node visitSeqStat(SeqStatContext ctx) {
    // todo: CFG
    SeqNode node = new SeqNode();

    for (StatContext s : ctx.stat()) {

      node.add((StatNode) visit(s));
    }

    return null;
//    return node;
  }

  @Override
  public Node visitReadStat(ReadStatContext ctx) {
    // Type targetType = visitAssign_lhs(ctx.assign_lhs());
    // if (!targetType.equalToType(new IntegerType())
    // && !targetType.equalToType(new CharType())) {
    //   throw new IllegalArgumentException("cannot read in type " + targetType.getTypeName());
    // }
    // todo: CFG
    return null;
  }

  @Override
  public Node visitPrintlnStat(PrintlnStatContext ctx) {
    // print statement does not need to check semantic, can print any expr
    // todo: CFG
    return null;
  }

  @Override
  public Node visitAssignStat(AssignStatContext ctx) {
    // todo: CFG

    return super.visitAssignStat(ctx);
  }

  @Override
  public Node visitPrintStat(PrintStatContext ctx) {
    // same as println
    // todo: CFG
    return null;
  }

  @Override
  public Node visitIfStat(IfStatContext ctx) {
    //check the condition expr is bool or bool type
    return null;
//    return new IfNode((ExprNode) visit(ctx.expr()),
//        (StatNode) visit(ctx.stat(0)), (StatNode) visit(ctx.stat(1)));
  }

  @Override
  public Node visitFreeStat(FreeStatContext ctx) {
    return super.visitFreeStat(ctx);
  }

  @Override
  public Node visitSkipStat(SkipStatContext ctx) {
    return super.visitSkipStat(ctx);
  }

  @Override
  public Node visitWhileStat(WhileStatContext ctx) {
    //check the condition expr is bool or bool type
    return null;
//    return new WhileNode((ExprNode) visit(ctx.expr()), (StatNode) visit(ctx.stat()));
  }

  @Override
  public Node visitScopeStat(ScopeStatContext ctx) {
    return null;
//    return new ScopeNode((StatNode) visit(ctx.stat()));
  }

  @Override
  public Node visitDelcarAssignStat(DelcarAssignStatContext ctx) {
    return super.visitDelcarAssignStat(ctx);
  }

  @Override
  public Node visitParenExpr(ParenExprContext ctx) {
    return super.visitParenExpr(ctx);
  }

  @Override
  public Node visitArray_elem(Array_elemContext ctx) {
    return super.visitArray_elem(ctx);
  }

  @Override
  public Node visitAndOrExpr(AndOrExprContext ctx) {
    // TODO Auto-generated method stub
    return super.visitAndOrExpr(ctx);
  }

  @Override
  public Node visitArg_list(Arg_listContext ctx) {
    // TODO Auto-generated method stub
    return super.visitArg_list(ctx);
  }

  @Override
  public Node visitArray_liter(Array_literContext ctx) {
    // TODO Auto-generated method stub
    return super.visitArray_liter(ctx);
  }

  @Override
  public Node visitArray_type(Array_typeContext ctx) {
    // TODO Auto-generated method stub
    return super.visitArray_type(ctx);
  }

  @Override
  public Node visitAssign_lhs(Assign_lhsContext ctx) {
    // TODO Auto-generated method stub
    return super.visitAssign_lhs(ctx);
  }

  @Override
  public Node visitAssign_rhs(Assign_rhsContext ctx) {
    // TODO Auto-generated method stub
    return super.visitAssign_rhs(ctx);
  }

  @Override
  public Node visitBoolExpr(BoolExprContext ctx) {
    // TODO Auto-generated method stub
    return super.visitBoolExpr(ctx);
  }

  @Override
  public Node visitPairExpr(PairExprContext ctx) {
    return super.visitPairExpr(ctx);
  }

  @Override
  public Node visitCharExpr(CharExprContext ctx) {
    // TODO Auto-generated method stub
    return super.visitCharExpr(ctx);
  }

  @Override
  public Node visitCmpExpr(CmpExprContext ctx) {
    // TODO Auto-generated method stub
    return super.visitCmpExpr(ctx);
  }

  @Override
  public Node visitEqExpr(EqExprContext ctx) {
    // TODO Auto-generated method stub
    return super.visitEqExpr(ctx);
  }

  @Override
  public Node visitIdExpr(IdExprContext ctx) {
    // TODO Auto-generated method stub
    return super.visitIdExpr(ctx);
  }

  @Override
  public Node visitIntExpr(IntExprContext ctx) {
    // TODO Auto-generated method stub
    return super.visitIntExpr(ctx);
  }

  @Override
  public Node visitMulDivExpr(MulDivExprContext ctx) {
    // TODO Auto-generated method stub
    return super.visitMulDivExpr(ctx);
  }

  @Override
  public Node visitPair_elem(Pair_elemContext ctx) {
    // TODO Auto-generated method stub
    return super.visitPair_elem(ctx);
  }

  @Override
  public Node visitPair_elem_type(Pair_elem_typeContext ctx) {
    // TODO Auto-generated method stub
    return super.visitPair_elem_type(ctx);
  }

  @Override
  public Node visitPair_type(Pair_typeContext ctx) {
    // return new PairType<>(
    //         visitPair_elem_type(ctx.pair_elem_type(0)),
    //         visitPair_elem_type(ctx.pair_elem_type(1)));
    return null;
  }

  @Override
  public Node visitParam(ParamContext ctx) {
    return visitType(ctx.type());
  }

  @Override
  public Node visitReturnStat(ReturnStatContext ctx) {
    return super.visitReturnStat(ctx);
  }

  @Override
  public Node visitExitStat(ExitStatContext ctx) {
    return super.visitExitStat(ctx);
  }

  @Override
  public Node visitParam_list(Param_listContext ctx) {
    // throw new IllegalAccessException("visitParam_list should never be accessed, see implementation of visitFunction");
    return null;
  }

  @Override
  public Node visitPlusMinExpr(PlusMinExprContext ctx) {
    // TODO Auto-generated method stub
    return super.visitPlusMinExpr(ctx);
  }

  @Override
  public Node visitArrayExpr(ArrayExprContext ctx) {
    return super.visitArrayExpr(ctx);
  }

  @Override
  public Node visitStrExpr(StrExprContext ctx) {
    // TODO Auto-generated method stub
    return super.visitStrExpr(ctx);
  }

  @Override
  public Node visitType(TypeContext ctx) {
    // TODO Auto-generated method stub
    return super.visitType(ctx);
  }

  @Override
  public Node visitBase_type(Base_typeContext ctx) {
    // switch (ctx.getRuleIndex()) {
    //   case 10:
    //     return new IntegerType();
    //   case 11:
    //     return new BoolType();
    //   case 12:
    //     return new CharType();
    //   case 13:
    //     return new StringType();
    //   default:
    //     throw new IllegalArgumentException("invalid rule index in visitBase_type: " + ctx.getRuleIndex());
    // }
    return null;
  }

  @Override
  public Node visitUnopExpr(UnopExprContext ctx) {
    // String unop = ctx.uop.getText();
    // Type type = visitChildren(ctx);
    // switch (unop) {
    //   case "-":
    //   case "chr":
    //     check(type, IntegerType.class);
    //     return new IntegerType();
    //   case "!":
    //     check(type, BoolType.class);
    //     return new BoolType();
    //   case "len":
    //     check(type, ArrayType.class);
    //     ArrayType array = ((ArrayType) type);
    //     // return symbolTable.lookUpAll(array.getId());
    //   case "ord":
    //     check(type, CharType.class);
    //     return new CharType();
    //   default:
    //     throw new IllegalArgumentException("invalid unary operator in visitUnopExpr: " + unop);
    // }
    return null;
  }
  
}