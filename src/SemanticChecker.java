import antlr.WACCParser.*;
import antlr.WACCParserBaseVisitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import node.FuncNode;
import node.Node;
import node.ProgramNode;
import node.expr.ExprNode;
import node.expr.IdentNode;
import node.stat.*;
import type.Type;
import utils.ErrorHandler;
import utils.SymbolTable;
import type.BasicType;
import type.BasicTypeEnum;

public class SemanticChecker extends WACCParserBaseVisitor<Node> {

  /**
   * The errorHandler which will print useful semantic/syntatic error message
   */
  private static ErrorHandler errorHandler = new ErrorHandler();
  private SymbolTable currSymbolTable;

  public SemanticChecker() {
    currSymbolTable = null;
  }

  @Override
  public Node visitProgram(ProgramContext ctx) {
    /* a list of functions declared at the beginning of the program */
    List<FuncNode> functions = new ArrayList<>();

    for (FuncContext f : ctx.func()) {
      FuncNode funcNode = (FuncNode) visitFunc(f);
      String funcName = f.IDENT().getText();

      /* if the function declaration is not terminated with a return/exit statement, then throw the semantic error */
      if (!funcNode.getFunctionBody().leaveAtEnd()) {
        errorHandler.invalidFunctionReturnExit(f, funcName);
      }

      functions.add(funcNode);
    }

    currSymbolTable = new SymbolTable(currSymbolTable);
    StatNode body = (StatNode) visit(ctx.stat());
    currSymbolTable = currSymbolTable.getParentSymbolTable();

    return new ProgramNode(functions, body);
  }

  @Override
  public Node visitFunc(FuncContext ctx) {
    Type returnType = visitType(ctx.type());
    List<IdentNode> param_list = new ArrayList<>();
    for (ParamContext param : ctx.param_list().param()) {
      Type param_type = visitType(param.type());
      IdentNode paramNode = new IdentNode(param_type, param.IDENT().getText());
      param_list.add(paramNode);
    }

    StatNode functionBody = (StatNode) visitChildren(ctx);

    return new FuncNode(returnType, functionBody, param_list);
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
    currSymbolTable = new SymbolTable(currSymbolTable);
    StatNode ifBody = (StatNode) visit(ctx.stat(0));
    currSymbolTable = currSymbolTable.getParentSymbolTable();
    
    currSymbolTable = new SymbolTable(currSymbolTable);
    StatNode elseBody = (StatNode) visit(ctx.stat(1));
    currSymbolTable = currSymbolTable.getParentSymbolTable();

    StatNode node = new IfNode((ExprNode) visit(ctx.expr()), ifBody, elseBody);

    node.setScope(currSymbolTable);

    return node;
  }

  @Override
  public Node visitWhileStat(WhileStatContext ctx) {

    //check the condition expr is bool or bool type at first

    currSymbolTable = new SymbolTable(currSymbolTable);
    StatNode body = (StatNode) visit(ctx.stat());
    currSymbolTable = currSymbolTable.getParentSymbolTable();

    StatNode node = new WhileNode((ExprNode) visit(ctx.expr()), body);
    node.setScope(currSymbolTable);

    return node;
  }

  @Override
  public Node visitScopeStat(ScopeStatContext ctx) {

    currSymbolTable = new SymbolTable(currSymbolTable);
    StatNode body = (StatNode) visit(ctx.stat());
    currSymbolTable = currSymbolTable.getParentSymbolTable();

    /* ScopeNode actually does not need to use the scope field, so we do not set its scope field */
    return new ScopeNode(body);
  }

  @Override
  public Node visitReadStat(ReadStatContext ctx) {
    ReadNode node = new ReadNode((ExprNode) visitAssign_lhs(ctx.assign_lhs()));
    Type inputType = node.getInputExpr().getType(currSymbolTable);
    if (inputType.equalToType(new BasicType(BasicTypeEnum.STRING)) 
        && inputType.equalToType(new BasicType(BasicTypeEnum.INTEGER)) 
        && inputType.equalToType(new BasicType(BasicTypeEnum.CHAR))) {
      List<Type> allowedTypes = new ArrayList<>();
      allowedTypes.add(new BasicType(BasicTypeEnum.STRING));
      allowedTypes.add(new BasicType(BasicTypeEnum.INTEGER));
      allowedTypes.add(new BasicType(BasicTypeEnum.CHAR));
      errorHandler.typeMismatch(ctx, allowedTypes, inputType);
    }

    node.setScope(currSymbolTable);

    return node;
  }

  @Override
  public Node visitPrintlnStat(PrintlnStatContext ctx) {
    StatNode node = new PrintlnNode((ExprNode) visit(ctx.expr()));
    node.setScope(currSymbolTable);

    return node;
  }

  @Override
  public Node visitAssignStat(AssignStatContext ctx) {
    //check the type of the lhs, rhs and update the symbol table at first (not sure)
    StatNode node = new AssignNode((ExprNode) visit(ctx.assign_lhs()), (ExprNode) visit(ctx.assign_rhs()));
    node.setScope(currSymbolTable);

    return node;
  }

  @Override
  public Node visitPrintStat(PrintStatContext ctx) {
    StatNode node = new PrintNode((ExprNode) visit(ctx.expr()));
    node.setScope(currSymbolTable);

    return node;
  }

  @Override
  public Node visitFreeStat(FreeStatContext ctx) {

    //check the type of the expr at first (not sure)
    StatNode node = new FreeNode((ExprNode) visit(ctx.expr()));
    node.setScope(currSymbolTable);

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
    node.setScope(currSymbolTable);

    return node;
  }

  @Override
  public Node visitReturnStat(ReturnStatContext ctx) {

    //check the type of the expr at first (not sure)
    StatNode node = new ReturnNode((ExprNode) visit(ctx.expr()));
    node.setScope(currSymbolTable);

    return node;
  }

  @Override
  public Node visitExitStat(ExitStatContext ctx) {

    //check the type of the expr at first (not sure)
    StatNode node = new ExitNode((ExprNode) visit(ctx.expr()));
    node.setScope(currSymbolTable);

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
    return 
  }

  @Override
  public Node visitBase_type(Base_typeContext ctx) {
    switch (ctx.getRuleIndex()) {
      case 10:
        return new IntegerNode();
      case 11:
        return new BoolType();
      case 12:
        return new CharType();
      case 13:
        return new StringType();
      default:
        throw new IllegalArgumentException("invalid rule index in visitBase_type: " + ctx.getRuleIndex());
    }
    return null;
  }

  @Override
  public Node visitUnopExpr(UnopExprContext ctx) {
    String unop = ctx.uop.getText();
    ExprNode childExpr = (ExprNode) visitChildren(ctx);
    Unop operator;
    switch (unop) {
      case "-":
        operator = Unop.MINUS;
        check(childExpr.getType(), IntegerType.class);
        break;
      case "chr":
        operator = Unop.CHR;
        check(childExpr.getType(), IntegerType.class);
        break;
      case "!":
        operator = Unop.NOT;
        check(childExpr.getType(), BoolType.class);
        break;
      case "len":
        operator = Unop.LEN;
        check(childExpr.getType(), ArrayType.class);
        break;
      case "ord":
        operator = Unop.ORD;
        check(childExpr.getType(), CharType.class);
        break;
      default:
        throw new IllegalArgumentException("invalid unary operator in visitUnopExpr: " + unop);
    }
    return new UnopNode(childExpr, operator)
  }
  
}