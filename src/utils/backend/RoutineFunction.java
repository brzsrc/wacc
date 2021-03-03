package utils.backend;

import backend.instructions.Instruction;
import backend.instructions.Label;
import utils.Utils.RoutineInstruction;

import java.util.List;
import java.util.Map;

public interface RoutineFunction {
  List<Instruction> routineFunctionAssemble(RoutineInstruction routine, LabelGenerator labelGenerator, Map<Label, String> dataSegment);
}
