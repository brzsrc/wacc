import antlr.WACCParser.*;
import antlr.WACCParserBaseVisitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import node.FuncNode;
import node.Node;
import node.ProgramNode;
import node.expr.ExprNode;
import node.stat.*;
import utils.ErrorHandler;
import utils.SymbolTable;

public class SemanticChecker extends WACCParserBaseVisitor<Node> {

  private Stack<SymbolTable> scopes = new Stack<>();
  private static ErrorHandler errorHandler = new ErrorHandler();

  @Override
  public Node visitProgram(ProgramContext ctx) {
    List<FuncNode> functions = new ArrayList<>();

    for (FuncContext f : ctx.func()) {
      FuncNode node = (FuncNode) visitFunc(f);
      if (!node.getFunctionBody().isLeaveAtEnd()) {
        //throw syntax exception
      }
      functions.add(node);
    }

    /* Main function has its own scope */
    scopes.push(new SymbolTable());
    StatNode body = (StatNode) visit(ctx.stat());
    scopes.pop();

    if (body.isHasReturn()) {
      //throw semantic exception
    }

    return new ProgramNode(functions, body);
  }

  @Override
  public Node visitFunc(FuncContext ctx) {

    /** Compilation error in this method body, so commented out
     *  Cause Reason: 1.FuncType not existed
     *                2.Return type of visit() method is Node not Type
     *  Please fix it later **/

    /* Each function has its own scopes */
    scopes.push(new SymbolTable());
    StatNode body = (StatNode) visit(ctx.stat());
    scopes.pop();

    /*

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

    */
    return null;
  }

  /******************************** StatNode Visitors *************************************/

  @Override
  public Node visitSeqStat(SeqStatContext ctx) {
    /* SeqNode actually does not need to use the scope field, so we do not set its scope field */
    return new SeqNode((StatNode) visit(ctx.stat(0)), (StatNode) visit(ctx.stat(1)));
  }

  @Override
  public Node visitIfStat(IfStatContext ctx) {

    //check the condition expr is bool or bool type at first

    scopes.push(scopes.peek().createScope());
    StatNode ifBody = (StatNode) visit(ctx.stat(0));
    scopes.pop();

    scopes.push(scopes.peek().createScope());
    StatNode elseBody = (StatNode) visit(ctx.stat(1));
    scopes.pop();

    StatNode node = new IfNode((ExprNode) visit(ctx.expr()), ifBody, elseBody);
    node.setScope(scopes.peek());

    return node;
  }

  @Override
  public Node visitWhileStat(WhileStatContext ctx) {

    //check the condition expr is bool or bool type at first

    scopes.push(scopes.peek().createScope());
    StatNode body = (StatNode) visit(ctx.stat());
    scopes.pop();

    StatNode node = new WhileNode((ExprNode) visit(ctx.expr()), body);
    node.setScope(scopes.peek());

    return node;
  }

  @Override
  public Node visitScopeStat(ScopeStatContext ctx) {

    scopes.push(scopes.peek().createScope());
    StatNode body = (StatNode) visit(ctx.stat());
    scopes.pop();

    /* ScopeNode actually does not need to use the scope field, so we do not set its scope field */
    return new ScopeNode(body);
  }

  @Override
  public Node visitReadStat(ReadStatContext ctx) {

    //check the type of the assign_lhs at first (not sure)
    StatNode node = new ReadNode((ExprNode) visit(ctx.assign_lhs()));
    node.setScope(scopes.peek());

    // Type targetType = visitAssign_lhs(ctx.assign_lhs());
    // if (!targetType.equalToType(new IntegerType())
    // && !targetType.equalToType(new CharType())) {
    //   throw new IllegalArgumentException("cannot read in type " + targetType.getTypeName());
    // }
    // todo: CFG
    return node;
  }

  @Override
  public Node visitPrintlnStat(PrintlnStatContext ctx) {

    //check the type of the expr at first (not sure)
    StatNode node = new PrintlnNode((ExprNode) visit(ctx.expr()));
    node.setScope(scopes.peek());

    // print statement does not need to check semantic, can print any expr
    // todo: CFG
    return node;
  }

  @Override
  public Node visitAssignStat(AssignStatContext ctx) {

    //check the type of the lhs, rhs and update the symbol table at first (not sure)
    StatNode node = new AssignNode((ExprNode) visit(ctx.assign_lhs()), (ExprNode) visit(ctx.assign_rhs()));
    node.setScope(scopes.peek());

    // todo: CFG

    return node;
  }

  @Override
  public Node visitPrintStat(PrintStatContext ctx) {

    //check the type of the expr at first (not sure)
    StatNode node = new PrintNode((ExprNode) visit(ctx.expr()));
    node.setScope(scopes.peek());

    // same as println
    // todo: CFG
    return node;
  }

  @Override
  public Node visitFreeStat(FreeStatContext ctx) {

    //check the type of the expr at first (not sure)
    StatNode node = new FreeNode((ExprNode) visit(ctx.expr()));
    node.setScope(scopes.peek());

    return node;
  }

  @Override
  public Node visitSkipStat(SkipStatContext ctx) {
    /* Skip actually does not need to use scope, so we do not set its scope field */
    return new SkipNode();
  }

  @Override
  public Node visitDeclareStat(DeclareStatContext ctx) {

    /** There are some problems with the identifier and type
     *  We may need to move the IDEN rule from lexer to parser
     *  cause now ctx.IDEN() is a terminal not a context,
     *  And we may need a WrapperTypeNode class to handle contain a Type field
     *  cause the visitType() method would return a Node **/

    //add new entry into the symbol table at first (not sure)
    StatNode node = new DeclareNode(null, null, (ExprNode) visit(ctx.assign_rhs()));
    node.setScope(scopes.peek());

    return node;
  }

  @Override
  public Node visitReturnStat(ReturnStatContext ctx) {

    //check the type of the expr at first (not sure)
    StatNode node = new ReturnNode((ExprNode) visit(ctx.expr()));
    node.setScope(scopes.peek());

    return node;
  }

  @Override
  public Node visitExitStat(ExitStatContext ctx) {

    //check the type of the expr at first (not sure)
    StatNode node = new ExitNode((ExprNode) visit(ctx.expr()));
    node.setScope(scopes.peek());

    return super.visitExitStat(ctx);
  }

  /************************ ExprNode(and all other nodes) Visitors *****************************/

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