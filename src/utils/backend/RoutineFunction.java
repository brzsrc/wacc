package utils.backend;

import backend.instructions.Instruction;
import backend.instructions.Label;
import java.util.List;
import java.util.Map;
import utils.Utils.RoutineInstruction;

public interface RoutineFunction {

  List<Instruction> routineFunctionAssemble(RoutineInstruction routine,
      LabelGenerator labelGenerator, Map<Label, String> dataSegment);
}
