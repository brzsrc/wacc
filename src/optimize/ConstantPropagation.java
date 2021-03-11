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
import java.util.*;

import static utils.Utils.*;

public class ConstantPropagation implements NodeVisitor<Node> {
  /* map an ident with its value exprNode */
  private Map<Symbol, ExprNode> identValMap;

  public ConstantPropagation() {
    identValMap = new HashMap<>();
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
    System.out.println("looking for " + node.getName() + " with " + node.getSymbol());
    if (identValMap.containsKey(node.getSymbol())) {
      System.out.println("successfully found " + node.getSymbol());
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
    ExprNode simpChild = unopApplyMap.get(node.getOperator()).apply(expr);
    return simpChild == null ?
            new UnopNode(expr, node.getOperator()) :
            simpChild;
  }

  @Override
  public Node visitAssignNode(AssignNode node) {
    ExprNode exprNode = visit(node.getRhs()).asExprNode();
    AssignNode resultNode = new AssignNode(node.getLhs(), exprNode);
    resultNode.setScope(node.getScope());

    /* constant propagation:
    *  add new entry into map */
    if ((node.getLhs() instanceof IdentNode) && exprNode.isImmediate()) {
      identValMap.remove(((IdentNode) node.getLhs()).getSymbol());
      identValMap.put(((IdentNode) node.getLhs()).getSymbol(), exprNode);
    }
    return resultNode;
  }

  @Override
  public Node visitDeclareNode(DeclareNode node) {
    ExprNode exprNode = visit(node.getRhs()).asExprNode();
    DeclareNode resultNode = new DeclareNode(node.getIdentifier(), exprNode);
    resultNode.setScope(node.getScope());

    /* constant propagation:
    *  add new entry in map */
    if (exprNode.isImmediate()) {
      identValMap.put(node.getScope().lookup(node.getIdentifier()), exprNode);
      System.out.println(node.getIdentifier() + " has pointer " + node.getScope().lookup(node.getIdentifier()));
    }
    return resultNode;
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

    /* 3 visit while body */
    identValMap = oldMap;
    StatNode elseBody = visit(node.getElseBody()).asStatNode();

    /* 4 new idMap is intersection of both */
//    identValMap.keySet().retainAll(ifIdMap.keySet());
    mergeIdMap(identValMap, ifIdMap);

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
    ReadNode resultNode = new ReadNode(visit(node.getInputExpr()).asExprNode());
    resultNode.setScope(node.getScope());
    return resultNode;
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

    /* 1 visit cond, record old idMap */
    ExprNode cond = visit(node.getCond()).asExprNode();
    Map<Symbol, ExprNode> oldMap = new HashMap<>(identValMap);

    /* 2 visit body once, get those idents that won't change after one visit
    *    ignore body generated */
    visit(node.getBody()).asStatNode();
//    identValMap.keySet().retainAll(oldMap.keySet());
    mergeIdMap(identValMap, oldMap);
    oldMap = new HashMap<>(identValMap);

    /* 3 visit again using map after getting the intersection */
    StatNode body = visit(node.getBody()).asStatNode();
    /* should set map back, prevent result from map influence the rest code's execution */
    identValMap = oldMap;

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
    showIdMap();
    return new ProgramNode(functions, body);
  }

  /* debug function, show content of idMap */
  private void showIdMap() {
    for (Map.Entry<Symbol, ExprNode> entry : identValMap.entrySet()) {
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

  private void mergeIdMap(Map<Symbol, ExprNode> map1, Map<Symbol, ExprNode> map2) {
    map1.entrySet().retainAll(map2.entrySet());
  }

  @Override
  public Node visitStructElemNode(StructElemNode node) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Node visitStructNode(StructNode node) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Node visitStructDeclareNode(StructDeclareNode node) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Node visitForNode(ForNode node) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Node visitJumpNode(JumpNode node) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Node visitSwitchNode(SwitchNode node) {
    // TODO Auto-generated method stub
    return null;
  }
}
