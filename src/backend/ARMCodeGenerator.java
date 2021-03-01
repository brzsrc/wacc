package backend;

import java.util.ArrayList;
import java.util.List;

import backend.directives.CodeSegment;
import backend.directives.DataSegment;
import backend.directives.Directive;
import backend.directives.TextSegment;
import backend.instructions.Instruction;

public class ARMCodeGenerator {

    public enum OptimizationLevel {
        NONE, CONSTANT_EVAL, CONSTANT_PROPAGATION, CONTROL_FLOW_ANALYSIS, DEAD_CODE_ELIM, PEEPHOLE
    }

    private List<Directive> directives;
    private OptimizationLevel optimizationLevel;

    public ARMCodeGenerator(DataSegment data, TextSegment text, CodeSegment code, OptimizationLevel optimizationLevel) {
        this.directives = List.of(data, text, code);
        this.optimizationLevel = optimizationLevel;
    }

    public String translate() {
        StringBuilder program = new StringBuilder();
        List<String> list = new ArrayList<>();
        for (Directive l : directives) {
            list.addAll(l.toStringList());
        }

        list.forEach(i -> program.append(i + "\n"));
        return program.toString();
    }

    public List<Instruction> constantEvaluation() {
        return null;
    }

    public List<Instruction> constantPropagation() {
        return null;
    }
}
