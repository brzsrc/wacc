package backend.intel;

import java.util.ArrayList;
import java.util.List;

import backend.InstructionPrinter;
import backend.common.Directive;
import backend.intel.section.CodeSection;
import backend.intel.section.DataSection;
import backend.intel.section.IntelAsmHeader;

public class IntelInstructionPrinter extends InstructionPrinter {
    List<Directive> directives;

    public IntelInstructionPrinter(IntelAsmHeader header, DataSection data, CodeSection code) {
        this.directives = List.of(header, data, code);
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
}
