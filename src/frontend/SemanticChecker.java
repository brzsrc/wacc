package frontend;

import frontend.antlr.WACCParser.*;
import frontend.antlr.WACCParserBaseVisitor;

import frontend.node.StructDeclareNode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import frontend.node.FuncNode;
import frontend.node.Node;
import frontend.node.ProgramNode;
import frontend.node.TypeDeclareNode;
import frontend.node.expr.*;
import frontend.node.stat.*;
import frontend.node.stat.JumpNode.JumpContext;
import frontend.node.stat.JumpNode.JumpType;
import frontend.node.stat.SwitchNode.CaseStat;
import frontend.node.expr.BinopNode.Binop;
import frontend.node.expr.UnopNode.Unop;

import frontend.type.*;
import org.antlr.v4.runtime.tree.TerminalNode;
import utils.frontend.symbolTable.Symbol;
import utils.frontend.symbolTable.SymbolTable;

import static utils.frontend.SemanticErrorHandler.*;
import static utils.Utils.*;

public class SemanticChecker extends WACCParserBaseVisitor<Node> {

  /**
   * SemanticChecker will not only check the semantics of the provided .wacc file, but also generate
   * an internal representation of the program using ExprNode and StatNode. This will aid the
   * process of code generation in the backend
   */

  /* recording the current SymbolTable during parser tree visits */
  private SymbolTable currSymbolTable;

  /* global data struct table, used to record all struct */
  private final Map<String, StructDeclareNode> globalStructTable;

  /* global function table, used to record all functions */
  private final Map<String, FuncNode> globalFuncTable;

  /* used after function declare step, to detect RETURN statement in main body */
  private boolean isMainFunction;

  /* used in determining whether branching statement is legal, i.e. break/continue is within a loop/switch */
  private boolean isBreakAllowed;
  private boolean isContinueAllowed;
  private boolean isJumpRepeated;
  private JumpContext jumpContext;

  /* used only in function declare step, to check function has the correct return type */
  private Type expectedFunctionReturn;

  /* record whether a skipable semantic error is found in visiting to support checking of multiple errors */
  private boolean semanticError;

  /* record whether the 'null' is used to represent uninitialised struct or not */
  private boolean isStruct = false;
  /* record the for-loop incrementer so that break and continue know what to do before jumping */
  private StatNode currForLoopIncrementBreak;
  private StatNode currForLoopIncrementContinue;

  /* constructor of SemanticChecker */
  public SemanticChecker() {
    currSymbolTable = null;
    globalStructTable = new HashMap<>();
    globalFuncTable = new HashMap<>();
    isMainFunction = false;
    isBreakAllowed = false;
    isContinueAllowed = false;
    jumpContext = null;
    isJumpRepeated = false;
    expectedFunctionReturn = null;
    currForLoopIncrementBreak = null;
    currForLoopIncrementContinue = null;
  }

  @Override
  public Node visitProgram(ProgramContext ctx) {

    /* add the struct name in order to have recursive data struct */
    for (StructContext s : ctx.struct()) {
      String structName = s.IDENT().getText();

      if (globalFuncTable.containsKey(structName)) {
        symbolRedeclared(ctx, structName);
        semanticError = true;
      }

      /* do not add the node during the first retrieve (would do it later) */
      globalStructTable.put(structName, null);
    }

    /* visit these struct, and update the map */
    for (StructContext s : ctx.struct()) {
      StructDeclareNode node = (StructDeclareNode) visitStruct(s);
      globalStructTable.replace(s.IDENT().getText(), node);
    }

    /* add the identifiers and parameter list of functions in the globalFuncTable first */
    for (FuncContext f : ctx.func()) {
      String funcName = f.IDENT().getText();

      /* check if the function is defined already */
      if (globalFuncTable.containsKey(funcName)) {
        symbolRedeclared(ctx, funcName);
        semanticError = true;
      }

      /* get the return type of the function */
      Type returnType = visit(f.type()).asTypeDeclareNode().getType();
      /* store the parameters in a list of IdentNode */
      List<IdentNode> param_list = new ArrayList<>();

      if (f.param_list() != null) {
        for (ParamContext param : f.param_list().param()) {
          Type param_type = visit(param.type()).asTypeDeclareNode().getType();
          IdentNode paramNode = new IdentNode(param_type, param.IDENT().getText());
          param_list.add(paramNode);
        }
      }

      globalFuncTable.put(funcName, new FuncNode(funcName, returnType, param_list));
    }

    /* then iterate through a list of function declarations to visit the function body */
    for (FuncContext f : ctx.func()) {
      String funcName = f.IDENT().getText();

      StatNode functionBody = visitFunc(f).asStatNode();

      /* if the function declaration is not terminated with a return/exit statement, then throw the semantic error */
      if (!functionBody.leaveAtEnd()) {
        invalidFunctionReturnExit(ctx, funcName);
      }

      globalFuncTable.get(funcName).setFunctionBody(functionBody);
    }

    /* visit the body of the program and create the root SymbolTable here */
    isMainFunction = true;
    currSymbolTable = new SymbolTable(currSymbolTable);
    StatNode body = visit(ctx.stat()).asStatNode();
    if (!(body instanceof ScopeNode)) {
      body = new ScopeNode(body);
      if (body.getScope() == null) {
        body.setScope(currSymbolTable);
      }
    }
    currSymbolTable = currSymbolTable.getParentSymbolTable();

    if (semanticError) {
      System.out.println("error found");
      System.exit(SEMANTIC_ERROR_CODE);
    }

    return new ProgramNode(globalFuncTable, body);
  }

  @Override
  public Node visitStruct(StructContext ctx) {
    List<IdentNode> elements = new ArrayList<>();

    for (ParamContext elem : ctx.param_list().param()) {
      Type elemType = visit(elem.type()).asTypeDeclareNode().getType();
      IdentNode elemNode = new IdentNode(elemType, elem.IDENT().getText());
      elements.add(elemNode);
    }

    return new StructDeclareNode(elements, ctx.IDENT().getText());
  }

  @Override
  public Node visitFunc(FuncContext ctx) {

    FuncNode funcNode = globalFuncTable.get(ctx.IDENT().getText());

    /* visit the function body */
    expectedFunctionReturn = funcNode.getReturnType();
    currSymbolTable = new SymbolTable(currSymbolTable);

    /* initialise as -4 byte in order to leave space for PUSH {lr}, 
       which takes up 4 bute on stack */
    int tempStackAddr = -POINTER_SIZE;
    List<IdentNode> params = funcNode.getParamList();
    int paramNum = params.size();

    for (int i = paramNum - 1; i >= 0; i--) {
      IdentNode param = params.get(i);
      tempStackAddr += param.getType().getSize();
      currSymbolTable.add(param.getName(), param, tempStackAddr);
    }

    StatNode functionBody = visit(ctx.stat()).asStatNode();
    functionBody.setScope(currSymbolTable);
    currSymbolTable = currSymbolTable.getParentSymbolTable();

    if (functionBody instanceof ScopeNode) {
      ((ScopeNode) functionBody).setFuncBody();
      return functionBody;
    }
    ScopeNode enclosedBody = new ScopeNode(functionBody);
    enclosedBody.setFuncBody();
    return enclosedBody;
  }

  /* =========================================================
   *                   Statement Visitors
   * =========================================================
   */

  @Override
  public Node visitSeqStat(SeqStatContext ctx) {
    StatNode before = visit(ctx.stat(0)).asStatNode();
    StatNode after = visit(ctx.stat(1)).asStatNode();
    if (!isMainFunction && before.leaveAtEnd()) {
      functionJunkAfterReturn(ctx);
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
    semanticError |= typeCheck(ctx.expr(), BOOL_BASIC_TYPE, conditionType);

    /* create the StatNode for the if body and gegerate new child scope */
    currSymbolTable = new SymbolTable(currSymbolTable);
    StatNode ifBody = visit(ctx.stat(0)).asStatNode();
    currSymbolTable = currSymbolTable.getParentSymbolTable();

    /* create the StatNode for the else body and generate new child scope */
    currSymbolTable = new SymbolTable(currSymbolTable);
    StatNode elseBody = visit(ctx.stat(1)).asStatNode();
    currSymbolTable = currSymbolTable.getParentSymbolTable();

    StatNode node = new IfNode(condition,
            ifBody instanceof ScopeNode ?
                    ifBody :
                    new ScopeNode(ifBody),
            elseBody instanceof ScopeNode ?
                    elseBody :
                    new ScopeNode(elseBody));

    node.setScope(currSymbolTable);

    return node;
  }

  @Override
  public Node visitWhileStat(WhileStatContext ctx) {
    /* check that the condition of while statement is of type boolean */
    ExprNode condition = visit(ctx.expr()).asExprNode();
    Type conditionType = condition.getType();
    semanticError |= typeCheck(ctx.expr(), BOOL_BASIC_TYPE, conditionType);

    /* get the StatNode of the execution body of while loop */
    currSymbolTable = new SymbolTable(currSymbolTable);

    boolean isNestedBreak = isBreakAllowed;
    boolean isNestedContinue = isContinueAllowed;
    isBreakAllowed = isContinueAllowed = true;
    jumpContext = JumpContext.WHILE;
    isJumpRepeated = false;
    StatNode body = visit(ctx.stat()).asStatNode();
    currSymbolTable = currSymbolTable.getParentSymbolTable();

    if (!isNestedBreak) isBreakAllowed = false;
    if (!isNestedContinue) isContinueAllowed = false;

    StatNode node = (body instanceof ScopeNode) ?
            new WhileNode(condition, body) :
            new WhileNode(condition, new ScopeNode(body));
    node.setScope(currSymbolTable);

    return node;
  }

  @Override 
  public Node visitDoWhileStat(DoWhileStatContext ctx) {
    /* check that the condition of while statement is of type boolean */
    ExprNode condition = visit(ctx.expr()).asExprNode();
    Type conditionType = condition.getType();
    semanticError |= typeCheck(ctx.expr(), BOOL_BASIC_TYPE, conditionType);

    /* get the StatNode of the execution body of while loop */
    currSymbolTable = new SymbolTable(currSymbolTable);
    isBreakAllowed = isContinueAllowed = true;
    StatNode body = visit(ctx.stat()).asStatNode();
    isBreakAllowed = isContinueAllowed = false;
    currSymbolTable = currSymbolTable.getParentSymbolTable();

    StatNode node = (body instanceof ScopeNode) ?
            new WhileNode(condition, body, true) :
            new WhileNode(condition, new ScopeNode(body), true);
    node.setScope(currSymbolTable);

    return node;
  }

  @Override
  public Node visitForStat(ForStatContext ctx) {
    /* create the symbol table for the entire for-loop, including the initiator, cond, and increment */
    currSymbolTable = new SymbolTable(currSymbolTable);
    /* visit the initiator */
    StatNode init = visit(ctx.for_stat(0)).asStatNode();

    /* isNestedBreak and isNestedContinue is used for nested/mixed loops/switch statements */
    boolean isNestedBreak = isBreakAllowed;
    boolean isNestedContinue = isContinueAllowed;
    
    ExprNode cond = visit(ctx.expr()).asExprNode();
    StatNode increment = visit(ctx.for_stat(1)).asStatNode();

    /* mark the point where break and continue are allowed to appear */
    isBreakAllowed = isContinueAllowed = true;
    /* record the for-loop increment so that when `continue` appear, it still processes the increment in the assembly */
    currForLoopIncrementBreak = increment;
    currForLoopIncrementContinue = increment;
    /* mark the jump context to let `break` know what statement its parent is */
    jumpContext = JumpContext.FOR;
    isJumpRepeated = false;
    /* visit the for loop body */
    StatNode body = visit(ctx.stat()).asStatNode();
    currForLoopIncrementBreak = null;
    currForLoopIncrementContinue = null;
    if (!isNestedBreak) isBreakAllowed = false;
    if (!isNestedContinue) isContinueAllowed = false;

    StatNode _init = init instanceof ScopeNode ? init : new ScopeNode(init);
    StatNode _increment = increment instanceof ScopeNode ? increment : new ScopeNode(increment);
    StatNode _body = body instanceof ScopeNode ? body : new ScopeNode(body);

    StatNode forNode = new ForNode(_init, cond, _increment, _body);

    _init.setScope(currSymbolTable);
    _increment.setScope(currSymbolTable);

    currSymbolTable = currSymbolTable.getParentSymbolTable();

    forNode.setScope(currSymbolTable);
    
    return forNode;
  }

  @Override
  public Node visitForStatSeq(ForStatSeqContext ctx) {
    StatNode stat1 = visit(ctx.for_stat(0)).asStatNode();
    StatNode stat2 = visit(ctx.for_stat(1)).asStatNode();
    
    return new ScopeNode(stat1, stat2);
  }

  @Override
  public Node visitBreakStat(BreakStatContext ctx) {
    if (isJumpRepeated) {
      branchStatementMutipleError(ctx, JumpType.BREAK);
    }
    if (!isBreakAllowed) {
      branchStatementPositionError(ctx, JumpType.BREAK);
    }
    StatNode breakNode = new JumpNode(JumpType.BREAK, currForLoopIncrementBreak, jumpContext);
    breakNode.setScope(currSymbolTable);
    isJumpRepeated = true;
    return breakNode;
  }

  @Override
  public Node visitContinueStat(ContinueStatContext ctx) {
    if (isJumpRepeated) {
      branchStatementMutipleError(ctx, JumpType.CONTINUE);
    }
    if (!isContinueAllowed) {
      branchStatementPositionError(ctx, JumpType.CONTINUE);
    }
    StatNode continueNode = new JumpNode(JumpType.CONTINUE, currForLoopIncrementContinue, jumpContext);
    continueNode.setScope(currSymbolTable);
    isJumpRepeated = true;
    return continueNode;
  }

  @Override
  public Node visitSwitchStat(SwitchStatContext ctx) {
    ExprNode expr = visit(ctx.expr(0)).asExprNode();

    int numOfCases = ctx.expr().size();
    List<CaseStat> cases = new ArrayList<>();
    SymbolTable switchSymbolTable = new SymbolTable(currSymbolTable);

    /* this is to deal with nested loop/switch cases */
    boolean isNested = isBreakAllowed;
    isBreakAllowed = true;
    jumpContext = JumpContext.SWITCH;
    isJumpRepeated = false;
    StatNode defaultCase = visit(ctx.stat(numOfCases - 1)).asStatNode();
    
    defaultCase.setScope(switchSymbolTable);
    currForLoopIncrementBreak = null;
    

    for (int i = 1; i < numOfCases; i++) {
      ExprNode caseExpr = visit(ctx.expr(i)).asExprNode();

      currSymbolTable = switchSymbolTable;
      StatNode caseNode = visit(ctx.stat(i - 1)).asStatNode();
      caseNode.setScope(currSymbolTable);
      currSymbolTable = currSymbolTable.getParentSymbolTable();

      CaseStat caseStat = new CaseStat(caseExpr, caseNode);
      cases.add(caseStat);
    }

    if (!isNested) isBreakAllowed = false;

    StatNode switchNode = new SwitchNode(expr, cases, defaultCase);
    switchNode.setScope(currSymbolTable);

    return switchNode;
  }

  @Override
  public Node visitScopeStat(ScopeStatContext ctx) {
    /* simply create a new SymbolTable to represent a BEGIN ... END statement */
    currSymbolTable = new SymbolTable(currSymbolTable);
    StatNode body = visit(ctx.stat()).asStatNode();
    ScopeNode scopeNode = new ScopeNode(body);
    if (scopeNode.getScope() == null) {
      scopeNode.setScope(currSymbolTable);
    }
    currSymbolTable = currSymbolTable.getParentSymbolTable();

    return scopeNode;
  }

  @Override
  public Node visitReadStat(ReadStatContext ctx) {
    ExprNode exprNode = visit(ctx.assign_lhs()).asExprNode();
    if (exprNode != null) {
      Type inputType = exprNode.getType();
      semanticError |= typeCheck(ctx.assign_lhs(), readStatAllowedTypes, inputType);
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
    ExprNode lhs = visit(ctx.assign_lhs()).asExprNode();
    ExprNode rhs = visit(ctx.assign_rhs()).asExprNode();

    if (rhs != null && lhs != null) {
      Type lhsType = lhs.getType();
      Type rhsType = rhs.getType();

      semanticError |= typeCheck(ctx.assign_rhs(), lhsType, rhsType);
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
    semanticError |= typeCheck(ctx.expr(), freeStatAllowedTypes, refType);

    StatNode node = new FreeNode(ref);
    node.setScope(currSymbolTable);

    return node;
  }

  @Override
  public Node visitSkipStat(SkipStatContext ctx) {
    return new SkipNode(currSymbolTable);
  }

  @Override
  public Node visitDeclareStat(DeclareStatContext ctx) {

    ExprNode expr = visit(ctx.assign_rhs()).asExprNode();
    String varName = ctx.IDENT().getText();
    Type varType = visit(ctx.type()).asTypeDeclareNode().getType();

    if (expr != null) {
      Type exprType = expr.getType();
      semanticError |= typeCheck(ctx.assign_rhs(), varName, exprType, varType);
      /* need to set the type of the rhs expression */
      expr.setType(varType);
    }

    semanticError |= currSymbolTable.add(varName, expr);

    StatNode node = new DeclareNode(varName, expr);
    node.setScope(currSymbolTable);

    return node;
  }

  @Override
  public Node visitReturnStat(ReturnStatContext ctx) {
    ExprNode returnNum = visit(ctx.expr()).asExprNode();

    if (isMainFunction) {
      returnFromMainError(ctx);
      semanticError = true;
    }

    Type returnType = returnNum.getType();
    semanticError |= typeCheck(ctx.expr(), expectedFunctionReturn, returnType);

    StatNode node = new ReturnNode(returnNum);
    node.setScope(currSymbolTable);
    return node;
  }

  @Override
  public Node visitExitStat(ExitStatContext ctx) {
    ExprNode exitCode = visit(ctx.expr()).asExprNode();
    Type exitCodeType = exitCode.getType();

    semanticError |= typeCheck(ctx.expr(), INT_BASIC_TYPE, exitCodeType);

    StatNode node = new ExitNode(exitCode);
    node.setScope(currSymbolTable);

    return node;
  }

  /* =======================================================
   *                  Expression Visitors
   * =======================================================
   */

  @Override
  public Node visitParenExpr(ParenExprContext ctx) {
    return visit(ctx.expr());
  }

  @Override
  public Node visitArray_elem(Array_elemContext ctx) {

    String arrayIdent = ctx.IDENT().getText();
    Symbol symbol = lookUpWithNotFoundException(ctx, currSymbolTable, arrayIdent);
    ExprNode array = symbol.getExprNode();

    /* special case: if ident is not array, cannot call asArrayType on it, exit directly */
    if (typeCheck(ctx, ARRAY_TYPE, array.getType())) {
      System.exit(SEMANTIC_ERROR_CODE);
    }

    List<ExprNode> indexList = new ArrayList<>();

    Type type = array.getType();

    for (ExprContext index_ : ctx.expr()) {
      ExprNode index = visit(index_).asExprNode();
      // check every expr can evaluate to integer
      Type elemType = index.getType();
      semanticError |= typeCheck(index_, INT_BASIC_TYPE, elemType);
      indexList.add(index);

      type = type.asArrayType().getContentType();
    }

    return new ArrayElemNode(array, indexList, type, arrayIdent, symbol);
  }

  @Override
  public Node visitAndExpr(AndExprContext ctx) {
    ExprNode expr1 = visit(ctx.expr(0)).asExprNode();
    Type expr1Type = expr1.getType();
    ExprNode expr2 = visit(ctx.expr(1)).asExprNode();
    Type expr2Type = expr2.getType();

    semanticError |= typeCheck(ctx.expr(0), BOOL_BASIC_TYPE, expr1Type);
    semanticError |= typeCheck(ctx.expr(1), BOOL_BASIC_TYPE, expr2Type);

    return new BinopNode(expr1, expr2, Binop.AND);
  }

  @Override
  public Node visitOrExpr(OrExprContext ctx) {
    ExprNode expr1 = visit(ctx.expr(0)).asExprNode();
    Type expr1Type = expr1.getType();
    ExprNode expr2 = visit(ctx.expr(1)).asExprNode();
    Type expr2Type = expr2.getType();

    semanticError |= typeCheck(ctx.expr(0), BOOL_BASIC_TYPE, expr1Type);
    semanticError |= typeCheck(ctx.expr(1), BOOL_BASIC_TYPE, expr2Type);

    return new BinopNode(expr1, expr2, Binop.OR);
  }

  @Override
  public Node visitArray_liter(Array_literContext ctx) {
    int length = ctx.expr().size();
    if (length == 0) {
      return new ArrayNode(null, new ArrayList<>(), length);
    }
    ExprNode firstExpr = visit(ctx.expr(0)).asExprNode();
    Type firstContentType = firstExpr.getType();
    List<ExprNode> list = new ArrayList<>();
    for (ExprContext context : ctx.expr()) {
      ExprNode expr = visit(context).asExprNode();
      Type exprType = expr.getType();
      semanticError |= typeCheck(context, firstContentType, exprType);
      list.add(expr);
    }
    return new ArrayNode(firstContentType, list, length);
  }

  @Override
  public Node visitArray_type(Array_typeContext ctx) {
    TypeDeclareNode type;
    if (ctx.array_type() != null) {
      type = visitArray_type(ctx.array_type()).asTypeDeclareNode();
    } else if (ctx.base_type() != null) {
      type = visit(ctx.base_type()).asTypeDeclareNode();
    } else if (ctx.pair_type() != null) {
      type = visitPair_type(ctx.pair_type()).asTypeDeclareNode();
    } else {
      invalidRuleException(ctx, "visitArray_type");
      return null;
    }
    return new TypeDeclareNode(new ArrayType(type.getType()));
  }

  @Override
  public Node visitIdent(IdentContext ctx) {
    String varName = ctx.IDENT().getText();
    Symbol symbol = lookUpWithNotFoundException(ctx, currSymbolTable, varName);

    IdentNode idNode = new IdentNode(symbol.getExprNode().getType(), varName);
    idNode.setSymbol(symbol);
    return idNode;
  }

  @Override
  public Node visitNewPair(NewPairContext ctx) {
    ExprNode fst = visit(ctx.expr(0)).asExprNode();
    ExprNode snd = visit(ctx.expr(1)).asExprNode();
    return new PairNode(fst, snd);
  }

  @Override
  public Node visitStructExpr(StructExprContext ctx) {
    String name = ctx.new_struct().IDENT().getText();
    StructDeclareNode struct = globalStructTable.get(name);
    if (struct == null) {
      symbolNotFound(ctx, name);
    }
    List<ExprContext> exprContexts = ctx.new_struct().arg_list().expr();
    List<ExprNode> elemNodes = new ArrayList<>();

    for (int i = 0; i < exprContexts.size(); i++) {
      boolean lastStatus = isStruct;
      isStruct = struct.getElem(i).getType().equalToType(STRUCT_TYPE);
      ExprNode node = visit(exprContexts.get(i)).asExprNode();
      isStruct = lastStatus;
      elemNodes.add(node);
    }

    return new StructNode(elemNodes, struct.getOffsets(), struct.getSize(), name);
  }

  @Override
  public Node visitStruct_elem(Struct_elemContext ctx) {
    /* e.g. a.b.c where a is the variable name and b is the elem of a, and c is elem of a.b */
    List<TerminalNode> identifiers = ctx.IDENT();
    assert identifiers.size() >= 2;

    int curPos = 0;
    List<Integer> offsets = new ArrayList<>();
    /* this is just for printAST */
    List<String> elemNames = new ArrayList<>();
    Symbol symbol = lookUpWithNotFoundException(ctx, currSymbolTable, identifiers.get(0).getText());
    Type type = symbol.getExprNode().getType();

    do {
      semanticError |= typeCheck(ctx, STRUCT_TYPE, type);
      String structName = ((StructType) type).getName();
      StructDeclareNode struct = globalStructTable.get(structName);
      String elemName = identifiers.get(curPos + 1).getText();
      elemNames.add(elemName);
      IdentNode elemNode = struct.findElem(elemName);
      if (elemNode == null) {
        symbolNotFound(ctx, elemName);
      }
      offsets.add(struct.findOffset(elemName));
      type = elemNode.getType();
      curPos++;
    } while (curPos < identifiers.size() - 1);

    StructElemNode node = new StructElemNode(offsets, identifiers.get(0).getText(), symbol, elemNames, type);
    return node;
  }

  @Override
  public Node visitStructElemExpr(StructElemExprContext ctx) {
    return visitStruct_elem(ctx.struct_elem());
  }

  @Override
  public Node visitFunctionCall(FunctionCallContext ctx) {
    String funcName = ctx.IDENT().getText();
    FuncNode function = globalFuncTable.get(funcName);
    if (function == null) {
      symbolNotFound(ctx, funcName);
    }
    List<ExprNode> params = new ArrayList<>();

    /* check whether function has same number of parameter */
    int expectedParamNum = function.getParamList().size();
    if (expectedParamNum != 0) {
      if (ctx.arg_list() == null) {
        invalidFuncArgCount(ctx, expectedParamNum, 0);
      } else if (expectedParamNum != ctx.arg_list().expr().size()) {
        invalidFuncArgCount(ctx, expectedParamNum, ctx.arg_list().expr().size());
      }

      /* given argument number is not 0, generate list */
      int exprIndex = 0;
      for (ExprContext e : ctx.arg_list().expr()) {
        ExprNode param = visit(e).asExprNode();
        Type paramType = param.getType();
        Type targetType = function.getParamList().get(exprIndex).getType();

        /* check param types */
        semanticError |= typeCheck(ctx.arg_list().expr(exprIndex), targetType, paramType);
        params.add(param);
        exprIndex++;
      }
    }

    currSymbolTable = new SymbolTable(currSymbolTable);
    Node node = new FunctionCallNode(function, params, currSymbolTable);
    currSymbolTable = currSymbolTable.getParentSymbolTable();

    return node;
  }

  @Override
  public Node visitBoolExpr(BoolExprContext ctx) {
    return new BoolNode(ctx.BOOL_LITER().getText().equals("true"));
  }

  /* pairExpr is basically 'null', extend 'null' to represent uninitialised struct */
  @Override
  public Node visitPairExpr(PairExprContext ctx) {
    return (isStruct)? new StructNode() : new PairNode();
  }

  @Override
  public Node visitCharExpr(CharExprContext ctx) {
    String text = ctx.CHAR_LITER().getText();
    /* text for char 'a' is \'a\' length is 3
     * text for escChar like '\0' is \'\\0\' length is 4 */
    assert text.length() == 3 || text.length() == 4;
    if (text.length() == 3) {
      return new CharNode(text.charAt(1));
    } else {
      return new CharNode(escCharMap.get(text.charAt(2)));
    }
  }

  @Override
  public Node visitCmpExpr(CmpExprContext ctx) {
    String literal = ctx.bop.getText();
    Binop binop = CmpEnumMapping.get(literal);

    ExprNode expr1 = visit(ctx.expr(0)).asExprNode();
    Type expr1Type = expr1.getType();
    ExprNode expr2 = visit(ctx.expr(1)).asExprNode();
    Type expr2Type = expr2.getType();

    semanticError |= typeCheck(ctx.expr(0), cmpStatAllowedTypes, expr1Type);
    semanticError |= typeCheck(ctx.expr(1), cmpStatAllowedTypes, expr2Type);
    semanticError |= typeCheck(ctx.expr(0), expr1Type, expr2Type);

    return new BinopNode(expr1, expr2, binop);
  }

  @Override
  public Node visitEqExpr(EqExprContext ctx) {
    String literal = ctx.bop.getText();
    Binop binop = EqEnumMapping.get(literal);

    ExprNode expr1 = visit(ctx.expr(0)).asExprNode();
    Type exrp1Type = expr1.getType();
    ExprNode expr2 = visit(ctx.expr(1)).asExprNode();
    Type expr2Type = expr2.getType();

    semanticError |= typeCheck(ctx.expr(0), exrp1Type, expr2Type);

    return new BinopNode(expr1, expr2, binop);
  }

  @Override
  public Node visitIdExpr(IdExprContext ctx) {
    String name = ctx.IDENT().getText();
    Symbol symbol = lookUpWithNotFoundException(ctx, currSymbolTable, name);
    IdentNode identNode = new IdentNode(symbol.getExprNode().getType(), name);
    identNode.setSymbol(symbol);

    return identNode;
  }

  @Override
  public Node visitHexExpr(HexExprContext ctx) {
    return new IntegerNode(Integer.parseInt(ctx.HEX_LITER().getText().substring(2), 16));
  }

  @Override
  public Node visitBinaryExpr(BinaryExprContext ctx) {
    return new IntegerNode(Integer.parseInt(ctx.BINARY_LITER().getText().substring(2), 2));
  }

  @Override
  public Node visitOctalExpr(OctalExprContext ctx) {
    return new IntegerNode(Integer.parseInt(ctx.OCTAL_LITER().getText().substring(2), 8));
  }

  @Override
  public Node visitIntExpr(IntExprContext ctx) {
    return new IntegerNode(intParse(ctx, ctx.INT_LITER().getText()));
  }

  @Override
  public Node visitFstExpr(FstExprContext ctx) {
    ExprNode exprNode = visit(ctx.expr()).asExprNode();
    Type pairType = exprNode.getType();
    Type pairElemType = pairType.asPairType().getFstType();

    semanticError |= typeCheck(ctx.expr(), PAIR_TYPE, pairType);

    if (pairElemType == null) {
      invalidPairError(ctx.expr());
    }

    return new PairElemNode(exprNode, pairElemType, true);
  }

  @Override
  public Node visitSndExpr(SndExprContext ctx) {
    ExprNode exprNode = visit(ctx.expr()).asExprNode();
    Type pairType = exprNode.getType();
    Type pairElemType = pairType.asPairType().getSndType();

    semanticError |= typeCheck(ctx.expr(), PAIR_TYPE, pairType);

    if (pairElemType == null) {
      invalidPairError(ctx.expr());
    }

    return new PairElemNode(exprNode, pairElemType, false);
  }

  @Override
  public Node visitParam(ParamContext ctx) {
    TypeDeclareNode type = visit(ctx.type()).asTypeDeclareNode();
    return new IdentNode(type.getType(), ctx.IDENT().getText());
  }

  @Override
  public Node visitArithmeticExpr(ArithmeticExprContext ctx) {
    String literal = ctx.bop.getText();
    Binop binop = binopEnumMapping.get(literal);

    ExprNode expr1 = visit(ctx.expr(0)).asExprNode();
    ExprNode expr2 = visit(ctx.expr(1)).asExprNode();
    Type expr1Type = expr1.getType();
    Type expr2Type = expr2.getType();

    semanticError |= typeCheck(ctx.expr(0), INT_BASIC_TYPE, expr1Type);
    semanticError |= typeCheck(ctx.expr(1), INT_BASIC_TYPE, expr2Type);

    return new BinopNode(expr1, expr2, binop);
  }

  @Override
  public Node visitArrayExpr(ArrayExprContext ctx) {
    return visitArray_elem(ctx.array_elem());
  }

  @Override
  public Node visitStrExpr(StrExprContext ctx) {
    return new StringNode(ctx.STR_LITER().getText());
  }

  /* visit UnopExpr with a special case on the MINUS sign being the negative sign of an integer */
  @Override
  public Node visitUnopExpr(UnopExprContext ctx) {
    String literal = ctx.uop.getText();
    Unop unop = unopEnumMapping.get(literal);
    Type targetType = unopTypeMapping.get(literal);

    /* special case of MINUS: can be potentially parsed directly as a negative number(IntegerNode) */
    String exprText = ctx.expr().getText();
    if (unop.equals(Unop.MINUS) && isInteger(exprText)) {
      Integer intVal = intParse(ctx.expr(), "-" + exprText);
      return new IntegerNode(intVal);
    }

    /* Check the range of integer in the chr unary operator */
    if (unop.equals(Unop.CHR) && isInteger(exprText)) {
      Integer intVal = intParse(ctx.expr(), exprText);
      if (!isCharInRange(intVal)) {
        charOperatorRangeError(ctx.expr(), exprText);
      }
    }

    ExprNode expr = visit(ctx.expr()).asExprNode();
    Type exprType = expr.getType();
    semanticError |= typeCheck(ctx.expr(), targetType, exprType);

    return new UnopNode(expr, unop);
  }

  /* =======================================================
   *                     Type visitors
   * =======================================================
   */

  @Override
  public Node visitIntType(IntTypeContext ctx) {
    return new TypeDeclareNode(INT_BASIC_TYPE);
  }

  @Override
  public Node visitBoolType(BoolTypeContext ctx) {
    return new TypeDeclareNode(BOOL_BASIC_TYPE);
  }

  @Override
  public Node visitCharType(CharTypeContext ctx) {
    return new TypeDeclareNode(CHAR_BASIC_TYPE);
  }

  @Override
  public Node visitStringType(StringTypeContext ctx) {
    return new TypeDeclareNode(STRING_BASIC_TYPE);
  }

  @Override
  public Node visitArrayType(ArrayTypeContext ctx) {
    return visitArray_type(ctx.array_type());
  }

  @Override
  public Node visitPairType(PairTypeContext ctx) {
    return visitPair_type(ctx.pair_type());
  }

  @Override
  public Node visitPairElemPairType(PairElemPairTypeContext ctx) {
    return new TypeDeclareNode(new PairType());
  }

  @Override
  public Node visitPair_type(Pair_typeContext ctx) {
    TypeDeclareNode leftChild = visit(ctx.pair_elem_type(0)).asTypeDeclareNode();
    TypeDeclareNode rightChild = visit(ctx.pair_elem_type(1)).asTypeDeclareNode();
    Type type = new PairType(leftChild.getType(), rightChild.getType());
    return new TypeDeclareNode(type);
  }

  @Override
  public Node visitStructType(StructTypeContext ctx) { return visitStruct_type(ctx.struct_type()); }

  @Override
  public Node visitStruct_type(Struct_typeContext ctx) {
    String name = ctx.IDENT().getText();
    if (!globalStructTable.containsKey(name)) {
      symbolNotFound(ctx, name);
    }
    return new TypeDeclareNode(new StructType(name));
  }
}
