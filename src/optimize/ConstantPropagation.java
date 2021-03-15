package optimize;

import frontend.node.FuncNode;
import frontend.node.Node;
import frontend.node.ProgramNode;
import frontend.node.StructDeclareNode;
import frontend.node.expr.*;
import frontend.node.stat.*;
import utils.NodeVisitor;
import utils.frontend.symbolTable.Symbol;

import javax.swing.text.html.parser.Entity;
import java.lang.reflect.Array;
import java.util.*;

import static utils.Utils.*;

public class ConstantPropagation implements NodeVisitor<Node> {
  /* map an ident with its value exprNode
  *  used for the current visit of ident node */
  private Map<Symbol, ExprNode> identValMap;

  /* map of SYMBOL with the list of SYMBOL that depend on it */
  private Map<Symbol, List<Symbol>> dependentMap;
  /* list of SYMBOL of variables that right hand side contains */
  private List<Symbol> dependentList;

  /* list of ident map, which will be merged in the end of the CURRENT while, if, switch, for loop
  *    loopStartMapList is maplist that can reach while condition check
  *    breakMapList is all mapList copy of each time a BREAK is called */
  private List<Map<Symbol, ExprNode>> loopStartMapList, breakMapList;

  public ConstantPropagation() {
    identValMap = new HashMap<>();
    this.loopStartMapList = new ArrayList<>();
    this.breakMapList = new ArrayList<>();
    this.dependentMap = new HashMap<>();
  }

  @Override
  public Node visitArrayElemNode(ArrayElemNode node) {
    /* only simplify all indexes, nothing else to be changed */
    List<ExprNode> params = simplifyExprList(node.getIndex());
    return new ArrayElemNode(node.getArray(), params, node.getType(), node.getName(), node.getSymbol());
  }

  @Override
  public Node visitArrayNode(ArrayNode node) {
    /* only simplify all contents, nothing else to be changed */
    List<ExprNode> content = simplifyExprList(node.getContent());
    return new ArrayNode(node.getType().asArrayType().getContentType(), content, node.getLength());
  }

  @Override
  public Node visitBinopNode(BinopNode node) {
    ExprNode expr1 = visit(node.getExpr1()).asExprNode();
    ExprNode expr2 = visit(node.getExpr2()).asExprNode();

    /* if either of the nodes is not immediate, stop constant propagation
    *  return a node with so far the simplified form */
    if (!expr1.isImmediate() || !expr2.isImmediate()) {
      return new BinopNode(expr1, expr2, node.getOperator());
    }

    /*  apply arithmetic evaluation */
    if (arithmeticApplyMap.containsKey(node.getOperator())) {
      ExprNode simpChild = arithmeticApplyMap.get(node.getOperator()).apply(
              expr1.getCastedVal(),
              expr2.getCastedVal());
      return simpChild == null ? new BinopNode(expr1, expr2, node.getOperator()) : simpChild;
    }

    /* otherwise, have to be binop covered by cmpMap key */
    assert cmpMap.containsKey(node.getOperator());

    boolean val = cmpMap.get(node.getOperator()).apply(expr1.getCastedVal(), expr2.getCastedVal());
    return new BoolNode(val);
  }

  @Override
  public Node visitBoolNode(BoolNode node) {
    return node;
  }

  @Override
  public Node visitCharNode(CharNode node) {
    return node;
  }

  @Override
  public Node visitFunctionCallNode(FunctionCallNode node) {
    List<ExprNode> params = simplifyExprList(node.getParams());
    return new FunctionCallNode(node.getFunction(), params, node.getFuncSymbolTable());
  }

  @Override
  public Node visitIdentNode(IdentNode node) {
    if (identValMap.containsKey(node.getSymbol())) {
      dependentList.add(node.getSymbol());
      return identValMap.get(node.getSymbol());
    }
    return node;
  }

  @Override
  public Node visitIntegerNode(IntegerNode node) {
    return node;
  }

  @Override
  public Node visitPairElemNode(PairElemNode node) {
    /* from WACC language definition, can never call fst newpair(1, 2)
    *  expr contained can never be immediate */
    return node;
  }

  @Override
  public Node visitPairNode(PairNode node) {

    /* if one child is null, the pair node is null, no simplify */
    if (node.getFst() == null) {
      return node;
    }

    ExprNode expr1 = visit(node.getFst()).asExprNode();
    ExprNode expr2 = visit(node.getSnd()).asExprNode();

    return new PairNode(expr1, expr2);
  }

  @Override
  public Node visitStringNode(StringNode node) {
    return node;
  }

  @Override
  public Node visitUnopNode(UnopNode node) {

    assert unopApplyMap.containsKey(node.getOperator());

    /* visit expr first, ensure child expr have already been simplified */
    ExprNode expr = visit(node.getExpr()).asExprNode();
    if (!expr.isImmediate()) {
      return new UnopNode(expr, node.getOperator());
    }
    ExprNode simpChild = unopApplyMap.get(node.getOperator()).apply(expr);
    /* return null when overflow */
    return simpChild == null ?
            new UnopNode(expr, node.getOperator()) :
            simpChild;
  }

  @Override
  public Node visitAssignNode(AssignNode node) {
    dependentList = new ArrayList<>();
    ExprNode exprNode;
    
    /* if lhs is IDENT, delete it first, 
     * so that if lhs IDENT is contained in right hand side, 
           and if assign is in loop, 
           this assign could update value of IDENT from last iterate */
    if (node.getLhs() instanceof IdentNode) {
      Symbol lhsSymbol = ((IdentNode) node.getLhs()).getSymbol();
      /* remove prev map of symbol
         and all other symbol that depend on prev value of this updated SYMBOL */
      removeSymbol(lhsSymbol);

      exprNode = visit(node.getRhs()).asExprNode();

      if (exprNode.isImmediate()) {
        /* add in updated value */
        identValMap.put(lhsSymbol, exprNode);
        addDependent(lhsSymbol, dependentList);
      }

    /* else, lhs cannot be added into dependMap or identMap, 
       simply simplified rhs */
    } else {
      exprNode = visit(node.getRhs()).asExprNode();
    }
    
    AssignNode resultNode = new AssignNode(node.getLhs(), exprNode);
    resultNode.setScope(node.getScope());
    return resultNode;
  }

  @Override
  public Node visitDeclareNode(DeclareNode node) {
    dependentList = new ArrayList<>();
    ExprNode exprNode = visit(node.getRhs()).asExprNode();
    DeclareNode resultNode = new DeclareNode(node.getIdentifier(), exprNode);
    resultNode.setScope(node.getScope());

    /* constant propagation:
    *  add new entry in map */
    if (exprNode.isImmediate()) {
      Symbol lhsSymbol = node.getScope().lookup(node.getIdentifier());
      identValMap.put(lhsSymbol, exprNode);
      addDependent(lhsSymbol, dependentList);
    }
    return resultNode;
  }

  /* remove the symbol from current identValMap
  *  and remove all SYMBOL that depend on this SYMBOL */
  private void removeSymbol(Symbol symbol) {
    /* IMPORTANT: have to remove this symbol first, prevent circular remove, 
                  i.e a = a + ..., not remove a first will result in live-lock */
    identValMap.remove(symbol);
    List<Symbol> childSymbols = dependentMap.get(symbol);

    dependentMap.remove(symbol);
    if (dependentMap.containsKey(symbol)) {
      for (Symbol child : childSymbols) {
        removeSymbol(child);
      }
    }
  }

  /* add lhsSymbol as symbol that depend on each rhsSymbols */
  private void addDependent(Symbol lhsSymbol, List<Symbol> rhsSymbols) {
    for (Symbol rhsSymbol : rhsSymbols) {
      if (dependentMap.containsKey(rhsSymbol)) {
        dependentMap.get(rhsSymbol).add(lhsSymbol);
      }
    }
  }

  @Override
  public Node visitExitNode(ExitNode node) {
    ExitNode resultNode = new ExitNode(visit(node.getValue()).asExprNode());
    resultNode.setScope(node.getScope());
    return resultNode;
  }

  @Override
  public Node visitFreeNode(FreeNode node) {
    /* free is expected to have to be pointer, optimise here is expected no effect */
    FreeNode resultNode = new FreeNode(visit(node.getExpr()).asExprNode());
    resultNode.setScope(node.getScope());
    return resultNode;
  }

  @Override
  public Node visitIfNode(IfNode node) {
    /* constant propagation algorithm:
    *  go through if and else body, generate symbol and exprNode map for both,
    *  merge two tables by taking intersection  */

    /* 1 visit cond, record map before enter if body */
    ExprNode cond = visit(node.getCond()).asExprNode();
    Map<Symbol, ExprNode> oldMap = new HashMap<>(identValMap);

    /* 2 visit if body */
    StatNode ifBody = visit(node.getIfBody()).asStatNode();
    Map<Symbol, ExprNode> ifIdMap = new HashMap<>(identValMap);

    /* 3 visit else body */
    identValMap = oldMap;
    StatNode elseBody = visit(node.getElseBody()).asStatNode();

    /* 4 new idMap is intersection of both */
    List<Map<Symbol, ExprNode>> list = new ArrayList<>();
    list.add(ifIdMap);
    mergeIdMap(list);

    /* return new ifNode with propagated result */
    IfNode resultNode = new IfNode(cond, ifBody, elseBody);
    resultNode.setScope(node.getScope());
    return resultNode;
  }

  @Override
  public Node visitPrintlnNode(PrintlnNode node) {
    PrintlnNode resultNode = new PrintlnNode(visit(node.getExpr()).asExprNode());
    resultNode.setScope(node.getScope());
    return resultNode;
  }

  @Override
  public Node visitPrintNode(PrintNode node) {
    PrintNode resultNode = new PrintNode(visit(node.getExpr()).asExprNode());
    resultNode.setScope(node.getScope());
    return resultNode;
  }

  @Override
  public Node visitReadNode(ReadNode node) {
    ExprNode expr = node.getInputExpr();
    /* delete expr in identMap, since cannot determine expr's value */
    if (expr instanceof IdentNode) {
      identValMap.remove(((IdentNode) expr).getSymbol());
    }
    return node;
  }

  @Override
  public Node visitReturnNode(ReturnNode node) {
    ReturnNode resultNode = new ReturnNode(visit(node.getExpr()).asExprNode());
    resultNode.setScope(node.getScope());
    return resultNode;
  }

  @Override
  public Node visitScopeNode(ScopeNode node) {
    List<StatNode> body = new ArrayList<>();
    for (StatNode statNode : node.getBody()) {
      body.add(visit(statNode).asStatNode());
    }
    return new ScopeNode(node, body);
  }

  @Override
  public Node visitSkipNode(SkipNode node) {
    return node;
  }

  @Override
  public Node visitWhileNode(WhileNode node) {
    /* constant propagation algorithm:
     *  visit body twice, get intersection with the first iterate */

    /* 1 record the mapList, which might contain maps from parent WHILE
     *      current identMap is one map of parent block of while block */
    List<Map<Symbol, ExprNode>>
            oldLoopStartMapList = loopStartMapList,
            oldBreakMapList = breakMapList;
    loopStartMapList = new ArrayList<>();
    breakMapList = new ArrayList<>();
    loopStartMapList.add(new HashMap<>(identValMap));

    /* 2 visit body once, get those idents that won't change after one visit
     *    ignore body generated
     *   loop start map is merged, result is map parent of loop body and block after loop
     *   break is then merged, result is map before block after loop
     *   merged start/end map are used to record map, prevent overwrite in second visit */
    visit(node.getBody()).asStatNode();
    mergeIdMap(loopStartMapList);
    Map<Symbol, ExprNode> mergedStartMap = new HashMap<>(identValMap);
    mergeIdMap(breakMapList);
    Map<Symbol, ExprNode> mergedEndMap = new HashMap<>(identValMap);
    identValMap = mergedStartMap;

    /* 3 visit again using map after getting the intersection */
    ExprNode cond = visit(node.getCond()).asExprNode();
    StatNode body = visit(node.getBody()).asStatNode();
    identValMap = mergedEndMap;

    /* 4 restore mapList */
    loopStartMapList = oldLoopStartMapList;
    breakMapList = oldBreakMapList;

    WhileNode resultNode = new WhileNode(cond, body);
    resultNode.setScope(node.getScope());
    return resultNode;
  }

  @Override
  public Node visitFuncNode(FuncNode node) {
    StatNode newFunctionBody = visit(node.getFunctionBody()).asStatNode();
    node.setFunctionBody(newFunctionBody);
    return node;

  }

  @Override
  public Node visitProgramNode(ProgramNode node) {
    Map<String, FuncNode> functions = new HashMap<>();
    for (Map.Entry<String, FuncNode> entry : node.getFunctions().entrySet()) {
      /* explicit call visitFuncNode, can call cast */
      functions.put(entry.getKey(), (FuncNode) visitFuncNode(entry.getValue()));
    }
    StatNode body = visit(node.getBody()).asStatNode();
    return new ProgramNode(functions, node.getStructTable(), body);
  }

  /* debug function, show content of idMap */
  private void showIdMap() {
    showIdMap(identValMap);
  }

  private void showIdMap(Map<Symbol, ExprNode> map) {
    for (Map.Entry<Symbol, ExprNode> entry : map.entrySet()) {
      System.out.println(entry.getKey() + " casted as " + entry.getValue().getCastedVal());
    }
  }

  /* helper function for simplify al list of expr
  *  can only be placed in optimise, since require visit function, which call THIS */
  private List<ExprNode> simplifyExprList(List<ExprNode> rawExprs) {
    /* can happen when simplify array */
    if (rawExprs == null) {
      return null;
    }

    List<ExprNode> params = new ArrayList<>();
    for (ExprNode param : rawExprs) {
      params.add(visit(param).asExprNode());
    }
    return params;
  }

  private void mergeMap(Map<Symbol, ExprNode> otherMap) {
    Map<Symbol, ExprNode> result = new HashMap<>();
    for (Map.Entry<Symbol, ExprNode> entry : identValMap.entrySet()) {
      ExprNode expr2 = otherMap.get(entry.getKey());
      if (expr2 != null
              && expr2.isImmediate()
              && entry.getValue().isImmediate()
              && entry.getValue().getCastedVal() == expr2.getCastedVal()) {
        result.put(entry.getKey(), entry.getValue());
      }
    }
    identValMap = result;
  }

  /* get the list of ident map from mapList, return the result map */
  private void mergeIdMap(List<Map<Symbol, ExprNode>> list) {
    for (Map<Symbol, ExprNode> mapCase : list) {
      mergeMap(mapCase);
    }
    list.clear();
  }

  @Override
  public Node visitStructElemNode(StructElemNode node) {
    // todo: support constant propagation on struct, same difficulty as array, pair
    return node;
  }

  @Override
  public Node visitStructNode(StructNode node) {
    // todo: support constant propagation on struct, same difficulty as array, pair
    return node;
  }

  @Override
  public Node visitStructDeclareNode(StructDeclareNode node) {
    // todo: support constant propagation on struct, same difficulty as array, pair
    return node;
  }

  @Override
  public Node visitForNode(ForNode node) {
    /* init statement count as part of map of block before loop body */
    StatNode initStat = visit(node.getInit()).asStatNode();
    /* 1 visit body and increment */
    /*   (same as while) record the mapList, which might contain maps from parent WHILE
     *      current identMap is one map of parent block of while block */
    List<Map<Symbol, ExprNode>>
            oldLoopStartMapList = loopStartMapList,
            oldBreakMapList = breakMapList;
    loopStartMapList = new ArrayList<>();
    breakMapList = new ArrayList<>();
    loopStartMapList.add(new HashMap<>(identValMap));

    /* 2 visit body once, get those idents that won't change after one visit
     *    ignore body generated
     *   loop start map is merged, result is map parent of loop body and block after loop
     *   break is then merged, result is map before block after loop
     *   merged start/end map are used to record map, prevent overwrite in second visit */
    visit(node.getBody());
    visit(node.getIncrement());
    mergeIdMap(loopStartMapList);
    Map<Symbol, ExprNode> mergedStartMap = new HashMap<>(identValMap);
    mergeIdMap(breakMapList);
    Map<Symbol, ExprNode> mergedEndMap = new HashMap<>(identValMap);
    identValMap = mergedStartMap;

    /* 3 visit again using map after getting the intersection */
    ExprNode cond = visit(node.getCond()).asExprNode();
    StatNode body = visit(node.getBody()).asStatNode();
    StatNode inc = visit(node.getIncrement()).asStatNode();
    identValMap = mergedEndMap;

    /* 4 restore mapList */
    loopStartMapList = oldLoopStartMapList;
    breakMapList = oldBreakMapList;

    ForNode forNode = new ForNode(initStat, cond, inc, body);
    forNode.setScope(node.getScope());
    return forNode;
  }

  @Override
  public Node visitJumpNode(JumpNode node) {
    switch (node.getJumpType()) {
      case BREAK:
        breakMapList.add(new HashMap<>(identValMap));
        break;
      case CONTINUE:
        loopStartMapList.add(new HashMap<>(identValMap));
        break;
      default:
        throw new IllegalArgumentException("unsupported jump node type " + node.getJumpType());
    }
    return node;
  }

  @Override
  public Node visitSwitchNode(SwitchNode node) {
    /* constant evaluate switch expr */
    ExprNode switchExpr = visit(node.getExpr()).asExprNode();

    /* 1 create identmaplist for switch only use
    *    record root identMap */
    List<Map<Symbol, ExprNode>> switchMapList = new ArrayList<>();
    Map<Symbol, ExprNode> rootIdentMap = identValMap;

    List<SwitchNode.CaseStat> simplifiedCaseStats = new ArrayList<>();
    /* 2 for each case expr, constant propagate */
    for (SwitchNode.CaseStat caseStat : node.getCases()) {
      /* 2.1 propagate using current identMap */
      identValMap = new HashMap<>(rootIdentMap);
      ExprNode evaledCaseExpr = visit(caseStat.getExpr()).asExprNode();
      StatNode propedCaseStat = visit(caseStat.getBody()).asStatNode();

      /* 2.2 add result identMap to list to be merged */
      switchMapList.add(new HashMap<>(identValMap));

      /* 2.3 record simplified expr and stat */
      simplifiedCaseStats.add(new SwitchNode.CaseStat(evaledCaseExpr, propedCaseStat));
    }

    /* 3 same operation for default case */
    identValMap = new HashMap<>(rootIdentMap);
    StatNode propedDefaultStat = visit(node.getDefault()).asStatNode();
    switchMapList.add(new HashMap<>(identValMap));

    mergeIdMap(switchMapList);

    StatNode result = new SwitchNode(switchExpr, simplifiedCaseStats, propedDefaultStat);
    result.setScope(node.getScope());
    return result;
  }
}
