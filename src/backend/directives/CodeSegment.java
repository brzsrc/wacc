package backend.directives;

import backend.instructions.Instruction;
import java.util.ArrayList;
import java.util.List;

public class CodeSegment implements Directive {

  private final List<Instruction> instructionList;

  public CodeSegment(List<Instruction> list) {
    instructionList = list;
  }

  public List<Instruction> getInstructions() {
    return instructionList;
  }

  @Override
  public List<String> toStringList() {
    List<String> list = new ArrayList<>();
    list.add("\t.global main");
    for (Instruction i : instructionList) {
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
