package utils.backend;

import backend.instructions.B;
import backend.instructions.BL;
import backend.instructions.Cmp;
import backend.instructions.Instruction;
import backend.instructions.LDR;
import backend.instructions.Label;
import backend.instructions.Mov;
import backend.instructions.LDR.LdrMode;
import backend.instructions.addressing.LabelAddressing;
import backend.instructions.addressing.RegAddressing;
import backend.instructions.addressing.addressingMode2.AddressingMode2;
import backend.instructions.addressing.addressingMode2.AddressingMode2.AddrMode2;
import backend.instructions.arithmeticLogic.Add;
import backend.instructions.memory.Pop;
import backend.instructions.memory.Push;
import backend.instructions.operand.Immediate;
import backend.instructions.operand.Immediate.BitNum;
import backend.instructions.operand.Operand2;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import utils.Utils;
import utils.Utils.RoutineInstruction;
import utils.Utils.SystemCallInstruction;

public class ARMInstructionRoutines {

  /* static ARM register references */
  public static final ARMConcreteRegister r0 = new ARMConcreteRegister(ARMRegisterLabel.R0);
  public static final ARMConcreteRegister r1 = new ARMConcreteRegister(ARMRegisterLabel.R1);
  public static final ARMConcreteRegister r2 = new ARMConcreteRegister(ARMRegisterLabel.R2);
  public static final ARMConcreteRegister LR = new ARMConcreteRegister(ARMRegisterLabel.LR);
  public static final ARMConcreteRegister PC = new ARMConcreteRegister(ARMRegisterLabel.PC);
  public static final ARMConcreteRegister SP = new ARMConcreteRegister(ARMRegisterLabel.SP);

  public static List<Instruction> addRead(RoutineInstruction routine, LabelGenerator labelGenerator, Map<Label, String> dataSegment) {
    List<Instruction> instructions = new ArrayList<>();
    /* add the helper function label */
    Label readLabel = new Label(routine.toString());

    String ascii = routine == RoutineInstruction.READ_INT ? "\"%d\\0\"" : "\" %c\\0\"";
    Label msgLabel = labelGenerator.getLabel();
    dataSegment.put(readLabel, ascii);

    instructions = List.of(readLabel, new Push(Collections.singletonList(LR)),
        new Mov(r1, new Operand2(r0)), new LDR(r0, new LabelAddressing(msgLabel)),
        new Add(r0, r0, new Operand2(4)), new BL(SystemCallInstruction.SCANF.toString()),
        new Pop(Collections.singletonList(PC)));

    return instructions;
  }

  public static List<Instruction> addPrint(RoutineInstruction routine, LabelGenerator labelGenerator, Map<Label, String> dataSegment) {
    Label msgLabel = labelGenerator.getLabel();
    switch (routine) {
      case PRINT_CHAR:
        return List.of(new BL(SystemCallInstruction.PUTCHAR.toString()));
      case PRINT_BOOL:
        /* add the printing true into .data section */
        dataSegment.put(msgLabel, Utils.routineMsgMapping.get(RoutineInstruction.PRINT_BOOL).get(0));
        Label sndLabel = labelGenerator.getLabel();
        dataSegment.put(sndLabel, Utils.routineMsgMapping.get(RoutineInstruction.PRINT_BOOL).get(1));
        return addPrintBool(msgLabel, sndLabel);
      case PRINT_STRING:
        dataSegment.put(msgLabel, "\"%.*s\\0\"");
        return addPrintMultiple(msgLabel);
      case PRINT_INT:
      case PRINT_REFERENCE:
      default:
        dataSegment.put(msgLabel, Utils.routineMsgMapping.get(routine).get(0));
        return addPrintSingle(routine, msgLabel);
    }
  }

  public static List<Instruction> addPrintln(LabelGenerator labelGenerator, Map<Label, String> dataSegment) {
    List<Instruction> instructions = new ArrayList<>();
    RoutineInstruction routine = RoutineInstruction.PRINT_LN;

    Label printlnMsgLabel = labelGenerator.getLabel();
    dataSegment.put(printlnMsgLabel, Utils.routineMsgMapping.get(RoutineInstruction.PRINT_LN).get(0));

    /* add the helper function label */
    Label label = new Label(routine.toString());
    instructions = List.of(
        label, new Push(Collections.singletonList(LR)), new LDR(r0, new LabelAddressing(printlnMsgLabel)),
        /* skip the first 4 byte of the msg which is the length of it */
        new Add(r0, r0, new Operand2(4)), new BL(SystemCallInstruction.PUTS.toString()),
        /* refresh the r0 and buffer */
        new Mov(r0, new Operand2(0)), new BL(SystemCallInstruction.FFLUSH.toString()),
        new Pop(Collections.singletonList(PC))
    );

    return instructions;
  }

  public static List<Instruction> addFree(RoutineInstruction routine, LabelGenerator labelGenerator, Map<Label, String> dataSegment) {
    List<Instruction> instructions = new ArrayList<>();

    Label msgLabel = labelGenerator.getLabel();
    dataSegment.put(msgLabel, Utils.routineMsgMapping.get(routine).get(0));

    Label printlnLabel = labelGenerator.getLabel();
    dataSegment.put(printlnLabel, Utils.routineMsgMapping.get(routine).get(1));

    /* add the helper function label */
    Label freeLabel = new Label(routine.toString());

    instructions.add(freeLabel);
    instructions.add(new Push(Collections.singletonList(LR)));
    instructions.add(new Cmp(r0, new Operand2(0)));
    instructions.add(new LDR(r0, new LabelAddressing(msgLabel), LdrMode.LDREQ));
    instructions.add(new B(Cond.EQ, RoutineInstruction.THROW_RUNTIME_ERROR.toString()));

    if(routine.equals(RoutineInstruction.FREE_PAIR)) {
      instructions.add(new Push(Collections.singletonList(r0)));
      instructions.add(new LDR(r0, new RegAddressing(r0)));
      instructions.add(new BL(SystemCallInstruction.FREE.toString()));
      instructions.add(new LDR(r0, new RegAddressing(SP)));
      instructions.add(new LDR(r0, new AddressingMode2(AddrMode2.OFFSET, r0, new Immediate(4, BitNum.CONST8))));
      instructions.add(new BL(SystemCallInstruction.FREE.toString()));
      instructions.add(new Pop(Collections.singletonList(r0)));
    }
    instructions.add(new BL(SystemCallInstruction.FREE.toString()));
    instructions.add(new Pop(Collections.singletonList(PC)));
    instructions.addAll(addThrowRuntimeError(labelGenerator, dataSegment));

    return instructions;
  }

  public static List<Instruction> addCheckNullPointer(LabelGenerator labelGenerator, Map<Label, String> dataSegment) {
    List<Instruction> instructions = new ArrayList<>();
    RoutineInstruction routineInstruction = RoutineInstruction.CHECK_NULL_POINTER;

    Label msgLabel = labelGenerator.getLabel();
    dataSegment.put(msgLabel, Utils.routineMsgMapping.get(RoutineInstruction.CHECK_NULL_POINTER).get(0));
    Label printlnLabel = labelGenerator.getLabel();
    dataSegment.put(printlnLabel, Utils.routineMsgMapping.get(RoutineInstruction.CHECK_NULL_POINTER).get(1));

    /* add the helper function label */
    Label label = new Label(routineInstruction.toString());
    instructions.add(label);
    instructions.add(new Push(Collections.singletonList(LR)));
    instructions.add(new Cmp(r0, new Operand2(new Immediate(0, BitNum.CONST8))));
    instructions.add(new LDR(r0, new LabelAddressing(msgLabel), LdrMode.LDREQ));
    instructions.add(new BL(Cond.EQ, RoutineInstruction.THROW_RUNTIME_ERROR.toString()));
    instructions.add(new Pop(Collections.singletonList(PC)));
    instructions.addAll(addThrowRuntimeError(labelGenerator, dataSegment));

    return instructions;
  }

  public static List<Instruction> addCheckDivByZero(LabelGenerator labelGenerator, Map<Label, String> dataSegment) {
    List<Instruction> instructions = new ArrayList<>();
    RoutineInstruction routine = RoutineInstruction.CHECK_DIVIDE_BY_ZERO;

    Label msgLabel = labelGenerator.getLabel();
    dataSegment.put(msgLabel, Utils.routineMsgMapping.get(RoutineInstruction.CHECK_DIVIDE_BY_ZERO).get(0));
    Label printMultipleLabel = labelGenerator.getLabel();
    dataSegment.put(printMultipleLabel, Utils.routineMsgMapping.get(RoutineInstruction.CHECK_DIVIDE_BY_ZERO).get(1));

    /* add the helper function label */
    Label label = new Label(routine.toString());
    instructions.add(label);
    instructions.add(new Push(Collections.singletonList(LR)));
    instructions.add(new Cmp(r1, new Operand2(0)));
    instructions.add(new LDR(r0, new LabelAddressing(msgLabel), LdrMode.LDREQ));
    instructions.add(new BL(Cond.EQ, RoutineInstruction.THROW_RUNTIME_ERROR.toString()));
    instructions.add(new Pop(Collections.singletonList(PC)));
    instructions.addAll(addThrowRuntimeError(labelGenerator, dataSegment));

    return instructions;
  }

  public static List<Instruction> addCheckArrayBound(LabelGenerator labelGenerator, Map<Label, String> dataSegment) {
    List<Instruction> instructions = new ArrayList<>();
    RoutineInstruction routine = RoutineInstruction.CHECK_ARRAY_BOUND;

    Label negativeIndexLabel = labelGenerator.getLabel();
    dataSegment.put(negativeIndexLabel, Utils.routineMsgMapping.get(RoutineInstruction.CHECK_ARRAY_BOUND).get(0));
    Label indexOutOfBoundLabel = labelGenerator.getLabel();
    dataSegment.put(indexOutOfBoundLabel, Utils.routineMsgMapping.get(RoutineInstruction.CHECK_ARRAY_BOUND).get(1));

    instructions.add(new Label(RoutineInstruction.CHECK_ARRAY_BOUND.toString()));
    instructions.add(new Push(Collections.singletonList(LR)));
    instructions.add(new Cmp(r0, new Operand2(new Immediate(0, BitNum.CONST8))));
    instructions.add(new LDR(r0, new LabelAddressing(negativeIndexLabel), LdrMode.LDRLT));
    instructions.add(new BL(Cond.LT, RoutineInstruction.THROW_RUNTIME_ERROR.toString()));
    instructions.add(new LDR(r1, new AddressingMode2(AddrMode2.OFFSET, r1)));
    instructions.add(new Cmp(r0, new Operand2(r1)));
    instructions.add(new LDR(r0, new LabelAddressing(indexOutOfBoundLabel), LdrMode.LDRCS));
    instructions.add(new BL(Cond.CS, RoutineInstruction.THROW_RUNTIME_ERROR.toString()));
    instructions.add(new Pop(Collections.singletonList(PC)));

    return instructions;
  }

  public static List<Instruction> addThrowOverflowError(LabelGenerator labelGenerator, Map<Label, String> dataSegment) {
    List<Instruction> instructions = new ArrayList<>();

    Label overflowMsgLabel = labelGenerator.getLabel();
    dataSegment.put(overflowMsgLabel, Utils.routineMsgMapping.get(RoutineInstruction.THROW_OVERFLOW_ERROR).get(0));
    Label printMultipleLabel = labelGenerator.getLabel();
    dataSegment.put(printMultipleLabel, Utils.routineMsgMapping.get(RoutineInstruction.THROW_OVERFLOW_ERROR).get(1));

    instructions.add(new Label("p_throw_overflow_error"));
    instructions.add(new LDR(r0, new LabelAddressing(overflowMsgLabel), LdrMode.LDR));
    instructions.add(new BL("p_throw_runtime_error"));
    instructions.addAll(addThrowRuntimeError(labelGenerator, dataSegment));

    return instructions;
  }

  public static List<Instruction> addThrowRuntimeError(LabelGenerator labelGenerator, Map<Label, String> dataSegment) {
    List<Instruction> instructions = new ArrayList<>();

    Label printMultipleLabel = labelGenerator.getLabel();
    dataSegment.put(printMultipleLabel, Utils.routineMsgMapping.get(RoutineInstruction.CHECK_DIVIDE_BY_ZERO).get(1));

    /* add the helper function label */
    Label label = new Label(Utils.RoutineInstruction.THROW_RUNTIME_ERROR.toString());
    instructions.add(label);
    instructions.add(new BL(RoutineInstruction.PRINT_STRING.toString()));
    instructions.add(new Mov(r0, new Operand2(-1)));
    instructions.add(new BL(SystemCallInstruction.EXIT.toString()));
    instructions.addAll(addPrintMultiple(printMultipleLabel));

    return instructions;
  }

  /* print string (char array included) */
  private static List<Instruction> addPrintMultiple(Label msgLabel) {
      List<Instruction> instructions = new ArrayList<>();
    RoutineInstruction routineInstruction = RoutineInstruction.PRINT_STRING;

    /* add the helper function label */
    Label label = new Label(routineInstruction.toString());
    instructions.add(label);
    instructions.add(new Push(Collections.singletonList(LR)));
    /* put the string length into r1 as snd arg */
    instructions.add(new LDR(r1, new RegAddressing(r0)));
    /* skip the fst 4 bytes which is the length of the string */
    instructions.add(new Add(r2, r0, new Operand2(new Immediate(4, BitNum.CONST8))));
    instructions.add(new LDR(r0, new LabelAddressing(msgLabel)));

    instructions.addAll(addCommonPrint());

    return instructions;
  }

  /* print int, print char or print reference */
  private static List<Instruction> addPrintSingle(RoutineInstruction routine, Label msgLabel) {
    List<Instruction> instructions = new ArrayList<>();

    /* add the helper function label */
    Label label = new Label(routine.toString());
    instructions.add(label);
    instructions.add(new Push(Collections.singletonList(LR)));
    /* put the content in r0 int o r1 as the snd arg of printf */
    instructions.add(new Mov(r1, new Operand2(r0)));
    /* fst arg of printf is the format */
    instructions.add(new LDR(r0, new LabelAddressing(msgLabel)));

    instructions.addAll(addCommonPrint());

    return instructions;
  }

  /* print bool */
  private static List<Instruction> addPrintBool(Label trueLabel, Label falseLabel) {
    List<Instruction> instructions = new ArrayList<>();

    /* add the helper function label */
    Label label = new Label(RoutineInstruction.PRINT_BOOL.toString());
    instructions.add(label);
    instructions.add(new Push(Collections.singletonList(LR)));
    /* cmp the content in r0 with 0 */
    instructions.add(new Cmp(r0, new Operand2(0)));
    /* if not equal to 0 LDR true */
    instructions.add(new LDR(r0, new LabelAddressing(trueLabel), LdrMode.LDRNE));
    /* otherwise equal to 0 LDR false */
    instructions.add(new LDR(r0, new LabelAddressing(falseLabel), LdrMode.LDREQ));

    instructions.addAll(addCommonPrint());

    return instructions;
  }

  private static List<Instruction> addCommonPrint() {
    return List.of(
        /* skip the first 4 byte of the msg which is the length of it */
        new Add(r0, r0, new Operand2(4)),
        new BL(SystemCallInstruction.PRINTF.toString()),
        /* refresh the r0 and buffer */
        new Mov(r0, new Operand2(0)),
        new BL(SystemCallInstruction.FFLUSH.toString()),
        new Pop(Collections.singletonList(PC))
    );
  }
}
