package backend.intel.section;

import backend.common.Directive;
import backend.intel.instructions.IntelInstruction;

import java.util.List;
import java.util.stream.Collectors;

public class CodeSection implements Directive {

  private final List<IntelInstruction> instructions;

  public CodeSection(List<IntelInstruction> instructions) {
    this.instructions = instructions;
  }

  @Override
  public List<String> toStringList() {
    return instructions.stream().map(i -> "\t".repeat(i.getIndentationLevel()) + i.assemble()).collect(Collectors.toList());
  }

  @Override
  public int getIndentationLevel() {
    return 0;
  }
}
