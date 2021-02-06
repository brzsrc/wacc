import antlr.WACCParser.*;
import antlr.WACCParserBaseVisitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.antlr.v4.runtime.ParserRuleContext;

import node.FuncNode;
import node.Node;
import node.ProgramNode;
import node.expr.BinopNode;
import node.expr.BoolNode;
import node.expr.CharNode;
import node.expr.ExprNode;
import node.expr.IdentNode;
import node.expr.IntegerNode;
import node.expr.PairNode;
import node.expr.StringNode;
import node.expr.UnopNode;
import node.expr.BinopNode.Binops;
import node.expr.UnopNode.Unop;
import node.stat.*;
import type.Type;
import type.BasicType;
import type.BasicTypeEnum;
import utils.ErrorHandler;
import utils.SymbolTable;
import static utils.Utils.check;

public class SemanticChecker extends WACCParserBaseVisitor<Node> {

  /** 
   * Implement the scope by storing a stack of SymbolTables, 
   *  when the current scope exits, it will be popped off from the stack
   *  when a new scope is created, it will be added to the stack
   */
  private Stack<SymbolTable> scopes = new Stack<>();

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
    return super.visitArray_elem(ctx);
  }

  @Override
  public Node visitAndOrExpr(AndOrExprContext ctx) {
    String bop = ctx.bop.getText();
    Binops binop;
    if(bop.equals("&&")) {
      binop = Binops.AND;
    } else if(bop.equals("||")) {
      binop = Binops.OR;
    } else {
      throw new IllegalArgumentException("invalid unary operator in visitUnopExpr: " + bop);
    }
    ExprNode expr1 = (ExprNode)visit(ctx.expr(0));
    ExprNode expr2 = (ExprNode)visit(ctx.expr(1));
    SymbolTable sTable = scopes.peek();
    BasicType boolType = new BasicType(BasicTypeEnum.BOOLEAN);

    if(!expr1.getType(sTable).equalToType(boolType)) {
      errorHandler.typeMismatch(ctx.expr(0), expr1.getType(sTable), boolType);
    } else if(expr2.getType(sTable).equalToType(boolType)) {
      errorHandler.typeMismatch(ctx.expr(1), expr1.getType(sTable), boolType);
    }
    return new BinopNode(expr1, expr2, binop);
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
    String bool = ctx.BOOL_LITER().getText();
    boolean boolVal = bool.equals("true");
    return new BoolNode(boolVal);
  }

  @Override
  public Node visitPairExpr(PairExprContext ctx) {
    return new PairNode(null, null);
  }

  @Override
  public Node visitCharExpr(CharExprContext ctx) {
    return new CharNode(ctx.CHAR_LITER().getText().charAt(0));
  }

  @Override
  public Node visitCmpExpr(CmpExprContext ctx) {
    String bop = ctx.bop.getText();
    Binops binop;
    switch(bop) {
      case ">":
        binop = Binops.GREATER;
        break;
      case ">=":
        binop = Binops.GREATER_EQUAL;
        break;
      case "<":
        binop = Binops.LESS;
        break;
      case "<=":
        binop = Binops.LESS_EQUAL;
        break;
      default:
        throw new IllegalArgumentException("invalid unary operator in visitUnopExpr: " + bop);
    }

    ExprNode expr1 = (ExprNode)visit(ctx.expr(0));
    ExprNode expr2 = (ExprNode)visit(ctx.expr(1));
    SymbolTable sTable = scopes.peek();
    Type expr1Type = expr1.getType(sTable);
    Type expr2Type = expr2.getType(sTable);
    BasicType intType = new BasicType(BasicTypeEnum.INTEGER);
    BasicType chrType = new BasicType(BasicTypeEnum.CHAR);
    BasicType strType = new BasicType(BasicTypeEnum.STRING);
    
    if(!expr1Type.equalToType(intType) || !expr1Type.equalToType(chrType) 
        || !expr1Type.equalToType(strType)) {
      errorHandler.typeMismatch(ctx.expr(0), expr1Type, );
    } else if() {

    }

    return new BinopNode(expr1, expr2, binop);
  }

  @Override
  public Node visitEqExpr(EqExprContext ctx) {
    String bop = ctx.bop.getText();
    Binops binop;
    switch(bop) {
      case "=":
        binop = Binops.EQUAL;
        break;
      case "!=":
        binop = Binops.UNEQUAL;
        break;
      default:
        throw new IllegalArgumentException("invalid unary operator in visitUnopExpr: " + bop);
    }
    ExprNode expr1 = (ExprNode)visit(ctx.expr(0));
    ExprNode expr2 = (ExprNode)visit(ctx.expr(1));
    SymbolTable sTable = scopes.peek();
    Type expr1Type = expr1.getType(sTable);
    Type expr2Type = expr2.getType(sTable);
    BasicType intType = new BasicType(BasicTypeEnum.INTEGER);
    BasicType chrType = new BasicType(BasicTypeEnum.CHAR);
    BasicType strType = new BasicType(BasicTypeEnum.STRING);
    BasicType boolType = new BasicType(BasicTypeEnum.BOOLEAN);
    
    if(!expr1Type.equalToType(intType) || !expr1Type.equalToType(chrType) 
        || !expr1Type.equalToType(strType)) {
      errorHandler.typeMismatch(ctx.expr(0), expr1Type, );
    } else if() {

    }

    return new BinopNode(expr1, expr2, binop);
  }

  @Override
  public Node visitIdExpr(IdExprContext ctx) {
    return new IdentNode(ctx.IDENT().getText());
  }

  @Override
  public Node visitIntExpr(IntExprContext ctx) {
    int integer = Integer.parseInt(ctx.INT_LITER().getText());
    return new IntegerNode(integer);
  }

  @Override
  public Node visitMulDivExpr(MulDivExprContext ctx) {
    String bop = ctx.bop.getText();
    Binops binop;
    switch(bop) {
      case "*":
        binop = Binops.MUL;
        break;
      case "/":
        binop = Binops.DIV;
        break;
      case "%":
        binop = Binops.MOD;
        break;
      default:
        throw new IllegalArgumentException("invalid unary operator in visitUnopExpr: " + bop);
    }
    ExprNode expr1 = (ExprNode)visit(ctx.expr(0));
    ExprNode expr2 = (ExprNode)visit(ctx.expr(1));
    SymbolTable sTable = scopes.peek();
    Type expr1Type = expr1.getType(sTable);
    Type expr2Type = expr2.getType(sTable);
    BasicType intType = new BasicType(BasicTypeEnum.INTEGER);
    
    if(!expr1Type.equalToType(intType)) {
      errorHandler.typeMismatch(ctx.expr(0), expr1Type, intType);
    } else if(!expr2Type.equalToType(intType)) {
      errorHandler.typeMismatch(ctx.expr(1), expr2Type, intType);
    }

    return new BinopNode(expr1, expr2, binop);
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
    String bop = ctx.bop.getText();
    Binops binop;
    switch(bop) {
      case "+":
        binop = Binops.MUL;
        break;
      case "-":
        binop = Binops.DIV;
        break;
      default:
        throw new IllegalArgumentException("invalid unary operator in visitUnopExpr: " + bop);
    }
    ExprNode expr1 = (ExprNode)visit(ctx.expr(0));
    ExprNode expr2 = (ExprNode)visit(ctx.expr(1));
    SymbolTable sTable = scopes.peek();
    Type expr1Type = expr1.getType(sTable);
    Type expr2Type = expr2.getType(sTable);
    BasicType intType = new BasicType(BasicTypeEnum.INTEGER);
    
    if(!expr1Type.equalToType(intType)) {
      errorHandler.typeMismatch(ctx.expr(0), expr1Type, intType);
    } else if(!expr2Type.equalToType(intType)) {
      errorHandler.typeMismatch(ctx.expr(1), expr2Type, intType);
    }

    return new BinopNode(expr1, expr2, binop);
  }

  @Override
  public Node visitArrayExpr(ArrayExprContext ctx) {
    return ctx.array_elem();
  }

  @Override
  public Node visitStrExpr(StrExprContext ctx) {
    return new StringNode(ctx.STR_LITER().getText());
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
    String uop = ctx.uop.getText();
    ExprNode childExpr = (ExprNode) visitChildren(ctx);
    Unop unop;
    switch (uop) {
      case "-":
        unop = Unop.MINUS;
        break;
      case "chr":
        unop = Unop.CHR;
        break;
      case "!":
        unop = Unop.NOT;
        break;
      case "len":
        unop = Unop.LEN;
        break;
      case "ord":
        unop = Unop.ORD;
        break;
      default:
        throw new IllegalArgumentException("invalid unary operator in visitUnopExpr: " + uop);
    }
  
    ExprNode expr = (ExprNode)visit(ctx.expr());
    SymbolTable sTable = scopes.peek();
    Type exprType = expr.getType(sTable);
    BasicType intType = new BasicType(BasicTypeEnum.INTEGER);
    BasicType chrType = new BasicType(BasicTypeEnum.CHAR);
    BasicType strType = new BasicType(BasicTypeEnum.STRING);
    BasicType boolType = new BasicType(BasicTypeEnum.BOOLEAN);
    
    if(!exprType.equalToType(intType) || !exprType.equalToType(chrType) 
        || !exprType.equalToType(strType)) {
      errorHandler.typeMismatch(ctx.expr(), exprType, );
    } else if() {

    }

    return new UnopNode(expr, unop);
  }
  
}