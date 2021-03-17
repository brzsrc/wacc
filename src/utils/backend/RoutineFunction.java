package utils.backend;

import backend.arm.instructions.ARMInstruction;
import backend.arm.instructions.Label;
import java.util.List;
import java.util.Map;
import utils.Utils.RoutineInstruction;

public interface RoutineFunction {

  List<ARMInstruction> routineFunctionAssemble(RoutineInstruction routine,
      LabelGenerator labelGenerator, Map<Label, String> dataSegment);
}
