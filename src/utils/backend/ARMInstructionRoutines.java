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
import backend.instructions.arithmeticLogic.ArithmeticLogicAssemble;
import backend.instructions.memory.Pop;
import backend.instructions.memory.Push;
import backend.instructions.operand.Immediate;
import backend.instructions.operand.Immediate.BitNum;
import backend.instructions.operand.Operand2;

import java.util.*;

import frontend.node.expr.BinopNode;
import utils.Utils;
import utils.Utils.RoutineInstruction;
import utils.Utils.SystemCallInstruction;
import static utils.backend.ARMConcreteRegister.*;

import static utils.Utils.RoutineInstruction.*;

public class ARMInstructionRoutines {

  public static RoutineFunction addRead = (routine, labelGenerator, dataSegment) -> {
    /* add the helper function label */
    Label readLabel = new Label(routine.toString());

    String ascii = routine == READ_INT ? "\"%d\\0\"" : "\" %c\\0\"";
    Label msgLabel = labelGenerator.getLabel();
    dataSegment.put(readLabel, ascii);

    List<Instruction> instructions = List.of(readLabel, new Push(Collections.singletonList(LR)),
        new Mov(r1, new Operand2(r0)), new LDR(r0, new LabelAddressing(msgLabel)),
        new Add(r0, r0, new Operand2(4)), new BL(SystemCallInstruction.SCANF.toString()),
        new Pop(Collections.singletonList(PC)));

    return instructions;
  };

  public static RoutineFunction addPrint = (routine, labelGenerator, dataSegment) -> {
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
  };

  public static RoutineFunction addPrintln = (routine, labelGenerator, dataSegment) -> {
    /* overwrite, routine has to be PRINTLN */
    routine = RoutineInstruction.PRINT_LN;

    Label printlnMsgLabel = labelGenerator.getLabel();
    dataSegment.put(printlnMsgLabel, Utils.routineMsgMapping.get(RoutineInstruction.PRINT_LN).get(0));

    /* add the helper function label */
    Label label = new Label(routine.toString());
    List<Instruction> instructions = List.of(
        label, new Push(Collections.singletonList(LR)), new LDR(r0, new LabelAddressing(printlnMsgLabel)),
        /* skip the first 4 byte of the msg which is the length of it */
        new Add(r0, r0, new Operand2(4)), 
        new BL(SystemCallInstruction.PUTS.toString()),
        /* refresh the r0 and buffer */
        new Mov(r0, new Operand2(0)), 
        new BL(SystemCallInstruction.FFLUSH.toString()),
        new Pop(Collections.singletonList(PC))
    );

    return instructions;
  };

  public static RoutineFunction addThrowRuntimeError = (routine, labelGenerator, dataSegment) ->  {
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
  };

  public static RoutineFunction addFree = (routine, labelGenerator, dataSegment)->{
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
    instructions.addAll(addThrowRuntimeError.routineFunctionAssemble(RoutineInstruction.THROW_RUNTIME_ERROR, labelGenerator, dataSegment));

    return instructions;
  };

  public static RoutineFunction addCheckNullPointer = (routine, labelGenerator, dataSegment) ->  {
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
    instructions.addAll(addThrowRuntimeError.routineFunctionAssemble(RoutineInstruction.THROW_RUNTIME_ERROR, labelGenerator, dataSegment));

    return instructions;
  };

  public static RoutineFunction addCheckDivByZero = (routine, labelGenerator, dataSegment) ->  {
    List<Instruction> instructions = new ArrayList<>();

    /* overwrite, routine has to be check divide by zero */
    routine = RoutineInstruction.CHECK_DIVIDE_BY_ZERO;

    Label msgLabel = labelGenerator.getLabel();
    dataSegment.put(msgLabel, Utils.routineMsgMapping.get(routine).get(0));
    Label printMultipleLabel = labelGenerator.getLabel();
    dataSegment.put(printMultipleLabel, Utils.routineMsgMapping.get(routine).get(1));

    /* add the helper function label */
    Label label = new Label(routine.toString());
    instructions.add(label);
    instructions.add(new Push(Collections.singletonList(LR)));
    instructions.add(new Cmp(r1, new Operand2(0)));
    instructions.add(new LDR(r0, new LabelAddressing(msgLabel), LdrMode.LDREQ));
    instructions.add(new BL(Cond.EQ, RoutineInstruction.THROW_RUNTIME_ERROR.toString()));
    instructions.add(new Pop(Collections.singletonList(PC)));
    instructions.addAll(addThrowRuntimeError.routineFunctionAssemble(RoutineInstruction.THROW_RUNTIME_ERROR, labelGenerator, dataSegment));

    return instructions;
  };

  public static RoutineFunction addCheckArrayBound = (routine, labelGenerator, dataSegment) ->  {

    /* overwrite, routine has to be check array bound */
    routine = RoutineInstruction.CHECK_ARRAY_BOUND;
    List<Instruction> instructions = new ArrayList<>();

    Label negativeIndexLabel = labelGenerator.getLabel();
    dataSegment.put(negativeIndexLabel, Utils.routineMsgMapping.get(routine).get(0));
    Label indexOutOfBoundLabel = labelGenerator.getLabel();
    dataSegment.put(indexOutOfBoundLabel, Utils.routineMsgMapping.get(routine).get(1));

    instructions.add(new Label(routine.toString()));
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
  };

  public static RoutineFunction addThrowOverflowError = (routine, labelGenerator, dataSegment) ->  {
    List<Instruction> instructions = new ArrayList<>();

    Label overflowMsgLabel = labelGenerator.getLabel();
    dataSegment.put(overflowMsgLabel, Utils.routineMsgMapping.get(RoutineInstruction.THROW_OVERFLOW_ERROR).get(0));
    Label printMultipleLabel = labelGenerator.getLabel();
    dataSegment.put(printMultipleLabel, Utils.routineMsgMapping.get(RoutineInstruction.THROW_OVERFLOW_ERROR).get(1));

    instructions.add(new Label("p_throw_overflow_error"));
    instructions.add(new LDR(r0, new LabelAddressing(overflowMsgLabel), LdrMode.LDR));
    instructions.add(new BL("p_throw_runtime_error"));
    instructions.addAll(addThrowRuntimeError.routineFunctionAssemble(RoutineInstruction.THROW_RUNTIME_ERROR, labelGenerator, dataSegment));

    return instructions;
  };

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
  private static List<Instruction> addPrintSingle (RoutineInstruction routine, Label msgLabel) {
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

  public static final Map<RoutineInstruction, RoutineFunction> routineFunctionMap = Map.ofEntries(
          new AbstractMap.SimpleEntry<>(READ_INT, addRead),
          new AbstractMap.SimpleEntry<>(READ_CHAR, addRead),
          new AbstractMap.SimpleEntry<>(PRINT_INT, addPrint),
          new AbstractMap.SimpleEntry<>(PRINT_BOOL, addPrint),
          new AbstractMap.SimpleEntry<>(PRINT_CHAR, addPrint),
          new AbstractMap.SimpleEntry<>(PRINT_STRING, addPrint),
          new AbstractMap.SimpleEntry<>(PRINT_REFERENCE, addPrint),
          new AbstractMap.SimpleEntry<>(PRINT_LN, addPrintln),
          new AbstractMap.SimpleEntry<>(CHECK_DIVIDE_BY_ZERO, addCheckDivByZero),
          new AbstractMap.SimpleEntry<>(THROW_RUNTIME_ERROR, addThrowRuntimeError),
          new AbstractMap.SimpleEntry<>(CHECK_ARRAY_BOUND, addCheckArrayBound),
          new AbstractMap.SimpleEntry<>(FREE_ARRAY, addFree),
          new AbstractMap.SimpleEntry<>(FREE_PAIR, addFree),
          new AbstractMap.SimpleEntry<>(CHECK_NULL_POINTER, addCheckNullPointer),
          new AbstractMap.SimpleEntry<>(THROW_OVERFLOW_ERROR, addThrowOverflowError)
          );

}
