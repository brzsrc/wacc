package backend;

import backend.instructions.Instruction;
import backend.instructions.Mov;
import backend.instructions.Operand.Immediate;
import frontend.node.*;
import frontend.node.expr.*;
import frontend.node.stat.*;
import frontend.visitor.NodeVisitor;

import java.util.ArrayList;
import java.util.List;

import static backend.instructions.Operand.Immediate.BitNum;
import static backend.instructions.Operand.SudoRegister.getCurrAvailableReg;
import static backend.instructions.Operand.SudoRegister.peakLastReg;

public class NodeTranslator implements NodeVisitor<List<Instruction>> {

  public static final int TRUE = 1;
  public static final int FALSE = 0;

  @Override
  public List<Instruction> visitArrayElemNode(ArrayElemNode node) {
    // todo: after discussing how to implement heap/stack allocation
    return null;
  }

  @Override
  public List<Instruction> visitArrayNode(ArrayNode node) {
    // todo: after discussing how to implement heap/stack allocation
    
    return null;
  }

  @Override
  public List<Instruction> visitBinopNode(BinopNode node) {
    List<Instruction> expr1Instr = visit(node.getExpr1());
    long resultReg1 = peakLastReg();
    List<Instruction> expr2Instr = visit(node.getExpr2());
    long resultReg2 = peakLastReg();

    /* generate corrisponding command for each binop command */
    // todo: how did mark say about not using switch? use map to map a binop.enum to a command?

    return null;
  }

  @Override
  public List<Instruction> visitBoolNode(BoolNode node) {
    List<Instruction> result = new ArrayList<>();
    result.add(new Mov(getCurrAvailableReg(), new Immediate(node.getVal() ? TRUE : FALSE, BitNum.SHIFT32)));
    return result;
  }

  @Override
  public List<Instruction> visitCharNode(CharNode node) {
    List<Instruction> result = new ArrayList<>();
    result.add(new Mov(getCurrAvailableReg(), new Immediate(node.getAsciiValue(), BitNum.SHIFT32)));
    return result;
  }

  @Override
  public List<Instruction> visitFunctionCallNode(FunctionCallNode node) {
    return null;
  }

  @Override
  public List<Instruction> visitIdentNode(IdentNode node) {
    return null;
  }

  @Override
  public List<Instruction> visitIntegerNode(IntegerNode node) {
    // todo: same as visitCharNode
    return null;
  }

  @Override
  public List<Instruction> visitPairElemNode(PairElemNode node) {
    return null;
  }

  @Override
  public List<Instruction> visitPairNode(PairNode node) {
    return null;
  }

  @Override
  public List<Instruction> visitStringNode(StringNode node) {
    return null;
  }

  @Override
  public List<Instruction> visitUnopNode(UnopNode node) {
    // todo: same as binop
    return null;
  }

  @Override
  public List<Instruction> visitAssignNode(AssignNode node) {
    return null;
  }

  @Override
  public List<Instruction> visitDeclareNode(DeclareNode node) {
    return null;
  }

  @Override
  public List<Instruction> visitExitNode(ExitNode node) {
    return null;
  }

  @Override
  public List<Instruction> visitFreeNode(FreeNode node) {
    return null;
  }

  @Override
  public List<Instruction> visitIfNode(IfNode node) {
    return null;
  }

  @Override
  public List<Instruction> visitPrintlnNode(PrintlnNode node) {
    return null;
  }

  @Override
  public List<Instruction> visitPrintNode(PrintNode node) {
    return null;
  }

  @Override
  public List<Instruction> visitReadNode(ReadNode node) {
    return null;
  }

  @Override
  public List<Instruction> visitReturnNode(ReturnNode node) {
    return null;
  }

  @Override
  public List<Instruction> visitScopeNode(ScopeNode node) {
    return null;
  }

  @Override
  public List<Instruction> visitSkipNode(SkipNode node) {
    return null;
  }

  @Override
  public List<Instruction> visitWhileNode(WhileNode node) {
    return null;
  }

  @Override
  public List<Instruction> visitFuncNode(FuncNode node) {
    return null;
  }

  @Override
  public List<Instruction> visitProgramNode(ProgramNode node) {
    return null;
  }
}
