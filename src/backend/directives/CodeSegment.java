package backend.directives;

import java.util.ArrayList;
import java.util.List;

import backend.instructions.Instruction;

public class CodeSegment implements Directive {

    private List<Instruction> instructionList;

    public CodeSegment() {
        instructionList = new ArrayList<>();
    }

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
            list.add("\t".repeat(i.getIndentationLevel()) + i.assemble());
        }
        return list;
    }

    @Override
    public int getIndentationLevel() {
        return 1;
    }

}
