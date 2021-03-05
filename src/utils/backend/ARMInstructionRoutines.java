package utils.backend;

import backend.instructions.B;
import backend.instructions.BL;
import backend.instructions.Cmp;
import backend.instructions.Instruction;
import backend.instructions.LDR;
import backend.instructions.Label;
import backend.instructions.Mov;
import backend.instructions.addressing.LabelAddressing;
import backend.instructions.addressing.AddressingMode2;
import backend.instructions.arithmeticLogic.Add;
import backend.instructions.memory.Pop;
import backend.instructions.memory.Push;
import backend.instructions.operand.Operand2;
import utils.Utils;
import utils.Utils.RoutineInstruction;

import java.util.*;

import static backend.instructions.LDR.LdrMode.*;
import static backend.instructions.addressing.AddressingMode2.AddrMode2.OFFSET;
import static utils.Utils.SystemCallInstruction.*;
import static utils.backend.register.ARMConcreteRegister.*;
import static utils.Utils.RoutineInstruction.*;
import static utils.backend.Cond.EQ;

public class ARMInstructionRoutines {

  private static final String PRINT_INT_MSG = "\"%d\\0\"";
  private static final String PRINT_REF_MSG = "\"%p\\0\"";
  private static final String PRINT_CHAR_MSG = "\" %c\\0\"";
  private static final String PRINT_LN_MSG = "\"\\0\"";
  private static final String PRINT_NULL_REF_MSG = "\"NullReferenceError: dereference a null reference\\n\\0\"";
  private static final String PRINT_STRING_MSG = "\"%.*s\\0\"";
  private static final String PRINT_OVERFLOW_MSG = "\"OverflowError: the result is too small/large to store in a 4-byte signed-integer.\\n\\0\"";
  private static final String PRINT_DIV_ZERO_MSG = "\"DivideByZeroError: divide or modulo by zero\\n\\0\"";
  private static final String PRINT_ARRAY_NEG_INDEX_MSG = "\"ArrayIndexOutOfBoundsError: negative index\\n\\0\"";
  private static final String PRINT_ARRAY_INDEX_TOO_LARGE_MSG = "\"ArrayIndexOutOfBoundsError: index too large\\n\\0\"";
  private static final String PRINT_BOOL_TRUE = "\"true\\0\"";
  private static final String PRINT_BOOL_FALSE = "\"false\\0\"";

  /* adding a private constructor to override the default public constructor in order to 
     indicate ARMInstructionRoutines class cannot be instantiated */
  private ARMInstructionRoutines() {
    throw new IllegalStateException("Utility Class cannot be instantiated!");
  }

  public static RoutineFunction addRead = (routine, labelGenerator, dataSegment) -> {
    /* add the helper function label */
    Label readLabel = new Label(routine.toString());

    /* add the format into the data list */
    String asciiMsg = routine == READ_INT ? PRINT_INT_MSG : PRINT_CHAR_MSG;
    Label msgLabel = labelGenerator.getLabel();
    dataSegment.put(msgLabel, asciiMsg);

    return List.of(
      readLabel,
      new Push(Collections.singletonList(LR)),
      /* fst arg of read is the snd arg of scanf (storing address) */
      new Mov(r1, new Operand2(r0)),
      /* fst arg of scanf is the format */
      new LDR(r0, new LabelAddressing(msgLabel)),
      /* skip the first 4 byte of the msg which is the length of it */
      new Add(r0, r0, new Operand2(4)), new BL(SCANF.toString()),
      new Pop(Collections.singletonList(PC)));
  };

  public static RoutineFunction addPrint = (routine, labelGenerator, dataSegment) -> {
    Label msgLabel = labelGenerator.getLabel();
    switch (routine) {
      case PRINT_CHAR:
        return new ArrayList<>();
      case PRINT_BOOL:
        return addPrintBool(dataSegment, labelGenerator);
      case PRINT_STRING:
        return addPrintMultiple(dataSegment, labelGenerator);
      case PRINT_INT:
        return addPrintSingle(PRINT_INT, dataSegment, labelGenerator);
      case PRINT_REFERENCE:
      default:
        dataSegment.put(msgLabel, PRINT_REF_MSG);
        return addPrintSingle(PRINT_REFERENCE, dataSegment, labelGenerator);
    }
  };

  public static RoutineFunction addPrintln = (routine, labelGenerator, dataSegment) -> {
    /* overwrite, routine has to be PRINTLN */
    routine = PRINT_LN;

    Label printlnMsgLabel = labelGenerator.getLabel();
    dataSegment.put(printlnMsgLabel, PRINT_LN_MSG);

    /* add the helper function label */
    Label label = new Label(routine.toString());

    return List.of(
      label,
      new Push(Collections.singletonList(LR)), new LDR(r0, new LabelAddressing(printlnMsgLabel)),
      /* skip the first 4 byte of the msg which is the length of it */
      new Add(r0, r0, new Operand2(4)),
      new BL(PUTS.toString()),
      /* refresh the r0 and buffer */
      new Mov(r0, new Operand2(0)),
      new BL(FFLUSH.toString()),
      new Pop(Collections.singletonList(PC))
    );
  };

  public static RoutineFunction addThrowRuntimeError = (routine, labelGenerator, dataSegment) ->  {
    List<Instruction> instructions = new ArrayList<>(List.of(
        /* add the helper function label */
        new Label(THROW_RUNTIME_ERROR.toString()),
        new BL(PRINT_STRING.toString()),
        new Mov(r0, new Operand2(-1)),
        new BL(EXIT.toString())
    ));

    return instructions;
  };

  public static RoutineFunction addFree = (routine, labelGenerator, dataSegment) -> {

    Label msg = addMsg(PRINT_NULL_REF_MSG, dataSegment, labelGenerator);

    List<Instruction> instructions = new ArrayList<>(List.of(
        /* add the helper function label */
        new Label(routine.toString()),
        new Push(Collections.singletonList(LR)),
        new Cmp(r0, new Operand2(0)),
        new LDR(r0, new LabelAddressing(msg), LDREQ),
        new B(EQ, THROW_RUNTIME_ERROR.toString())
    ));

    if(routine.equals(FREE_PAIR)) {
      List<Instruction> free_pair_instructions = List.of(
        new Push(Collections.singletonList(r0)),
        new LDR(r0, new AddressingMode2(OFFSET, r0)),
        new BL(FREE.toString()),
        new LDR(r0, new AddressingMode2(OFFSET, SP)),
        new LDR(r0, new AddressingMode2(OFFSET, r0, 4)),
        new BL(FREE.toString()),
        new Pop(Collections.singletonList(r0))
      );
      instructions.addAll(free_pair_instructions);
    }
    instructions.add(new BL(FREE.toString()));
    instructions.add(new Pop(Collections.singletonList(PC)));

    return instructions;
  };

  public static RoutineFunction addCheckNullPointer = (routine, labelGenerator, dataSegment) -> {
    Label msgLabel = labelGenerator.getLabel();
    dataSegment.put(msgLabel, PRINT_NULL_REF_MSG);

    List<Instruction> instructions = new ArrayList<>(List.of(
        /* add the helper function label */
        new Label(CHECK_NULL_POINTER.toString()),
        new Push(Collections.singletonList(LR)),
        new Cmp(r0, new Operand2(0)),
        new LDR(r0, new LabelAddressing(msgLabel), LDREQ),
        new BL(EQ, THROW_RUNTIME_ERROR.toString()),
        new Pop(Collections.singletonList(PC))
    ));

    return instructions;
  };

  public static RoutineFunction addCheckDivByZero = (routine, labelGenerator, dataSegment) ->  {
    /* overwrite, routine has to be check divide by zero */
    routine = CHECK_DIVIDE_BY_ZERO;

    Label msgLabel = labelGenerator.getLabel();
    dataSegment.put(msgLabel, PRINT_DIV_ZERO_MSG);

    List<Instruction> instructions = new ArrayList<>(List.of(
        /* add the helper function label */
        new Label(routine.toString()),
        new Push(Collections.singletonList(LR)),
        new Cmp(r1, new Operand2(0)),
        new LDR(r0, new LabelAddressing(msgLabel), LDREQ),
        new BL(EQ, THROW_RUNTIME_ERROR.toString()),
        new Pop(Collections.singletonList(PC))
    ));

    return instructions;
  };

  public static RoutineFunction addCheckArrayBound = (routine, labelGenerator, dataSegment) ->  {
    /* overwrite, routine has to be check array bound */
    routine = CHECK_ARRAY_BOUND;

    Label negativeIndexLabel = labelGenerator.getLabel();
    dataSegment.put(negativeIndexLabel, PRINT_ARRAY_NEG_INDEX_MSG);
    Label indexOutOfBoundLabel = labelGenerator.getLabel();
    dataSegment.put(indexOutOfBoundLabel, PRINT_ARRAY_INDEX_TOO_LARGE_MSG);

    return List.of(
      new Label(routine.toString()),
      new Push(Collections.singletonList(LR)),
      new Cmp(r0, new Operand2(0)),
      new LDR(r0, new LabelAddressing(negativeIndexLabel), LDRLT),
      new BL(Cond.LT, THROW_RUNTIME_ERROR.toString()),
      new LDR(r1, new AddressingMode2(OFFSET, r1)),
      new Cmp(r0, new Operand2(r1)),
      new LDR(r0, new LabelAddressing(indexOutOfBoundLabel), LDRCS),
      new BL(Cond.CS, THROW_RUNTIME_ERROR.toString()),
      new Pop(Collections.singletonList(PC))
    );
  };

  public static RoutineFunction addThrowOverflowError = (routine, labelGenerator, dataSegment) -> {
    Label overflowMsgLabel = labelGenerator.getLabel();
    dataSegment.put(overflowMsgLabel, PRINT_OVERFLOW_MSG);

    List<Instruction> instructions = new ArrayList<>(List.of(
        new Label(Utils.RoutineInstruction.THROW_OVERFLOW_ERROR.toString()),
        new LDR(r0, new LabelAddressing(overflowMsgLabel), LDR),
        new BL(Utils.RoutineInstruction.THROW_RUNTIME_ERROR.toString())
    ));

    return instructions;
  };

  /* print string (char array included) */
  private static List<Instruction> addPrintMultiple(Map<Label, String> dataSegment, LabelGenerator labelGenerator) {
    /* add the format into the data list */
    Label msg = addMsg(PRINT_STRING_MSG, dataSegment, labelGenerator);

    List<Instruction> instructions = new ArrayList<>(List.of(
        /* add the helper function label */
        new Label(PRINT_STRING.toString()),
        new Push(Collections.singletonList(LR)),
        /* put the string length into r1 as snd arg */
        new LDR(r1, new AddressingMode2(OFFSET, r0)),
        /* skip the fst 4 bytes which is the length of the string */
        new Add(r2, r0, new Operand2(4)),
        new LDR(r0, new LabelAddressing(msg))
    ));
    instructions.addAll(addCommonPrint());

    return instructions;
  }

  /* print int, print char or print reference */
  private static List<Instruction> addPrintSingle(RoutineInstruction routine, Map<Label, String> dataSegment, LabelGenerator labelGenerator) {
    /* add the format into the data list */
    String asciiMsg = routine.equals(PRINT_INT) ? PRINT_INT_MSG : PRINT_REF_MSG;
    Label msg = addMsg(asciiMsg, dataSegment, labelGenerator);

    List<Instruction> instructions = new ArrayList<>(List.of(
        /* add the helper function label */
        new Label(routine.toString()),
        new Push(Collections.singletonList(LR)),
        /* put the content in r0 int o r1 as the snd arg of printf */
        new Mov(r1, new Operand2(r0)),
        /* fst arg of printf is the format */
        new LDR(r0, new LabelAddressing(msg))
    ));
    instructions.addAll(addCommonPrint());

    return instructions;
  }

  /* print bool */
  private static List<Instruction> addPrintBool(Map<Label, String> dataSegment, LabelGenerator labelGenerator) {
    /* add the msgTrue into the data list */
    Label msgTrue = addMsg(PRINT_BOOL_TRUE, dataSegment, labelGenerator);
    /* add the msgFalse into the data list */
    Label msgFalse = addMsg(PRINT_BOOL_FALSE, dataSegment, labelGenerator);

    List<Instruction> instructions = new ArrayList<>(List.of(
        /* add the helper function label */
        new Label(PRINT_BOOL.toString()),
        new Push(Collections.singletonList(LR)),
        /* cmp the content in r0 with 0 */
        new Cmp(r0, new Operand2(0)),
        /* if not equal to 0 LDR true */
        new LDR(r0, new LabelAddressing(msgTrue), LDRNE),
        /* otherwise equal to 0 LDR false */
        new LDR(r0, new LabelAddressing(msgFalse), LDREQ)
    ));
    instructions.addAll(addCommonPrint());
    return instructions;
  }

  public static final Map<RoutineInstruction, RoutineFunction> routineFunctionMap = Map.ofEntries(
      new AbstractMap.SimpleEntry<>(RoutineInstruction.READ_INT, addRead),
      new AbstractMap.SimpleEntry<>(RoutineInstruction.READ_CHAR, addRead),
      new AbstractMap.SimpleEntry<>(RoutineInstruction.PRINT_INT, addPrint),
      new AbstractMap.SimpleEntry<>(RoutineInstruction.PRINT_BOOL, addPrint),
      new AbstractMap.SimpleEntry<>(RoutineInstruction.PRINT_CHAR, addPrint),
      new AbstractMap.SimpleEntry<>(RoutineInstruction.PRINT_STRING, addPrint),
      new AbstractMap.SimpleEntry<>(RoutineInstruction.PRINT_REFERENCE, addPrint),
      new AbstractMap.SimpleEntry<>(RoutineInstruction.PRINT_LN, addPrintln),
      new AbstractMap.SimpleEntry<>(RoutineInstruction.CHECK_DIVIDE_BY_ZERO, addCheckDivByZero),
      new AbstractMap.SimpleEntry<>(RoutineInstruction.THROW_RUNTIME_ERROR, addThrowRuntimeError),
      new AbstractMap.SimpleEntry<>(RoutineInstruction.CHECK_ARRAY_BOUND, addCheckArrayBound),
      new AbstractMap.SimpleEntry<>(RoutineInstruction.FREE_ARRAY, addFree),
      new AbstractMap.SimpleEntry<>(RoutineInstruction.FREE_PAIR, addFree),
      new AbstractMap.SimpleEntry<>(RoutineInstruction.CHECK_NULL_POINTER, addCheckNullPointer),
      new AbstractMap.SimpleEntry<>(RoutineInstruction.THROW_OVERFLOW_ERROR, addThrowOverflowError)
  );

  private static List<Instruction> addCommonPrint() {
    return List.of(
      /* skip the first 4 byte of the msg which is the length of it */
      new Add(r0, r0, new Operand2(4)),
      new BL(PRINTF.toString()),
      /* refresh the r0 and buffer */
      new Mov(r0, new Operand2(0)),
      new BL(FFLUSH.toString()),
      new Pop(Collections.singletonList(PC))
    );
  }

  private static Label addMsg(String msgAscii, Map<Label, String> data, LabelGenerator labelGenerator) {
    /* add a Msg into the data list */
    Label msgLabel = labelGenerator.getLabel();
    data.put(msgLabel, msgAscii);
    return msgLabel;
  }
}
