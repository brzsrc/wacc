import antlr.WACCParser;
import antlr.WACCParser.*;
import antlr.WACCParserBaseVisitor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import com.sun.jdi.IntegerType;
import node.FuncNode;
import node.Node;
import node.ProgramNode;
import node.TypeDeclareNode;
import node.expr.*;
import node.stat.*;
import type.ArrayType;
import type.BasicType;
import type.BasicTypeEnum;
import type.Type;
import utils.ErrorHandler;
import utils.SymbolTable;

public class SemanticChecker extends WACCParserBaseVisitor<Node> {

  /** 
   * Implement the scope by storing a stack of SymbolTables, 
   *  when the current scope exits, it will be popped off from the stack
   *  when a new scope is created, it will be added to the stack
   */
  private SymbolTable scopes = new SymbolTable();

  /**
   * The errorHandler which will print useful semantic/syntatic error message
   */
  private static ErrorHandler errorHandler = new ErrorHandler();

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

    /* Main function has its own scope */
    scopes.push(new SymbolTable());
    StatNode body = (StatNode) visit(ctx.stat());
    scopes.pop();

    return new ProgramNode(functions, body);
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

    ArrayNode array = scopes.lookupAll(ctx.IDENT().getText());
    // check that index depth is less than the array depth
    int indexDepth = ctx.expr().size();
    int arrayMaxDepth = 1;
    while (array.getElem(0).getType(scopes) instanceof ArrayType) {
        arrayMaxDepth++;
    }
    if (arrayMaxDepth < indexDepth) {
      errorHandler.invalidArrayIndexError("array index more than depth the array is declared");
    }

    List<ExprNode> indexList = new ArrayList<>();

    for (ExprContext index_ : ctx.expr()) {
      ExprNode index = visit(index_);
      // check every expr can evaluate to integer
      if (index.getType(scopes).equalToType(new BasicType(BasicTypeEnum.INTEGER))) {
        errorHandler.typeMismatch(ctx, index.getType(scopes), new BasicType(BasicTypeEnum.INTEGER));
      }

      indexList.add(index);
    }
    return new ArrayElemNode("", array, indexList);
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
    int length = ctx.expr().size();
    if (length == 0) {
      // contentType SHOULD not ever be used, if length is 0
      return new ArrayNode(null, new ArrayList<>(), length);
    }
    ExprNode firstExpr = visit(ctx.expr(0));
    Type firstContentType = firstExpr.getType(scopes);
    List<ExprNode> list = new ArrayList<>();
    for (ExprContext context : ctx.expr()) {
      ExprNode expr = visit(context);
      if (firstContentType.equalToType(expr.getType(scopes))) {
        errorHandler.typeMismatch(ctx, firstContentType, expr.getType(scopes));
      }

      list.add(expr);
    }
    return new ArrayNode(firstContentType, list, length);
  }

  @Override
  public Node visitArray_type(Array_typeContext ctx) {
    ExprNode type = visitChildren(ctx);
    return new TypeDeclareNode(new ArrayType(type.getType(scopes)));
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
    if (ctx.getRuleIndex()) {

    }
    return ctx.;
  }

  @Override
  public Node visitPair_elem_type(Pair_elem_typeContext ctx) {
    // todo
    return null;
  }

  @Override
  public Node visitPair_type(Pair_typeContext ctx) {
    ExprNode leftChild = (ExprNode) visitPair_elem_type(ctx.pair_elem_type(0));
    ExprNode rightChild = (ExprNode) visitPair_elem_type(ctx.pair_elem_type(1));
    return new PairNode(leftChild, rightChild);
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
    return visitChildren(ctx);
  }

  @Override
  public Node visitBase_type(Base_typeContext ctx) {
    switch (ctx.getRuleIndex()) {
      case 10:
        return new TypeDeclareNode(new BasicType(BasicTypeEnum.INTEGER));
      case 11:
        return new TypeDeclareNode(new BasicType(BasicTypeEnum.BOOLEAN));
      case 12:
        return new TypeDeclareNode(new BasicType(BasicTypeEnum.CHAR));
      case 13:
        return new TypeDeclareNode(new BasicType(BasicTypeEnum.STRING));
      default:
        throw new IllegalArgumentException("invalid rule index in visitBase_type: " + ctx.getRuleIndex());
    }
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