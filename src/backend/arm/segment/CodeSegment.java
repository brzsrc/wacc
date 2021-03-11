package backend.arm.segment;

import backend.arm.instructions.ARMInstruction;
import backend.common.Directive;
import java.util.ArrayList;
import java.util.List;

public class CodeSegment implements Directive {

  private final List<ARMInstruction> instructionList;

  public CodeSegment(List<ARMInstruction> list) {
    instructionList = list;
  }

  public List<ARMInstruction> getInstructions() {
    return instructionList;
  }

  @Override
  public List<String> toStringList() {
    List<String> list = new ArrayList<>();
    list.add("\t.global main");
    for (ARMInstruction i : instructionList) {
      String tabs = "";
      for (int j = 0; j < i.getIndentationLevel(); j++) {
        tabs += "\t";
      }
      list.add(tabs + i.assemble());
    }
    return list;
  }

  @Override
  public int getIndentationLevel() {
    return 1;
  }

}
