package backend;

import backend.common.LabelInstruction;
import java.util.ArrayList;
import java.util.List;
import utils.NodeVisitor;
import utils.frontend.symbolTable.SymbolTable;

public abstract class InstructionGenerator<T extends Instruction> implements NodeVisitor<Void> {
  /* the code section of the assembly code */
  protected final List<T> instructions;
  /* record the current symbolTable used during instruction generation */
  protected SymbolTable currSymbolTable;
  /* record the symbolTable of the innermost loop */
  protected SymbolTable currForLoopSymbolTable;
  /* mark if we are visiting a lhs or rhs of an expr */
  protected boolean isLhs;
  /* offset used when pushing variable in stack in visitFunctionCall
   * USED FOR evaluating function parameters, not for changing parameters' offset in function body*/
  protected int stackOffset;
  /* used by visitFunc and visitReturn, set how many byte this function used on stack
     accumulated, on enter a new scope, decrease on exit
     no need to distinguish function and non function scope, as non function scope does not call return */
  protected int funcStackSize;

  /* recording the jump-to label for branching statement, i.e. break, continue */
  protected LabelInstruction currBreakJumpToLabel;
  protected LabelInstruction currContinueJumpToLabel;

  public InstructionGenerator() {
    instructions = new ArrayList<>();
    currSymbolTable = null;
    stackOffset = 0;
    isLhs = false;
    currBreakJumpToLabel = null;
    currContinueJumpToLabel = null;
    currForLoopSymbolTable = null;
  }
}
