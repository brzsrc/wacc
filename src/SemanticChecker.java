import antlr.WACCParser;
import antlr.WACCParser.*;
import antlr.WACCParserBaseVisitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import node.FuncNode;
import node.Node;
import node.ProgramNode;
import node.TypeDeclareNode;
import node.expr.*;
import node.expr.BinopNode.Binops;
import node.expr.UnopNode.Unop;
import node.stat.*;
import type.*;
import utils.ErrorHandler;
import utils.SymbolTable;

public class SemanticChecker extends WACCParserBaseVisitor<Node> {

  /* Type classes to represent BasicType, ArrayType, and PairType */
  private static Type INT_BASIC_TYPE = new BasicType(BasicTypeEnum.INTEGER);
  private static Type BOOL_BASIC_TYPE = new BasicType(BasicTypeEnum.BOOLEAN);
  private static Type CHAR_BASIC_TYPE = new BasicType(BasicTypeEnum.CHAR);
  private static Type STRING_BASIC_TYPE = new BasicType(BasicTypeEnum.STRING);
  private static Type ARRAY_TYPE = new ArrayType();
  private static Type PAIR_TYPE = new PairType();

  /* ErrorHandler that will print appropriate messages of semantic/syntax error */
  private static ErrorHandler errorHandler = new ErrorHandler();

  /* recording the current SymbolTable during parser tree visits */
  private SymbolTable currSymbolTable;

  /* global function table */
  private Map<String, FuncNode> globalFuncTable;

  public SemanticChecker() {
    currSymbolTable = null;
    globalFuncTable = new HashMap<>();
  }

  @Override
  public Node visitProgram(ProgramContext ctx) {
    /* a list of functions declared at the beginning of the program */
    List<FuncNode> functions = new ArrayList<>();
    
    /* iterate through a list of function declarations to build a list of FuncNode */
    for (FuncContext f : ctx.func()) {
      FuncNode funcNode = (FuncNode) visitFunc(f);
      String funcName = f.IDENT().getText();

      /* if the function declaration is not terminated with a return/exit statement, then throw the semantic error */
      if (!funcNode.getFunctionBody().leaveAtEnd()) {
        errorHandler.invalidFunctionReturnExit(ctx, funcName);
      }

      functions.add(funcNode);
      globalFuncTable.put(funcName, funcNode);
    }

    /* visit the body of the program and create the root SymbolTable here */
    currSymbolTable = new SymbolTable(currSymbolTable);
    StatNode body = (StatNode) visit(ctx.stat());
    currSymbolTable = currSymbolTable.getParentSymbolTable();
    body.setScope(currSymbolTable);

    return new ProgramNode(functions, new ScopeNode(body));
  }

  @Override
  public Node visitFunc(FuncContext ctx) {

    /**
     * visitFunc() will only handle the declaration of functions and generate corresponding
     * FuncNode. It will not process the calling of the functions
     */

    /* get the return type of the function */
    Type returnType = ((TypeDeclareNode) visitType(ctx.type())).getType();
    /* store the parameters in a list of IdentNode */
    List<IdentNode> param_list = new ArrayList<>();

    for (ParamContext param : ctx.param_list().param()) {
      Type param_type = ((TypeDeclareNode) visitType(param.type())).getType();
      IdentNode paramNode = new IdentNode(param_type, param.IDENT().getText());
      param_list.add(paramNode);
    }

    /* visit the function body */
    currSymbolTable = new SymbolTable(currSymbolTable);
    param_list.stream().forEach(i -> {
      currSymbolTable.add(i.getName(), i);
    });
    StatNode functionBody = (StatNode) visit(ctx.stat());
    functionBody.setScope(currSymbolTable);
    currSymbolTable = currSymbolTable.getParentSymbolTable();

    return new FuncNode(returnType, new ScopeNode(functionBody), param_list);
  }

  /******************************** StatNode Visitors *************************************/
  /** all following function's return type are statNode */

  @Override
  public Node visitSeqStat(SeqStatContext ctx) {
    StatNode before = (StatNode) visit(ctx.stat(0));
    StatNode after = (StatNode) visit(ctx.stat(1));
    
    return new ScopeNode(before, after);
  }

  @Override
  public Node visitIfStat(IfStatContext ctx) {
    /* check that the condition of if statement is of type boolean */
    ExprNode condition = (ExprNode) visit(ctx.expr());
    Type conditionType = condition.getType();

    if (!conditionType.equalToType(BOOL_BASIC_TYPE)) {
      errorHandler.typeMismatch(ctx.expr(), BOOL_BASIC_TYPE, conditionType);
    }
    
    /* create the StatNode for the if body and gegerate new child scope */
    currSymbolTable = new SymbolTable(currSymbolTable);
    StatNode ifBody = (StatNode) visit(ctx.stat(0));
    currSymbolTable = currSymbolTable.getParentSymbolTable();
    
    /* create the StatNode for the else body and generate new child scope */
    currSymbolTable = new SymbolTable(currSymbolTable);
    StatNode elseBody = (StatNode) visit(ctx.stat(1));
    currSymbolTable = currSymbolTable.getParentSymbolTable();

    StatNode node = new IfNode(condition, new ScopeNode(ifBody), new ScopeNode(elseBody));

    node.setScope(currSymbolTable);

    return node;
  }

  @Override
  public Node visitWhileStat(WhileStatContext ctx) {
    /* check that the condition of while statement is of type boolean */
    ExprNode condition = (ExprNode) visit(ctx.expr());
    Type conditionType = condition.getType();

    if (!conditionType.equalToType(BOOL_BASIC_TYPE)) {
      errorHandler.typeMismatch(ctx.expr(), BOOL_BASIC_TYPE, conditionType);
    }

    /* get the StatNode of the execution body of while loop */
    currSymbolTable = new SymbolTable(currSymbolTable);
    StatNode body = (StatNode) visit(ctx.stat());
    currSymbolTable = currSymbolTable.getParentSymbolTable();

    StatNode node = new WhileNode((ExprNode) visit(ctx.expr()), new ScopeNode(body));
    node.setScope(currSymbolTable);

    return node;
  }

  @Override
  public Node visitScopeStat(ScopeStatContext ctx) {
    /* simply create a new SymbolTable to represent a BEGIN ... END statement */
    currSymbolTable = new SymbolTable(currSymbolTable);
    StatNode body = (StatNode) visit(ctx.stat());
    ScopeNode scopeNode = new ScopeNode(body);
    currSymbolTable = currSymbolTable.getParentSymbolTable();

    return scopeNode;
  }

  @Override
  public Node visitReadStat(ReadStatContext ctx) {
    /* a list of allowed types in Read statement */
    List<Type> allowedTypes = List.of(STRING_BASIC_TYPE, INT_BASIC_TYPE, CHAR_BASIC_TYPE);

    ExprNode exprNode = (ExprNode) visitAssign_lhs(ctx.assign_lhs());
    Type inputType = exprNode.getType();

    /* check if the variable for the read input is of type string, int, or char */
    if (allowedTypes.stream().noneMatch(inputType::equalToType)) {
      errorHandler.typeMismatch(ctx.assign_lhs(), allowedTypes, inputType);
    }

    ReadNode readNode = new ReadNode(exprNode);

    readNode.setScope(currSymbolTable);

    return readNode;
  }

  @Override
  public Node visitPrintStat(PrintStatContext ctx) {
    ExprNode printContent = (ExprNode) visit(ctx.expr());
    /* no restriction on type to be printed, all type can be printed */

    StatNode node = new PrintNode(printContent);
    node.setScope(currSymbolTable);

    return node;
  }

  @Override
  public Node visitPrintlnStat(PrintlnStatContext ctx) {
    ExprNode printContent = (ExprNode) visit(ctx.expr());
    /* no restriction on type to be printed, all type can be printed */

    StatNode node = new PrintlnNode(printContent);
    node.setScope(currSymbolTable);

    return node;
  }

  @Override
  public Node visitAssignStat(AssignStatContext ctx) {
    
    /* check if the type of lhs and rhs are equal */
    ExprNode lhs = (ExprNode) visit(ctx.assign_lhs());
    ExprNode rhs = (ExprNode) visit(ctx.assign_rhs());
    Type lhsType = lhs.getType();
    Type rhsType = rhs.getType();

    if (!lhsType.equalToType(rhsType)) {
      errorHandler.typeMismatch(ctx.assign_rhs(), lhsType, rhsType);
    }

    StatNode node = new AssignNode(lhs, rhs);
    node.setScope(currSymbolTable);

    return node;
  }

  @Override
  public Node visitFreeStat(FreeStatContext ctx) {
    /* the allowed types of free statement */
    List<Type> allowedType = List.of(ARRAY_TYPE, PAIR_TYPE);

    ExprNode ref = (ExprNode) visit(ctx.expr());
    Type refType = ref.getType();
    
    /* check if the reference has correct type(array or pair) */
    if (allowedType.stream().noneMatch(refType::equalToType)) {
      errorHandler.typeMismatch(ctx.expr(), allowedType, refType);
    }

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

    ExprNode expr = (ExprNode) visitAssign_rhs(ctx.assign_rhs());
    String varName = ctx.IDENT().getText();
    Type varType = ((TypeDeclareNode) visitType(ctx.type())).getType();
    Type exprType = expr.getType();

    if (!varType.equalToType(exprType)) {
      errorHandler.typeMismatch(ctx.assign_rhs(), varName, varType, exprType);
    }

    StatNode node = new DeclareNode(varName, expr);
    node.setScope(currSymbolTable);

    currSymbolTable.add(varName, expr);

    return node;
  }

  @Override
  public Node visitReturnStat(ReturnStatContext ctx) {
    return new ReturnNode((ExprNode) visit(ctx.expr()));
  }

  @Override
  public Node visitExitStat(ExitStatContext ctx) {
    ExprNode exitCode = (ExprNode) visit(ctx.expr());
    Type exitCodeType = exitCode.getType();

    if (!exitCodeType.equalToType(INT_BASIC_TYPE)) {
      errorHandler.typeMismatch(ctx.expr(), INT_BASIC_TYPE, exitCodeType);
    }

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
    ExprNode array_ = currSymbolTable.lookupAll(arrayIdent);

    if (array_ == null) {
      errorHandler.symbolNotFound(ctx, arrayIdent);
      return null;
    }

    ArrayNode array = (ArrayNode) array_;

    /* get depth of type */
    int indexDepth = ctx.expr().size();
    int arrayMaxDepth = array.getDepth();

    if (arrayMaxDepth < indexDepth) {
      /* throw type error */
      errorHandler.arrayDepthError(ctx, array.getType(), indexDepth);
    }

    List<ExprNode> indexList = new ArrayList<>();

    for (ExprContext index_ : ctx.expr()) {
      ExprNode index = (ExprNode) visit(index_);
      // check every expr can evaluate to integer
      if (!index.getType().equalToType(INT_BASIC_TYPE)) {
        errorHandler.typeMismatch(index_, INT_BASIC_TYPE, index.getType());
      }

      indexList.add(index);
    }
    return new ArrayElemNode(array, indexList);
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

    ExprNode expr1 = (ExprNode)visit(ctx.expr(0));
    ExprNode expr2 = (ExprNode)visit(ctx.expr(1));

    if(!expr1.getType().equalToType(BOOL_BASIC_TYPE)) {
      errorHandler.typeMismatch(ctx.expr(0), BOOL_BASIC_TYPE, expr1.getType());
    }
    if(!expr2.getType().equalToType(BOOL_BASIC_TYPE)) {
      errorHandler.typeMismatch(ctx.expr(1), BOOL_BASIC_TYPE, expr2.getType());
    }
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
    ExprNode firstExpr = (ExprNode) visit(ctx.expr(0));
    Type firstContentType = firstExpr.getType();
    List<ExprNode> list = new ArrayList<>();
    for (ExprContext context : ctx.expr()) {
      ExprNode expr = (ExprNode) visit(context);
      if (!firstContentType.equalToType(expr.getType())) {
        errorHandler.typeMismatch(context, firstContentType, expr.getType());
      }

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
      type = (TypeDeclareNode) visitArray_type(ctx.array_type());
    } else if (ctx.base_type() != null) {
      type = (TypeDeclareNode) visitBase_type(ctx.base_type());
    } else if (ctx.pair_type() != null) {
      type = (TypeDeclareNode) visitPair_type(ctx.pair_type());
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
        errorHandler.symbolNotFound(ctx, varName);
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
      ExprNode fst = (ExprNode) visit(ctx.expr(0));
      ExprNode snd = (ExprNode) visit(ctx.expr(1));
      node = new PairNode(fst, snd);
    } else if (ctx.CALL() != null) {
      String funcName = ctx.IDENT().getText();
      FuncNode function = globalFuncTable.get(funcName);
      List<ExprNode> params = new ArrayList<>();

      int exprIndex = 0;
      for (ExprContext e : ctx.arg_list().expr()) {
        ExprNode param = (ExprNode) visit(e);
        Type targetType = function.getParamList().get(exprIndex).getType();

        /* check param types */
        if (!param.getType().equalToType(targetType)) {
          errorHandler.typeMismatch(ctx.expr(exprIndex), targetType, param.getType());
        }
        exprIndex++;

        params.add(param);
      }
      
      currSymbolTable = new SymbolTable(currSymbolTable);
      node = new FunctionCallNode(function, params, currSymbolTable);
      currSymbolTable = currSymbolTable.getParentSymbolTable();
    } else if (ctx.array_liter() != null) {
      node = visitArray_liter(ctx.array_liter());
    } else if (ctx.pair_elem() != null) {
      node = visitPair_elem(ctx.pair_elem());
    } else {
      /* expression */
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

    ExprNode expr1 = (ExprNode)visit(ctx.expr(0));
    ExprNode expr2 = (ExprNode)visit(ctx.expr(1));

    if (!expr1.getType().equalToType(expr2.getType())) {
      errorHandler.typeMismatch(ctx.expr(1), expr1.getType(), expr2.getType());
    }
    if (!expr1.getType().equalToType(INT_BASIC_TYPE)
     && !expr1.getType().equalToType(CHAR_BASIC_TYPE)) {
      errorHandler.typeMismatch(ctx.expr(0), INT_BASIC_TYPE, expr1.getType());
      errorHandler.typeMismatch(ctx.expr(0), CHAR_BASIC_TYPE, expr1.getType());
    }

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
    ExprNode expr1 = (ExprNode) visit(ctx.expr(0));
    ExprNode expr2 = (ExprNode) visit(ctx.expr(1));

    if (!expr1.getType().equalToType(expr2.getType())) {
      errorHandler.typeMismatch(ctx.expr(1), expr1.getType(), expr2.getType());
    }

    return new BinopNode(expr1, expr2, binop);
  }

  @Override
  public Node visitIdExpr(IdExprContext ctx) {
    String name = ctx.IDENT().getText();
    ExprNode value = currSymbolTable.lookupAll(name);
    if (value == null) {
      errorHandler.symbolNotFound(ctx, name);
    }
    return value;
  }

  /**
   * return type: IntegerNode */
  @Override
  public Node visitIntExpr(IntExprContext ctx) {
    int integer = Integer.parseInt(ctx.INT_LITER().getText());
    return new IntegerNode(integer);
  }

  /**
   * return type: BinopNode */
  @Override
  public Node visitMulDivExpr(MulDivExprContext ctx) {
    String bop = ctx.bop.getText();
    Binops binop = null;
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
        /* throw error */
        throw new IllegalArgumentException("illegal argument in visitMulDivExpr" + bop);
    }
    ExprNode expr1 = (ExprNode) visit(ctx.expr(0));
    ExprNode expr2 = (ExprNode) visit(ctx.expr(1));

    if (!expr1.getType().equalToType(INT_BASIC_TYPE)) {
      errorHandler.typeMismatch(ctx.expr(0), INT_BASIC_TYPE, expr1.getType());
    }
    if (!expr2.getType().equalToType(INT_BASIC_TYPE)) {
      errorHandler.typeMismatch(ctx.expr(1), INT_BASIC_TYPE, expr2.getType());
    }

    return new BinopNode(expr1, expr2, binop);
  }

  /**
   * return type: ExprNode */
  @Override
  public Node visitPair_elem(Pair_elemContext ctx) {
    ExprNode exprNode = (ExprNode) visit(ctx.expr());
    if (!exprNode.getType().equalToType(PAIR_TYPE)) {
      errorHandler.typeMismatch(ctx.expr(), PAIR_TYPE, exprNode.getType());
    }

    PairNode pairNode = (PairNode) exprNode;
    if (ctx.FST() != null) {
      return pairNode.getFst();
    } else if (ctx.SND() != null) {
      return pairNode.getSnd();
    }
    errorHandler.invalidRuleException(ctx, "visitPair_elem");
    return null;
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
      errorHandler.invalidRuleException(ctx, "visitPair_elem_type");
    }
    return null;
  }

  /**
   * return type: TypeDeclareNode */
  @Override
  public Node visitPair_type(Pair_typeContext ctx) {
    TypeDeclareNode leftChild = (TypeDeclareNode) visitPair_elem_type(ctx.pair_elem_type(0));
    TypeDeclareNode rightChild = (TypeDeclareNode) visitPair_elem_type(ctx.pair_elem_type(1));
    Type type = new PairType(leftChild.getType(), rightChild.getType());
    return new TypeDeclareNode(type);
  }

  /**
   * return type: FuncParamNode */
  @Override
  public Node visitParam(ParamContext ctx) {
    // add an elem in current cymbol table scope
    // no node corrisponding to param, node should be explicitely traversed in function def visit
    TypeDeclareNode type = (TypeDeclareNode) visitType(ctx.type());
    return new IdentNode(type.getType(), ctx.IDENT().getText());
  }

  /**
   * return type: BinopNode */
  @Override
  public Node visitPlusMinExpr(PlusMinExprContext ctx) {
    String bop = ctx.bop.getText();
    Binops binop;
    switch(bop) {
      case "+":
        binop = Binops.PLUS;
        break;
      case "-":
        binop = Binops.MINUS;
        break;
      default:
        throw new IllegalArgumentException("invalid unary operator in visitUnopExpr: " + bop);
    }
    ExprNode expr1 = (ExprNode) visit(ctx.expr(0));
    ExprNode expr2 = (ExprNode) visit(ctx.expr(1));
    Type expr1Type = expr1.getType();
    Type expr2Type = expr2.getType();

    if(!expr1Type.equalToType(INT_BASIC_TYPE)) {
      errorHandler.typeMismatch(ctx.expr(0), INT_BASIC_TYPE, expr1Type);
    }
    if(!expr2Type.equalToType(INT_BASIC_TYPE)) {
      errorHandler.typeMismatch(ctx.expr(1), INT_BASIC_TYPE, expr2Type);
    }

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
      errorHandler.invalidRuleException(ctx, "visitType");
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
      errorHandler.invalidRuleException(ctx, "visitBase_type");
    }
    return null;
  }

  /**
   * return type: UnopNode
   */
  @Override
  public Node visitUnopExpr(UnopExprContext ctx) {
    String uop = ctx.uop.getText();
    ExprNode expr = (ExprNode) visit(ctx.expr());
    Unop unop;
    Type targetType;
    switch (uop) {
      case "-":
        unop = Unop.MINUS;
        targetType = INT_BASIC_TYPE;
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
        errorHandler.invalidRuleException(ctx, "visitUnopExpr");
        return null;
    }

    if (!expr.getType().equalToType(targetType)) {
      errorHandler.typeMismatch(ctx.expr(), targetType, expr.getType());
    }
    return new UnopNode(expr, unop);
  }
  
}