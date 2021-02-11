import antlr.WACCParser.*;
import antlr.WACCParserBaseVisitor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import node.FuncNode;
import node.Node;
import node.ProgramNode;
import node.TypeDeclareNode;
import node.expr.*;
import node.stat.*;
import node.expr.BinopNode.Binops;
import node.expr.UnopNode.Unop;

import type.*;
import utils.SemanticErrorHandler;
import utils.SymbolTable;
import org.antlr.v4.runtime.ParserRuleContext;

public class SemanticChecker extends WACCParserBaseVisitor<Node> {

  /**
   * SemanticChecker will not only check the semantics of the provided .wacc file, but also
   * generate an internal representation of the program using ExprNode and StatNode. This will
   * aid the process of code generation in the backend
   */

  /* Type classes to represent BasicType, ArrayType, and PairType, used in type comparisons throughout the SemanticChecker */
  private static final Type INT_BASIC_TYPE = new BasicType(BasicTypeEnum.INTEGER);
  private static final Type BOOL_BASIC_TYPE = new BasicType(BasicTypeEnum.BOOLEAN);
  private static final Type CHAR_BASIC_TYPE = new BasicType(BasicTypeEnum.CHAR);
  private static final Type STRING_BASIC_TYPE = new BasicType(BasicTypeEnum.STRING);
  private static final Type ARRAY_TYPE = new ArrayType();
  private static final Type PAIR_TYPE = new PairType();

  /* a list of allowed types in read, free, cmp statement */
  private static final Set<Type> readStatAllowedTypes = Set.of(STRING_BASIC_TYPE, INT_BASIC_TYPE, CHAR_BASIC_TYPE);
  private static final Set<Type> freeStatAllowedTypes = Set.of(ARRAY_TYPE, PAIR_TYPE);
  private static final Set<Type> cmpStatAllowedTypes = Set.of(STRING_BASIC_TYPE, INT_BASIC_TYPE, CHAR_BASIC_TYPE);

  /* recording the current SymbolTable during parser tree visits */
  private SymbolTable currSymbolTable;

  /* global function table, used to record all functions */
  private Map<String, FuncNode> globalFuncTable;

  /* used after function declare step, to detect RETURN statement in main body */
  private boolean isMainFunction;

  /* used only in function declare step, to check function has the correct return type */
  private Type expectedFunctionReturn;

  /* constructor of SemanticChecker */
  public SemanticChecker() {
    currSymbolTable = null;
    globalFuncTable = new HashMap<>();
    isMainFunction = false;
  }

  @Override
  public Node visitProgram(ProgramContext ctx) {
    /* add the identifiers and parameter list of functions in the globalFuncTable first */
    for (FuncContext f : ctx.func()) {
      String funcName = f.IDENT().getText();

      /* check if the function is defined already */
      if (globalFuncTable.containsKey(funcName)) {
        SemanticErrorHandler.symbolRedeclared(ctx, funcName);
      }

      /* get the return type of the function */
      Type returnType = visitType(f.type()).asTypeDeclareNode().getType();
      /* store the parameters in a list of IdentNode */
      List<IdentNode> param_list = new ArrayList<>();

      if (f.param_list() != null) {
        for (ParamContext param : f.param_list().param()) {
          Type param_type = visitType(param.type()).asTypeDeclareNode().getType();
          IdentNode paramNode = new IdentNode(param_type, param.IDENT().getText());
          param_list.add(paramNode);
        }
      }

      globalFuncTable.put(funcName, new FuncNode(returnType, param_list));
    }
    
    /* then iterate through a list of function declarations to visit the function body */
    for (FuncContext f : ctx.func()) {
      String funcName = f.IDENT().getText();

      StatNode functionBody = visitFunc(f).asStatNode();

      /* if the function declaration is not terminated with a return/exit statement, then throw the semantic error */
      if (!functionBody.leaveAtEnd()) {
        SemanticErrorHandler.invalidFunctionReturnExit(ctx, funcName);
      }

      globalFuncTable.get(funcName).setFunctionBody(functionBody);
    }

    /* visit the body of the program and create the root SymbolTable here */
    isMainFunction = true;
    currSymbolTable = new SymbolTable(currSymbolTable);
    StatNode body = visit(ctx.stat()).asStatNode();
    body.setScope(currSymbolTable);
    currSymbolTable = currSymbolTable.getParentSymbolTable();

    if (!(body instanceof ScopeNode)) {
      return new ProgramNode(globalFuncTable, new ScopeNode(body));
    }
    return new ProgramNode(globalFuncTable, body);
  }

  @Override
  public Node visitFunc(FuncContext ctx) {

    /**
     * visitFunc() will only handle the declaration of functions and generate corresponding
     * FuncNode. It will not process the calling of the functions
     */

    FuncNode funcNode = globalFuncTable.get(ctx.IDENT().getText());

    /* visit the function body */
    expectedFunctionReturn = funcNode.getReturnType();
    currSymbolTable = new SymbolTable(currSymbolTable);
    funcNode.getParamList().stream().forEach(i -> currSymbolTable.add(i.getName(), i));
    StatNode functionBody = visit(ctx.stat()).asStatNode();
    functionBody.setScope(currSymbolTable);
    currSymbolTable = currSymbolTable.getParentSymbolTable();

    return functionBody;
  }

  /******************************** StatNode Visitors *************************************/
  /** all following function's return type are statNode */

  @Override
  public Node visitSeqStat(SeqStatContext ctx) {
    StatNode before = visit(ctx.stat(0)).asStatNode();
    StatNode after = visit(ctx.stat(1)).asStatNode();
    if (!isMainFunction && before.leaveAtEnd()) {
      SemanticErrorHandler.functionJunkAfterReturn(ctx);
    }

    StatNode node = new ScopeNode(before, after);

    /* ensure all statNode has scope not null */
    node.setScope(currSymbolTable);
    return node;
  }

  @Override
  public Node visitIfStat(IfStatContext ctx) {
    /* check that the condition of if statement is of type boolean */
    ExprNode condition = visit(ctx.expr()).asExprNode();
    Type conditionType = condition.getType();
    typeCheck(ctx.expr(), BOOL_BASIC_TYPE, conditionType);
    
    /* create the StatNode for the if body and gegerate new child scope */
    currSymbolTable = new SymbolTable(currSymbolTable);
    StatNode ifBody = visit(ctx.stat(0)).asStatNode();
    currSymbolTable = currSymbolTable.getParentSymbolTable();
    
    /* create the StatNode for the else body and generate new child scope */
    currSymbolTable = new SymbolTable(currSymbolTable);
    StatNode elseBody = visit(ctx.stat(1)).asStatNode();
    currSymbolTable = currSymbolTable.getParentSymbolTable();

    StatNode node = new IfNode(condition, new ScopeNode(ifBody), new ScopeNode(elseBody));

    node.setScope(currSymbolTable);

    return node;
  }

  @Override
  public Node visitWhileStat(WhileStatContext ctx) {
    /* check that the condition of while statement is of type boolean */
    ExprNode condition = visit(ctx.expr()).asExprNode();
    Type conditionType = condition.getType();
    typeCheck(ctx.expr(), BOOL_BASIC_TYPE, conditionType);    

    /* get the StatNode of the execution body of while loop */
    currSymbolTable = new SymbolTable(currSymbolTable);
    StatNode body = visit(ctx.stat()).asStatNode();
    currSymbolTable = currSymbolTable.getParentSymbolTable();

    StatNode node = new WhileNode(visit(ctx.expr()).asExprNode(), new ScopeNode(body));
    node.setScope(currSymbolTable);

    return node;
  }

  @Override
  public Node visitScopeStat(ScopeStatContext ctx) {
    /* simply create a new SymbolTable to represent a BEGIN ... END statement */
    currSymbolTable = new SymbolTable(currSymbolTable);
    StatNode body = visit(ctx.stat()).asStatNode();
    ScopeNode scopeNode = new ScopeNode(body);
    scopeNode.setScope(currSymbolTable);
    currSymbolTable = currSymbolTable.getParentSymbolTable();

    return scopeNode;
  }

  @Override
  public Node visitReadStat(ReadStatContext ctx) {
    ExprNode exprNode = visitAssign_lhs(ctx.assign_lhs()).asExprNode();
    if (exprNode != null) {
      Type inputType = exprNode.getType();
      typeCheck(ctx.assign_lhs(), readStatAllowedTypes, inputType);
    }

    ReadNode readNode = new ReadNode(exprNode);

    readNode.setScope(currSymbolTable);

    return readNode;
  }

  @Override
  public Node visitPrintStat(PrintStatContext ctx) {
    ExprNode printContent = visit(ctx.expr()).asExprNode();
    /* no restriction on type to be printed, all type can be printed */

    StatNode node = new PrintNode(printContent);
    node.setScope(currSymbolTable);

    return node;
  }

  @Override
  public Node visitPrintlnStat(PrintlnStatContext ctx) {
    ExprNode printContent = visit(ctx.expr()).asExprNode();
    /* no restriction on type to be printed, all type can be printed */

    StatNode node = new PrintlnNode(printContent);
    node.setScope(currSymbolTable);

    return node;
  }

  @Override
  public Node visitAssignStat(AssignStatContext ctx) {
    
    /* check if the type of lhs and rhs are equal */
    ExprNode lhs = visitAssign_lhs(ctx.assign_lhs()).asExprNode();
    ExprNode rhs = visitAssign_rhs(ctx.assign_rhs()).asExprNode();

    if (rhs != null && lhs != null) {
      Type lhsType = lhs.getType();
      Type rhsType = rhs.getType();

      typeCheck(ctx.assign_rhs(), lhsType, rhsType);
    }

    StatNode node = new AssignNode(lhs, rhs);
    node.setScope(currSymbolTable);

    return node;
  }

  @Override
  public Node visitFreeStat(FreeStatContext ctx) {
    ExprNode ref = visit(ctx.expr()).asExprNode();
    Type refType = ref.getType();
    
    /* check if the reference has correct type(array or pair) */
    typeCheck(ctx.expr(), freeStatAllowedTypes, refType);

    StatNode node = new FreeNode(ref);
    node.setScope(currSymbolTable);

    return node;
  }

  @Override
  public Node visitSkipStat(SkipStatContext ctx) {
    return new SkipNode();
  }

  @Override
  public Node visitDeclareStat(DeclareStatContext ctx) {

    ExprNode expr = visitAssign_rhs(ctx.assign_rhs()).asExprNode();
    String varName = ctx.IDENT().getText();
    Type varType = visitType(ctx.type()).asTypeDeclareNode().getType();

    if (expr != null) {
      Type exprType = expr.getType();
      typeCheck(ctx.assign_rhs(), varName, exprType, varType);
      /* first exprNode is responsible for recording type */
      expr.setType(varType);
    }

    StatNode node = new DeclareNode(varName, expr);
    node.setScope(currSymbolTable);

    currSymbolTable.add(varName, expr);

    return node;
  }

  @Override
  public Node visitReturnStat(ReturnStatContext ctx) {
    ExprNode returnNum = visit(ctx.expr()).asExprNode();

    if (isMainFunction) {
      SemanticErrorHandler.returnFromMainError(ctx);
    }

    Type returnType = returnNum.getType();
    typeCheck(ctx.expr(), expectedFunctionReturn, returnType);

    StatNode node = new ReturnNode(returnNum);
    node.setScope(currSymbolTable);
    return node;
  }

  @Override
  public Node visitExitStat(ExitStatContext ctx) {
    ExprNode exitCode = visit(ctx.expr()).asExprNode();
    Type exitCodeType = exitCode.getType();

    typeCheck(ctx.expr(), INT_BASIC_TYPE, exitCodeType);

    StatNode node = new ExitNode(exitCode);
    node.setScope(currSymbolTable);

    return node;
  }

  /************************ ExprNode(and all other nodes) Visitors *****************************/

  /**
   * return type: ExprNode */
  @Override
  public Node visitParenExpr(ParenExprContext ctx) {
    return visit(ctx.expr());
  }

  /**
   * return type: ArrayElemNode NullAble */
  @Override
  public Node visitArray_elem(Array_elemContext ctx) {

    String arrayIdent = ctx.IDENT().getText();
    ExprNode array = currSymbolTable.lookupAll(arrayIdent);

    if (array == null) {
      SemanticErrorHandler.symbolNotFound(ctx, arrayIdent);
    }

    typeCheck(ctx, ARRAY_TYPE, array.getType());

    List<ExprNode> indexList = new ArrayList<>();

    for (ExprContext index_ : ctx.expr()) {
      ExprNode index = visit(index_).asExprNode();
      // check every expr can evaluate to integer
      Type elemType = index.getType();
      typeCheck(index_, INT_BASIC_TYPE, elemType);
      indexList.add(index);
    }

    return new ArrayElemNode(array, indexList, array.getType().asArrayType().getContentType());
  }

  /**
   * return type: BinopNode */
  @Override
  public Node visitAndOrExpr(AndOrExprContext ctx) {
    String bop = ctx.bop.getText();
    Binops binop = null;
    if(bop.equals("&&")) {
      binop = Binops.AND;
    } else if(bop.equals("||")) {
      binop = Binops.OR;
    } else {
      /* throw error */
    }

    ExprNode expr1 = visit(ctx.expr(0)).asExprNode();
    Type expr1Type = expr1.getType();
    ExprNode expr2 = visit(ctx.expr(1)).asExprNode();
    Type expr2Type = expr2.getType();

    typeCheck(ctx.expr(0), BOOL_BASIC_TYPE, expr1Type);
    typeCheck(ctx.expr(1), BOOL_BASIC_TYPE, expr2Type);

    return new BinopNode(expr1, expr2, binop);
  }

  /**
   * return type: ArrayNode */
  @Override
  public Node visitArray_liter(Array_literContext ctx) {
    int length = ctx.expr().size();
    if (length == 0) {
      // contentType SHOULD not ever be used, if length is 0
      return new ArrayNode(null, new ArrayList<>(), length);
    }
    ExprNode firstExpr = visit(ctx.expr(0)).asExprNode();
    Type firstContentType = firstExpr.getType();
    List<ExprNode> list = new ArrayList<>();
    for (ExprContext context : ctx.expr()) {
      ExprNode expr = visit(context).asExprNode();
      Type exprType = expr.getType();
      typeCheck(context, firstContentType, exprType);
      list.add(expr);
    }
    return new ArrayNode(firstContentType, list, length);
  }

  /**
   * return type: TypeDeclareNode
   * */
  @Override
  public Node visitArray_type(Array_typeContext ctx) {
    TypeDeclareNode type;
    if (ctx.array_type() != null) {
      type = visitArray_type(ctx.array_type()).asTypeDeclareNode();
    } else if (ctx.base_type() != null) {
      type = visitBase_type(ctx.base_type()).asTypeDeclareNode();
    } else if (ctx.pair_type() != null) {
      type = visitPair_type(ctx.pair_type()).asTypeDeclareNode();
    } else {
      throw new IllegalArgumentException("type is not clear in visitArray_type");
    }
    return new TypeDeclareNode(new ArrayType(type.getType()));
  }

  @Override
  public Node visitAssign_lhs(Assign_lhsContext ctx) {
    Node node = null;

    if (ctx.IDENT() != null) {
      String varName = ctx.IDENT().getText();
      ExprNode value = currSymbolTable.lookupAll(varName);

      if (value == null) {
        SemanticErrorHandler.symbolNotFound(ctx, varName);
      }

      node = new IdentNode(value.getType(), varName);
    } else if (ctx.array_elem() != null) {
      node = visitArray_elem(ctx.array_elem());
    } else if (ctx.pair_elem() != null) {
      node = visitPair_elem(ctx.pair_elem());
    } else {
      /* throw error and exit program */
    }

    return node;
  }

  @Override
  public Node visitAssign_rhs(Assign_rhsContext ctx) {
    Node node = null;

    if (ctx.NEWPAIR() != null) {
      ExprNode fst = visit(ctx.expr(0)).asExprNode();
      ExprNode snd = visit(ctx.expr(1)).asExprNode();
      node = new PairNode(fst, snd);
    } else if (ctx.CALL() != null) {
      String funcName = ctx.IDENT().getText();
      FuncNode function = globalFuncTable.get(funcName);
      List<ExprNode> params = new ArrayList<>();

      /* check whether function has same number of parameter */
      int expectedParamNum = function.getParamList().size();
      if (expectedParamNum != 0) {
        if (ctx.arg_list() == null) {
          SemanticErrorHandler.invalidFuncArgCount(ctx, expectedParamNum, 0);
          return null;
        } else if (expectedParamNum != ctx.arg_list().expr().size()) {
          SemanticErrorHandler.invalidFuncArgCount(ctx, expectedParamNum, ctx.arg_list().expr().size());
          return null;
        }

        /* given argument number is not 0, generate list */
        int exprIndex = 0;
        for (ExprContext e : ctx.arg_list().expr()) {
          ExprNode param = visit(e).asExprNode();
          Type paramType = param.getType();
          Type targetType = function.getParamList().get(exprIndex).getType();

          /* check param types */
          typeCheck(ctx.arg_list().expr(exprIndex), targetType, paramType);
          params.add(param);
          exprIndex++;
        }
      }
      
      currSymbolTable = new SymbolTable(currSymbolTable);
      node = new FunctionCallNode(function, params, currSymbolTable);
      currSymbolTable = currSymbolTable.getParentSymbolTable();
    } else if (ctx.array_liter() != null) {
      node = visitArray_liter(ctx.array_liter());
    } else if (ctx.pair_elem() != null) {
      node = visitPair_elem(ctx.pair_elem());
    } else {
      node = visit(ctx.expr(0));
    }

    return node;
  }

  /**
   * return type: BoolNode */
  @Override
  public Node visitBoolExpr(BoolExprContext ctx) {
    String bool = ctx.BOOL_LITER().getText();
    return new BoolNode(bool.equals("true"));
  }

  /**
   * return type: PairNode */
  @Override
  public Node visitPairExpr(PairExprContext ctx) {
    return new PairNode();
  }

  /**
   * return type: CharNode */
  @Override
  public Node visitCharExpr(CharExprContext ctx) {
    return new CharNode(ctx.CHAR_LITER().getText().charAt(0));
  }

  /**
   * return type: BinopNode */
  @Override
  public Node visitCmpExpr(CmpExprContext ctx) {
    String bop = ctx.bop.getText();
    Binops binop = null;
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
        /* throw an error */
        throw new IllegalArgumentException("illegal argument in visitCmpExpr" + bop);
    }

    ExprNode expr1 = visit(ctx.expr(0)).asExprNode();
    Type expr1Type = expr1.getType();
    ExprNode expr2 = visit(ctx.expr(1)).asExprNode();
    Type expr2Type = expr2.getType();

    typeCheck(ctx.expr(0), cmpStatAllowedTypes, expr1Type);
    typeCheck(ctx.expr(1), cmpStatAllowedTypes, expr2Type);
    typeCheck(ctx.expr(0), expr1Type, expr2Type);

    return new BinopNode(expr1, expr2, binop);
  }

  @Override
  public Node visitEqExpr(EqExprContext ctx) {
    String bop = ctx.bop.getText();
    Binops binop = null;
    switch(bop) {
      case "==":
        binop = Binops.EQUAL;
        break;
      case "!=":
        binop = Binops.UNEQUAL;
        break;
      default:
        /* throw an error */
        throw new IllegalArgumentException("illegal argument in visitEqExpr" + bop);
    }
    ExprNode expr1 = visit(ctx.expr(0)).asExprNode();
    Type exrp1Type = expr1.getType();
    ExprNode expr2 = visit(ctx.expr(1)).asExprNode();
    Type expr2Type = expr2.getType();

    typeCheck(ctx.expr(0), exrp1Type, expr2Type);

    return new BinopNode(expr1, expr2, binop);
  }

  @Override
  public Node visitIdExpr(IdExprContext ctx) {
    String name = ctx.IDENT().getText();
    ExprNode value = currSymbolTable.lookupAll(name);
    if (value == null) {
      SemanticErrorHandler.symbolNotFound(ctx, name);
    }
    return value;
  }

  /**
   * return type: IntegerNode */
  @Override
  public Node visitIntExpr(IntExprContext ctx) {
    return new IntegerNode(intParse(ctx, ctx.INT_LITER().getText()));
  }

  /**
   * return type: ExprNode */
  @Override
  public Node visitPair_elem(Pair_elemContext ctx) {
    ExprNode exprNode = visit(ctx.expr()).asExprNode();
    Type pairType = exprNode.getType();
    boolean isFirst = false;
    Type pairElemType = null;

    typeCheck(ctx.expr(), PAIR_TYPE, pairType);

    if (ctx.FST() != null) {
      isFirst = true;
      pairElemType = pairType.asPairType().getFstType();
    } else if (ctx.SND() != null) {
      pairElemType = pairType.asPairType().getSndType();
    } else {
      throw new IllegalArgumentException("bost FST and SND are null in visitPair_elem");
    }

    if (pairElemType == null) {
      SemanticErrorHandler.invalidPairError(ctx.expr());
    }

    return new PairElemNode(exprNode, isFirst, pairElemType);
  }

  @Override
  public Node visitPair_elem_type(Pair_elem_typeContext ctx) {
    if (ctx.base_type() != null) {
      return visitBase_type(ctx.base_type());
    } else if (ctx.array_type() != null) {
      return visitArray_type(ctx.array_type());
    } else if (ctx.PAIR() != null) {
      return new TypeDeclareNode(new PairType());
    } else {
      /* throw error and exit the program */
      SemanticErrorHandler.invalidRuleException(ctx, "visitPair_elem_type");
    }
    return null;
  }

  /**
   * return type: TypeDeclareNode */
  @Override
  public Node visitPair_type(Pair_typeContext ctx) {
    TypeDeclareNode leftChild = visitPair_elem_type(ctx.pair_elem_type(0)).asTypeDeclareNode();
    TypeDeclareNode rightChild = visitPair_elem_type(ctx.pair_elem_type(1)).asTypeDeclareNode();
    Type type = new PairType(leftChild.getType(), rightChild.getType());
    return new TypeDeclareNode(type);
  }

  /**
   * return type: FuncParamNode */
  @Override
  public Node visitParam(ParamContext ctx) {
    // add an elem in current cymbol table scope
    // no node corrisponding to param, node should be explicitely traversed in function def visit
    TypeDeclareNode type = visitType(ctx.type()).asTypeDeclareNode();
    return new IdentNode(type.getType(), ctx.IDENT().getText());
  }

  @Override
  public Node visitArithmeticExpr(ArithmeticExprContext ctx) {
    String bop = ctx.bop.getText();
    Binops binop;
    switch(bop) {
      case "+":
        binop = Binops.PLUS;
        break;
      case "-":
        binop = Binops.MINUS;
        break;
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
        throw new IllegalArgumentException("invalid unary operator in visitBinopExpr: " + bop);
    }
    ExprNode expr1 = visit(ctx.expr(0)).asExprNode();
    ExprNode expr2 = visit(ctx.expr(1)).asExprNode();
    Type expr1Type = expr1.getType();
    Type expr2Type = expr2.getType();

    typeCheck(ctx.expr(0), INT_BASIC_TYPE, expr1Type);
    typeCheck(ctx.expr(1), INT_BASIC_TYPE, expr2Type);

    return new BinopNode(expr1, expr2, binop);
  }

  /**
   * return type: ArrayElemNode */
  @Override
  public Node visitArrayExpr(ArrayExprContext ctx) {
    return visitArray_elem(ctx.array_elem());
  }

  /**
   * return type: StringNode */
  @Override
  public Node visitStrExpr(StrExprContext ctx) {
    return new StringNode(ctx.STR_LITER().getText());
  }

  /**
   * return type: TypeDeclareNode */
  @Override
  public Node visitType(TypeContext ctx) {
    if (ctx.base_type() != null) {
      return visitBase_type(ctx.base_type());
    } else if (ctx.array_type() != null) {
      return visitArray_type(ctx.array_type());
    } else if (ctx.pair_type() != null) {
      return visitPair_type(ctx.pair_type());
    } else {
      /* should throw an error and exit the program here */
      SemanticErrorHandler.invalidRuleException(ctx, "visitType");
    }
    return null;
  }

  /**
   * return type: TypeDeclareNode
   * */
  @Override
  public Node visitBase_type(Base_typeContext ctx) {

    if (ctx.INT() != null) {
      return new TypeDeclareNode(INT_BASIC_TYPE);
    } else if (ctx.BOOL() != null) {
      return new TypeDeclareNode(BOOL_BASIC_TYPE);
    } else if (ctx.CHAR() != null) {
      return new TypeDeclareNode(CHAR_BASIC_TYPE);
    } else if (ctx.STRING() != null) {
      return new TypeDeclareNode(STRING_BASIC_TYPE);
    } else {
      /* should throw an error and exit the program here */
      SemanticErrorHandler.invalidRuleException(ctx, "visitBase_type");
    }
    return null;
  }

  /**
   * return type: UnopNode NullAble
   */
  @Override
  public Node visitUnopExpr(UnopExprContext ctx) {
    String uop = ctx.uop.getText();
    Unop unop;
    Type targetType;
    switch (uop) {
      case "-":
        unop = Unop.MINUS;
        targetType = INT_BASIC_TYPE;

        String exprText = ctx.expr().getText();
        if (isDigit(exprText)) {
          /* explicitely call cast here, in order to cover INT_MIN */
          Integer intVal = intParse(ctx.expr(), "-" + ctx.expr().getText());
          return new IntegerNode(intVal);
        }
        break;
      case "chr":
        unop = Unop.CHR;
        targetType = INT_BASIC_TYPE;
        break;
      case "!":
        unop = Unop.NOT;
        targetType = BOOL_BASIC_TYPE;
        break;
      case "len":
        unop = Unop.LEN;
        targetType = ARRAY_TYPE;
        break;
      case "ord":
        unop = Unop.ORD;
        targetType = CHAR_BASIC_TYPE;
        break;
      default:
        SemanticErrorHandler.invalidRuleException(ctx, "visitUnopExpr");
        return null;
    }

    ExprNode expr = visit(ctx.expr()).asExprNode();
    Type exprType = expr.getType();

    typeCheck(ctx.expr(), targetType, exprType);

    return new UnopNode(expr, unop);
  }

  /* Helper functions */

  private void typeCheck(ParserRuleContext ctx, Type expected, Type actual) {
    if (!actual.equalToType(expected)) {
      SemanticErrorHandler.typeMismatch(ctx, expected, actual);
    }
  }

  private void typeCheck(ParserRuleContext ctx, Set<Type> expected, Type actual) {
    if (expected.stream().noneMatch(actual::equalToType)) {
      SemanticErrorHandler.typeMismatch(ctx, expected, actual);
    }
  }

  private void typeCheck(ParserRuleContext ctx, String varName, Type expected, Type actual) {
    if (!actual.equalToType(expected)) {
      SemanticErrorHandler.typeMismatch(ctx, varName, expected, actual);
    }
  }

  private Integer intParse(ParserRuleContext ctx, String intExt) {
    int integer = 0;
    try {
      integer = Integer.parseInt(intExt);
    } catch (NumberFormatException e) {
      SemanticErrorHandler.integerRangeError(ctx, intExt);
    }
    return integer;
  }

  private boolean isDigit(String s) {
    return s.matches("[0-9]+");
  }
  
}