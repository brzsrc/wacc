package backend.arm;

import backend.arm.segment.*;
import backend.InstructionPrinter;
import backend.arm.instructions.ARMInstruction;
import backend.common.Directive;
import java.util.ArrayList;
import java.util.List;

public class ARMInstructionPrinter extends InstructionPrinter {

  private final List<Directive> directives;
  private final OptimizationLevel optimizationLevel;
  public ARMInstructionPrinter(DataSegment data, TextSegment text, CodeSegment code,
      OptimizationLevel optimizationLevel) {
    this.directives = List.of(data, text, code);
    this.optimizationLevel = optimizationLevel;
  }

  @Override
  public String translate() {
    StringBuilder program = new StringBuilder();
    List<String> list = new ArrayList<>();
    for (Directive l : directives) {
      list.addAll(l.toStringList());
    }

    list.forEach(i -> program.append(i).append("\n"));
    return program.toString();
  }

  /* not used, might be used in optimisation */
  public List<ARMInstruction> constantEvaluation() {
    return null;
  }

  public List<ARMInstruction> constantPropagation() {
    return null;
  }

  public enum OptimizationLevel {
    NONE, CONSTANT_EVAL, CONSTANT_PROPAGATION, CONTROL_FLOW_ANALYSIS, DEAD_CODE_ELIM, PEEPHOLE
  }
}
