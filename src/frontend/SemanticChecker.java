package frontend;

import frontend.antlr.WACCLexer;
import frontend.antlr.WACCParser;
import frontend.antlr.WACCParser.*;
import frontend.antlr.WACCParserBaseVisitor;

import frontend.node.StructDeclareNode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

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
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import utils.frontend.ParserErrorHandler;

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

  /* where the compiling file lives, used for determine include file */
  private String path;

  /* recording the current SymbolTable during parser tree visits */
  private SymbolTable currSymbolTable;

  /* global data struct table, used to record all struct */
  private final Map<String, StructDeclareNode> globalStructTable;

  /* global function table, used to record all functions */
  private final Map<String, FuncNode> globalFuncTable;

  /* used after function declare step, to detect RETURN statement in main body */
  private boolean isMainFunction;

  /* used in determining whether branching statement is legal, i.e. break/continue is within a loop/switch */
  private Stack<Boolean> isBreakAllowed;
  private Stack<Boolean> isContinueAllowed;
  private Stack<Boolean> isJumpRepeated;
  private Stack<JumpContext> jumpContext;
  /* record the for-loop incrementer so that break and continue know what to do before jumping */
  private Stack<StatNode> currForLoopIncrementBreak;
  private Stack<StatNode> currForLoopIncrementContinue;

  /* used only in function declare step, to check function has the correct return type */
  private Type expectedFunctionReturn;

  /* record whether a skipable semantic error is found in visiting to support checking of multiple errors */
  private boolean semanticError;

  /* record which file have already been included, IN the current import chain */
  private Set<String> libraryCollection;

  /* record all files that have been imported */
  private static Set<String> allImportCollection = new HashSet<>();

  /* for function overload */
  private Set<String> overloadFuncNames = new HashSet<>();


  /* indicate the architecture */
  private AssemblyArchitecture arch;

  /* constructor of SemanticChecker */
  public SemanticChecker(Set<String> libraryCollection, AssemblyArchitecture arch) {
    currSymbolTable = null;
    globalStructTable = new HashMap<>();
    globalFuncTable = new HashMap<>();
    isMainFunction = false;

    isBreakAllowed = new Stack<>();
    isBreakAllowed.push(false);

    isContinueAllowed = new Stack<>();
    isContinueAllowed.push(false);

    jumpContext = new Stack<>();

    isJumpRepeated = new Stack<>();
    isJumpRepeated.push(false);

    expectedFunctionReturn = null;
    currForLoopIncrementBreak = new Stack<>();
    currForLoopIncrementContinue = new Stack<>();
    this.libraryCollection = libraryCollection;
    this.arch = arch;
  }

  public void setPath(String path) {
    this.path = path;
  }

  @Override
  public Node visitProgram(ProgramContext ctx) {

    /* add all import file's struct and function into this program's struct and function */
    for (Import_fileContext importFile : ctx.import_file()) {
      visit(importFile);
    }

    visitDeclaration(ctx.declaration());

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

    return new ProgramNode(globalFuncTable, globalStructTable, body);
  }

  @Override
  public Node visitDeclaration(DeclarationContext ctx) {

    /* add the struct name in order to have recursive data struct */
    for (StructContext s : ctx.struct()) {
      String structName = s.IDENT().getText();

      if (globalStructTable.containsKey(structName)) {
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

    /* check function overload */
    Set<String> tempStore = new HashSet<>();
    for (FuncContext f : ctx.func()) {
      String funcName = f.IDENT().getText();
      if (tempStore.contains(funcName)) {
        overloadFuncNames.add(funcName);
      }
      tempStore.add(funcName);
    }

    /* add the identifiers and parameter list of functions in the globalFuncTable first */
    for (FuncContext f : ctx.func()) {
      String funcName = f.IDENT().getText();

      /* if the func name is overloading, append the types of all param */
      if (overloadFuncNames.contains(funcName)) {
        funcName = findOverloadFuncName(f);
      }

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

      /* store the text func name because ASTPrinter need to use it */
      FuncNode node = new FuncNode(f.IDENT().getText(), returnType, param_list);

      /* store the overload func name as well if necessary */
      if (!funcName.equals(f.IDENT().getText())) {
        node.setOverloadName(funcName);
      }

      globalFuncTable.put(funcName, node);
    }

    /* then iterate through a list of function declarations to visit the function body */
    for (FuncContext f : ctx.func()) {
      String funcName = f.IDENT().getText();
      if (overloadFuncNames.contains(funcName)) {
        funcName = findOverloadFuncName(f);
      }

      StatNode functionBody = visitFunc(f).asStatNode();

      /* if the function declaration is not terminated with a return/exit statement, then throw the semantic error */
      if (!functionBody.leaveAtEnd()) {
        invalidFunctionReturnExit(ctx, funcName);
      }

      globalFuncTable.get(funcName).setFunctionBody(functionBody);
    }

    /* same as visit import, all information in map */
    return null;
  }

  @Override
  public Node visitImport_file(Import_fileContext ctx) {
    visitImport(ctx, path + ctx.FILE_NAME().getText());
    return null;
  }


  public void visitImport(ParserRuleContext ctx, String importFile) {
    /* detect circular import */
    if (libraryCollection.contains(importFile)) {
      importFileErrorException(ctx, "circular import of " + importFile);
      return ;
    }
    libraryCollection.add(importFile);

    /* do not import file that has already been import */
    if (allImportCollection.contains(importFile)) {
      return ;
    }
    allImportCollection.add(importFile);

    /* get library's struct and functions, put in current program's struct and func list */
    File file = new File(importFile);

    try (FileInputStream fis = new FileInputStream(file)) {
      // Input stream of the file
      CharStream input = CharStreams.fromStream(fis);
      // Pass the input stream of the file to WACC lexer
      WACCLexer lexer = new WACCLexer(input);
      // Obtain the internal tokens from the lexer
      CommonTokenStream tokens = new CommonTokenStream(lexer);
      // Parse the tokens into a syntax tree
      WACCParser parser = new WACCParser(tokens);
      parser.setErrorHandler(new ParserErrorHandler());
      // Start parsing using the `library` rule defined in antlr_config/WACCParser.g4
      LibraryContext tree = parser.library();

      SemanticChecker semanticChecker = new SemanticChecker(libraryCollection, this.arch);
      semanticChecker.setPath(file.getParent() + "/");
      ProgramNode libraryContent = (ProgramNode) semanticChecker.visitLibrary(tree);

      /* add struct into current program's struct list */
      addAllContent(ctx, libraryContent.getStructTable(), globalStructTable);

      /* add struct into current program's struct list */
      addAllContent(ctx, libraryContent.getFunctions(), globalFuncTable);

    } catch (FileNotFoundException e) {
      importFileErrorException(ctx, "import file '" + importFile + "' is not found.");
    } catch (IOException e) {
      importFileErrorException(ctx, "IOException has been raised in visitImport");
    }

    /* remove this import file from dfs import set
    *  meaning allowing import collision, 2 .hwacc file import the third same .hwacc file */
    libraryCollection.remove(importFile);
  }

  /* add all content in given struct/func list into the given current program's list */
  private <T> void addAllContent(ParserRuleContext ctx, Map<String, T> map, Map<String, T> target) {
    for (Map.Entry<String, T> structEntry : map.entrySet()) {
      /* do not allow multiple elements have the same name */
      String structName = structEntry.getKey();
      if (globalStructTable.containsKey(structName)) {
        symbolRedeclared(ctx, structName);
        semanticError = true;
      }
      target.put(structName, structEntry.getValue());
    }
  }

  @Override
  public Node visitLibrary(LibraryContext ctx) {
    for (Import_fileContext importFile : ctx.import_file()) {
      visit(importFile);
    }

    visitDeclaration(ctx.declaration());

    /* program node is used for return func and struct table, no body returned,
     * scope set as null will not be visit by any visitor */
    return new ProgramNode(globalFuncTable, globalStructTable, new SkipNode(null));
  }

  @Override
  public Node visitStruct(StructContext ctx) {
    List<IdentNode> elements = new ArrayList<>();
    Set<String> elemNameSet = new HashSet<>();

    for (ParamContext elem : ctx.param_list().param()) {
      Type elemType = visit(elem.type()).asTypeDeclareNode().getType();
      String elemName = elem.IDENT().getText();
      /* do not allow multiple elements have the same name in the struct */
      if (elemNameSet.contains(elemName)) {
        symbolRedeclared(ctx, elemName);
        semanticError = true;
      }
      elemNameSet.add(elemName);
      IdentNode elemNode = new IdentNode(elemType, elemName);
      elements.add(elemNode);
    }

    return new StructDeclareNode(elements, ctx.IDENT().getText());
  }

  @Override
  public Node visitFunc(FuncContext ctx) {
    String funcName = ctx.IDENT().getText();
    if (overloadFuncNames.contains(funcName)) {
      funcName = findOverloadFuncName(ctx);
    }
    FuncNode funcNode = globalFuncTable.get(funcName);

    /* visit the function body */
    expectedFunctionReturn = funcNode.getReturnType();
    currSymbolTable = new SymbolTable(currSymbolTable);

    /* ARM: initialise as -4 byte in order to leave space for PUSH {lr},
       which takes up 4 bute on stack
       intel: rbp is assigned after push, so all */
    int tempStackAddr = this.arch.equals(AssemblyArchitecture.ARMv6) ? -WORD_SIZE : -QUAD_SIZE;
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
      ((ScopeNode) functionBody).setAvoidSubStack();
      return functionBody;
    }
    ScopeNode enclosedBody = new ScopeNode(functionBody);
    enclosedBody.setAvoidSubStack();
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

    /* both branch are permitted to have/not have jump */
    boolean permitJump = isJumpRepeated.peek();

    /* create the StatNode for the if body and gegerate new child scope */
    currSymbolTable = new SymbolTable(currSymbolTable);
    StatNode ifBody = visit(ctx.stat(0)).asStatNode();
    currSymbolTable = currSymbolTable.getParentSymbolTable();

    /* restore permit/not permit jump */
    isJumpRepeated.pop();
    isJumpRepeated.push(permitJump);

    /* create the StatNode for the else body and generate new child scope */
    StatNode elseBody = null;
    if (ctx.stat(1) != null) {
      currSymbolTable = new SymbolTable(currSymbolTable);
      elseBody = visit(ctx.stat(1)).asStatNode();
      currSymbolTable = currSymbolTable.getParentSymbolTable();
    }

    /* restore permit/not permit jump */
    isJumpRepeated.pop();
    isJumpRepeated.push(permitJump);

    StatNode realIfBody = ifBody instanceof ScopeNode ? ifBody : new ScopeNode(ifBody);
    StatNode realElseBody = null;
    if (elseBody != null) {
      realElseBody = elseBody instanceof ScopeNode ? elseBody : new ScopeNode(elseBody);
    }

    StatNode node = new IfNode(condition, realIfBody, realElseBody);

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

    /* set context for WHILE */
    isJumpRepeated.push(false);
    isBreakAllowed.push(true);
    isContinueAllowed.push(true);
    jumpContext.push(JumpContext.WHILE);

    StatNode body = visit(ctx.stat()).asStatNode();
    currSymbolTable = currSymbolTable.getParentSymbolTable();

    /* reset context to parent scope */
    isJumpRepeated.pop();
    isBreakAllowed.pop();
    isContinueAllowed.pop();
    jumpContext.pop();

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

    /* set contexts for DOWHILE */
    isJumpRepeated.push(false);
    isBreakAllowed.push(true);
    isContinueAllowed.push(true);
    jumpContext.push(JumpContext.WHILE);

    StatNode body = visit(ctx.stat()).asStatNode();

    /* reset context to parent scope */
    isJumpRepeated.pop();
    isBreakAllowed.pop();
    isContinueAllowed.pop();
    jumpContext.pop();

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
    
    ExprNode cond = visit(ctx.expr()).asExprNode();
    StatNode increment = visit(ctx.for_stat(1)).asStatNode();

    currSymbolTable = new SymbolTable(currSymbolTable);

    /* set break/continue context */
    isJumpRepeated.push(false);    // on entry to for body, no JUMP instruction executed
    isBreakAllowed.push(true);     // FOR allow break
    isContinueAllowed.push(true);  // FOR allow continue
    jumpContext.push(JumpContext.FOR);   // BREAK and CONTINUE are in FOR context
    currForLoopIncrementContinue.push(increment); // When continue occur in FOR, execute increment

    /* visit the for loop body */
    StatNode body = visit(ctx.stat()).asStatNode();

    /* restore contexts */
    isJumpRepeated.pop();
    isBreakAllowed.pop();
    isContinueAllowed.pop();
    jumpContext.pop();
    currForLoopIncrementContinue.pop();

    StatNode _body = body instanceof ScopeNode ? body : new ScopeNode(body);
    body.setScope(currSymbolTable);

    currSymbolTable = currSymbolTable.getParentSymbolTable();

    ScopeNode _init = init instanceof ScopeNode ? (ScopeNode) init : new ScopeNode(init);
    _init.setAvoidSubStack();
    ScopeNode _increment = increment instanceof ScopeNode ? (ScopeNode) increment : new ScopeNode(increment);
    _increment.setAvoidSubStack();
    // StatNode _body = body instanceof ScopeNode ? body : new ScopeNode(body);

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
    if (isJumpRepeated.peek()) {
      branchStatementMutipleError(ctx, JumpType.BREAK);
    }
    if (!isBreakAllowed.peek()) {
      branchStatementPositionError(ctx, JumpType.BREAK);
    }
    
    // break does not involve any instruction after exit a loop
    StatNode breakNode = new JumpNode(JumpType.BREAK, null, jumpContext.peek());  
    breakNode.setScope(currSymbolTable);
    isJumpRepeated.pop();
    isJumpRepeated.push(true);
    return breakNode;
  }

  @Override
  public Node visitContinueStat(ContinueStatContext ctx) {
    if (isJumpRepeated.peek()) {
      branchStatementMutipleError(ctx, JumpType.CONTINUE);
    }
    if (!isContinueAllowed.peek()) {
      branchStatementPositionError(ctx, JumpType.CONTINUE);
    }
    StatNode continueNode = new JumpNode(JumpType.CONTINUE, null, jumpContext.peek());
    continueNode.setScope(currSymbolTable);
    isJumpRepeated.pop();
    isJumpRepeated.push(true);
    return continueNode;
  }

  @Override
  public Node visitSwitchStat(SwitchStatContext ctx) {
    ExprNode expr = visit(ctx.expr(0)).asExprNode();

    int numOfCases = ctx.expr().size();
    List<CaseStat> cases = new ArrayList<>();
    SymbolTable switchSymbolTable = new SymbolTable(currSymbolTable);

    /* switch allow break, allow continue if switch is in for */
    isBreakAllowed.push(true);
    isJumpRepeated.push(false);

    jumpContext.push(JumpContext.SWITCH);

    StatNode defaultCase = visit(ctx.stat(numOfCases - 1)).asStatNode();
    
    defaultCase.setScope(switchSymbolTable);

    for (int i = 1; i < numOfCases; i++) {
      ExprNode caseExpr = visit(ctx.expr(i)).asExprNode();

      currSymbolTable = switchSymbolTable;
      isJumpRepeated.push(false);
      StatNode caseNode = visit(ctx.stat(i - 1)).asStatNode();
      caseNode.setScope(currSymbolTable);
      isJumpRepeated.pop();
      currSymbolTable = currSymbolTable.getParentSymbolTable();

      CaseStat caseStat = new CaseStat(caseExpr, caseNode);
      cases.add(caseStat);
    }

    isBreakAllowed.pop();
    jumpContext.pop();
    isJumpRepeated.pop();

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

    return new BinopNode(expr1, expr2, Binop.AND, this.arch);
  }

  @Override
  public Node visitOrExpr(OrExprContext ctx) {
    ExprNode expr1 = visit(ctx.expr(0)).asExprNode();
    Type expr1Type = expr1.getType();
    ExprNode expr2 = visit(ctx.expr(1)).asExprNode();
    Type expr2Type = expr2.getType();

    semanticError |= typeCheck(ctx.expr(0), BOOL_BASIC_TYPE, expr1Type);
    semanticError |= typeCheck(ctx.expr(1), BOOL_BASIC_TYPE, expr2Type);

    return new BinopNode(expr1, expr2, Binop.OR, this.arch);
  }

  @Override
  public Node visitBitwiseExpr(BitwiseExprContext ctx) {
    ExprNode expr1 = visit(ctx.expr(0)).asExprNode();
    Type expr1Type = expr1.getType();
    ExprNode expr2 = visit(ctx.expr(1)).asExprNode();
    Type expr2Type = expr2.getType();

    semanticError |= typeCheck(ctx.expr(0), INT_BASIC_TYPE, expr1Type);
    semanticError |= typeCheck(ctx.expr(1), INT_BASIC_TYPE, expr2Type);

    String literal = ctx.bop.getText();
    Binop binop = bitwiseOpEnumMapping.get(literal);

    return new BinopNode(expr1, expr2, binop, this.arch);
  }

  @Override
  public Node visitArray_liter(Array_literContext ctx) {
    int length = ctx.expr().size();
    if (length == 0) {
      return new ArrayNode(null, new ArrayList<>(), length, this.arch);
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
    return new ArrayNode(firstContentType, list, length, this.arch);
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
    return new TypeDeclareNode(new ArrayType(type.getType(), this.arch));
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
    return new PairNode(fst, snd, this.arch);
  }

  @Override
  public Node visitStructExpr(StructExprContext ctx) {
    String name = ctx.new_struct().IDENT().getText();
    StructDeclareNode struct = globalStructTable.get(name);

    /* check whether the struct existed or not */
    if (struct == null) {
      symbolNotFound(ctx, name);
    }

    /* check the number of elements */
    int expectedElemNum = struct.getElemCount();
    if (expectedElemNum != 0) {
      if (ctx.new_struct().arg_list() == null) {
        invalidFuncArgCount(ctx, expectedElemNum, 0);
      } else if (expectedElemNum != ctx.new_struct().arg_list().expr().size()) {
        invalidFuncArgCount(ctx, expectedElemNum, ctx.new_struct().arg_list().expr().size());
      }
    }

    List<ExprContext> exprContexts = ctx.new_struct().arg_list().expr();
    List<ExprNode> elemNodes = new ArrayList<>();

    /* visit each expr and add ExprNode into the list */
    for (int i = 0; i < exprContexts.size(); i++) {
      ExprNode node = visit(exprContexts.get(i)).asExprNode();
      /* check that the elem's type is correct */
      semanticError |= typeCheck(ctx, struct.getElem(i).getType(), node.getType());
      elemNodes.add(node);
    }

    return new StructNode(elemNodes, struct.getOffsets(), struct.getSize(), name, this.arch);
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
    /* lookup the first identifier (variable name) from the symbol table */
    Symbol symbol = lookUpWithNotFoundException(ctx, currSymbolTable, identifiers.get(0).getText());
    Type type = symbol.getExprNode().getType();

    do {
      /* check that the type is struct in the current position */
      if (typeCheck(ctx, STRUCT_TYPE, type)) {
        semanticError = true;
        break;
      }
      /* get the actual struct name of the current type and lookup info from global struct table */
      String structName = ((StructType) type).getName();
      StructDeclareNode struct = globalStructTable.get(structName);
      /* get the elemName behind dot */
      String elemName = identifiers.get(curPos + 1).getText();
      elemNames.add(elemName);
      IdentNode elemNode = struct.findElem(elemName);
      /* check the elemName behind dot existed for the current struct */
      if (elemNode == null) {
        symbolNotFound(ctx, elemName);
      }
      offsets.add(struct.findOffset(elemName));
      /* advance */
      type = elemNode.getType();
      curPos++;
    } while (curPos < identifiers.size() - 1);

    return new StructElemNode(offsets, identifiers.get(0).getText(), symbol, elemNames, type);
  }

  @Override
  public Node visitStructElemExpr(StructElemExprContext ctx) {
    return visitStruct_elem(ctx.struct_elem());
  }

  @Override
  public Node visitEmptyStructExpr(EmptyStructExprContext ctx) {
    return new StructNode();
  }

  @Override
  public Node visitFunctionCall(FunctionCallContext ctx) {
    String funcName = ctx.IDENT().getText();

    if (overloadFuncNames.contains(funcName)) {
      if (ctx.arg_list() != null) {
        for (ExprContext e : ctx.arg_list().expr()) {
          funcName += (overloadSeparator + visit(e).asExprNode().getType().toString());
        }
      }
      funcName = formatFuncName(funcName);
    }

    FuncNode function = globalFuncTable.get(funcName);
    if (function == null && (funcName.contains("null")) || funcName.contains("empty")) {
      overloadUnclearTypeError(ctx);
    } else if (function == null) {
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
    return new BoolNode(ctx.BOOL_LITER().getText().equals("true"), this.arch);
  }

  @Override
  public Node visitPairExpr(PairExprContext ctx) {
    return new PairNode(this.arch);
  }

  @Override
  public Node visitCharExpr(CharExprContext ctx) {
    String text = ctx.CHAR_LITER().getText();
    /* text for char 'a' is \'a\' length is 3
     * text for escChar like '\0' is \'\\0\' length is 4 */
    assert text.length() == 3 || text.length() == 4;
    if (text.length() == 3) {
      return new CharNode(text.charAt(1), this.arch);
    } else {
      return new CharNode(escCharMap.get(text.charAt(2)), this.arch);
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

    return new BinopNode(expr1, expr2, binop, this.arch);
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

    return new BinopNode(expr1, expr2, binop, this.arch);
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
    return new IntegerNode(Integer.parseInt(ctx.HEX_LITER().getText().substring(2), 16), this.arch);
  }

  @Override
  public Node visitBinaryExpr(BinaryExprContext ctx) {
    return new IntegerNode(Integer.parseInt(ctx.BINARY_LITER().getText().substring(2), 2), this.arch);
  }

  @Override
  public Node visitOctalExpr(OctalExprContext ctx) {
    return new IntegerNode(Integer.parseInt(ctx.OCTAL_LITER().getText().substring(2), 8), this.arch);
  }

  @Override
  public Node visitIntExpr(IntExprContext ctx) {
    return new IntegerNode(intParse(ctx, ctx.INT_LITER().getText()), this.arch);
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

    return new BinopNode(expr1, expr2, binop, this.arch);
  }

  @Override
  public Node visitArrayExpr(ArrayExprContext ctx) {
    return visitArray_elem(ctx.array_elem());
  }

  @Override
  public Node visitStrExpr(StrExprContext ctx) {
    return new StringNode(ctx.STR_LITER().getText(), this.arch);
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
      return new IntegerNode(intVal, this.arch);
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

    return new UnopNode(expr, unop, this.arch);
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
    return new TypeDeclareNode(new PairType(this.arch));
  }

  @Override
  public Node visitPair_type(Pair_typeContext ctx) {
    TypeDeclareNode leftChild = visit(ctx.pair_elem_type(0)).asTypeDeclareNode();
    TypeDeclareNode rightChild = visit(ctx.pair_elem_type(1)).asTypeDeclareNode();
    Type type = new PairType(leftChild.getType(), rightChild.getType(), this.arch);
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
    return new TypeDeclareNode(new StructType(name, this.arch));
  }
}
