package backend.arm.instructions;

import static backend.arm.instructions.LDR.LdrMode.LDR;
import static backend.arm.instructions.LDR.LdrMode.LDRCS;
import static backend.arm.instructions.LDR.LdrMode.LDREQ;
import static backend.arm.instructions.LDR.LdrMode.LDRLT;
import static backend.arm.instructions.LDR.LdrMode.LDRNE;
import static backend.arm.instructions.addressing.AddressingMode2.AddrMode2.OFFSET;
import static utils.Utils.RoutineInstruction.CHECK_ARRAY_BOUND;
import static utils.Utils.RoutineInstruction.CHECK_DIVIDE_BY_ZERO;
import static utils.Utils.RoutineInstruction.CHECK_NULL_POINTER;
import static utils.Utils.RoutineInstruction.FREE_PAIR;
import static utils.Utils.RoutineInstruction.PRINT_BOOL;
import static utils.Utils.RoutineInstruction.PRINT_INT;
import static utils.Utils.RoutineInstruction.PRINT_LN;
import static utils.Utils.RoutineInstruction.PRINT_REFERENCE;
import static utils.Utils.RoutineInstruction.PRINT_STRING;
import static utils.Utils.RoutineInstruction.READ_INT;
import static utils.Utils.RoutineInstruction.THROW_RUNTIME_ERROR;
import static utils.Utils.SystemCallInstruction.EXIT;
import static utils.Utils.SystemCallInstruction.FFLUSH;
import static utils.Utils.SystemCallInstruction.FREE;
import static utils.Utils.SystemCallInstruction.PRINTF;
import static utils.Utils.SystemCallInstruction.PUTS;
import static utils.Utils.SystemCallInstruction.SCANF;
import static utils.backend.Cond.EQ;
import static utils.backend.register.arm.ARMConcreteRegister.LR;
import static utils.backend.register.arm.ARMConcreteRegister.PC;
import static utils.backend.register.arm.ARMConcreteRegister.SP;
import static utils.backend.register.arm.ARMConcreteRegister.r0;
import static utils.backend.register.arm.ARMConcreteRegister.r1;
import static utils.backend.register.arm.ARMConcreteRegister.r2;

import backend.arm.instructions.addressing.AddressingMode2;
import backend.arm.instructions.addressing.ImmedAddress;
import backend.arm.instructions.arithmeticLogic.Add;
import backend.arm.instructions.addressing.Operand2;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import utils.Utils;
import utils.Utils.RoutineInstruction;
import utils.backend.Cond;
import utils.backend.LabelGenerator;
import utils.backend.RoutineFunction;

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
  public static RoutineFunction addRead = (routine, labelGenerator, dataSegment) -> {
    /* add the helper function label */
    Label readLabel = new Label(routine.toString());

    /* add the format into the data list */
    String asciiMsg = routine == READ_INT ? PRINT_INT_MSG : PRINT_CHAR_MSG;
    Label msgLabel = labelGenerator.getLabel().asArmLabel();
    dataSegment.put(msgLabel, asciiMsg);

    return List.of(
        readLabel,
        new Push(Collections.singletonList(LR)),
        /* fst arg of read is the snd arg of scanf (storing address) */
        new Mov(r1, new Operand2(r0)),
        /* fst arg of scanf is the format */
        new LDR(r0, new ImmedAddress(msgLabel)),
        /* skip the first 4 byte of the msg which is the length of it */
        new Add(r0, r0, new Operand2(4)), new BL(SCANF.toString()),
        new Pop(Collections.singletonList(PC)));
  };
  public static RoutineFunction addPrint = (routine, labelGenerator, dataSegment) -> {
    Label msgLabel = labelGenerator.getLabel().asArmLabel();
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

    Label printlnMsgLabel = labelGenerator.getLabel().asArmLabel();
    dataSegment.put(printlnMsgLabel, PRINT_LN_MSG);

    /* add the helper function label */
    Label label = new Label(routine.toString());

    return List.of(
        label,
        new Push(Collections.singletonList(LR)), new LDR(r0, new ImmedAddress(printlnMsgLabel)),
        /* skip the first 4 byte of the msg which is the length of it */
        new Add(r0, r0, new Operand2(4)),
        new BL(PUTS.toString()),
        /* refresh the r0 and buffer */
        new Mov(r0, new Operand2(0)),
        new BL(FFLUSH.toString()),
        new Pop(Collections.singletonList(PC))
    );
  };
  public static RoutineFunction addThrowRuntimeError = (routine, labelGenerator, dataSegment) -> {
    List<ARMInstruction> instructions = new ArrayList<>(List.of(
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

    List<ARMInstruction> instructions = new ArrayList<ARMInstruction>(List.of(
        /* add the helper function label */
        new Label(routine.toString()),
        new Push(Collections.singletonList(LR)),
        new Cmp(r0, new Operand2(0)),
        new LDR(r0, new ImmedAddress(msg), LDREQ),
        new B(EQ, THROW_RUNTIME_ERROR.toString())
    ));

    if (routine.equals(FREE_PAIR)) {
      List<ARMInstruction> free_pair_instructions = List.of(
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
    Label msgLabel = labelGenerator.getLabel().asArmLabel();
    dataSegment.put(msgLabel, PRINT_NULL_REF_MSG);

    List<ARMInstruction> instructions = new ArrayList<>(List.of(
        /* add the helper function label */
        new Label(CHECK_NULL_POINTER.toString()),
        new Push(Collections.singletonList(LR)),
        new Cmp(r0, new Operand2(0)),
        new LDR(r0, new ImmedAddress(msgLabel), LDREQ),
        new BL(EQ, THROW_RUNTIME_ERROR.toString()),
        new Pop(Collections.singletonList(PC))
    ));

    return instructions;
  };
  public static RoutineFunction addCheckDivByZero = (routine, labelGenerator, dataSegment) -> {
    /* overwrite, routine has to be check divide by zero */
    routine = CHECK_DIVIDE_BY_ZERO;

    Label msgLabel = labelGenerator.getLabel().asArmLabel();
    dataSegment.put(msgLabel, PRINT_DIV_ZERO_MSG);

    List<ARMInstruction> instructions = new ArrayList<>(List.of(
        /* add the helper function label */
        new Label(routine.toString()),
        new Push(Collections.singletonList(LR)),
        new Cmp(r1, new Operand2(0)),
        new LDR(r0, new ImmedAddress(msgLabel), LDREQ),
        new BL(EQ, THROW_RUNTIME_ERROR.toString()),
        new Pop(Collections.singletonList(PC))
    ));

    return instructions;
  };
  public static RoutineFunction addCheckArrayBound = (routine, labelGenerator, dataSegment) -> {
    /* overwrite, routine has to be check array bound */
    routine = CHECK_ARRAY_BOUND;

    Label negativeIndexLabel = labelGenerator.getLabel().asArmLabel();
    dataSegment.put(negativeIndexLabel, PRINT_ARRAY_NEG_INDEX_MSG);
    Label indexOutOfBoundLabel = labelGenerator.getLabel().asArmLabel();
    dataSegment.put(indexOutOfBoundLabel, PRINT_ARRAY_INDEX_TOO_LARGE_MSG);

    return List.of(
        new Label(routine.toString()),
        new Push(Collections.singletonList(LR)),
        new Cmp(r0, new Operand2(0)),
        new LDR(r0, new ImmedAddress(negativeIndexLabel), LDRLT),
        new BL(Cond.LT, THROW_RUNTIME_ERROR.toString()),
        new LDR(r1, new AddressingMode2(OFFSET, r1)),
        new Cmp(r0, new Operand2(r1)),
        new LDR(r0, new ImmedAddress(indexOutOfBoundLabel), LDRCS),
        new BL(Cond.CS, THROW_RUNTIME_ERROR.toString()),
        new Pop(Collections.singletonList(PC))
    );
  };
  public static RoutineFunction addThrowOverflowError = (routine, labelGenerator, dataSegment) -> {
    Label overflowMsgLabel = labelGenerator.getLabel().asArmLabel();
    dataSegment.put(overflowMsgLabel, PRINT_OVERFLOW_MSG);

    List<ARMInstruction> instructions = new ArrayList<>(List.of(
        new Label(Utils.RoutineInstruction.THROW_OVERFLOW_ERROR.toString()),
        new LDR(r0, new ImmedAddress(overflowMsgLabel), LDR),
        new BL(Utils.RoutineInstruction.THROW_RUNTIME_ERROR.toString())
    ));

    return instructions;
  };
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

  /* adding a private constructor to override the default public constructor in order to
     indicate ARMInstructionRoutines class cannot be instantiated */
  private ARMInstructionRoutines() {
    throw new IllegalStateException("Utility Class cannot be instantiated!");
  }

  /* print string (char array included) */
  private static List<ARMInstruction> addPrintMultiple(Map<Label, String> dataSegment,
      LabelGenerator labelGenerator) {
    /* add the format into the data list */
    Label msg = addMsg(PRINT_STRING_MSG, dataSegment, labelGenerator);

    List<ARMInstruction> instructions = new ArrayList<>(List.of(
        /* add the helper function label */
        new Label(PRINT_STRING.toString()),
        new Push(Collections.singletonList(LR)),
        /* put the string length into r1 as snd arg */
        new LDR(r1, new AddressingMode2(OFFSET, r0)),
        /* skip the fst 4 bytes which is the length of the string */
        new Add(r2, r0, new Operand2(4)),
        new LDR(r0, new ImmedAddress(msg))
    ));
    instructions.addAll(addCommonPrint());

    return instructions;
  }

  /* print int, print char or print reference */
  private static List<ARMInstruction> addPrintSingle(RoutineInstruction routine,
      Map<Label, String> dataSegment, LabelGenerator labelGenerator) {
    /* add the format into the data list */
    String asciiMsg = routine.equals(PRINT_INT) ? PRINT_INT_MSG : PRINT_REF_MSG;
    Label msg = addMsg(asciiMsg, dataSegment, labelGenerator);

    List<ARMInstruction> instructions = new ArrayList<>(List.of(
        /* add the helper function label */
        new Label(routine.toString()),
        new Push(Collections.singletonList(LR)),
        /* put the content in r0 int o r1 as the snd arg of printf */
        new Mov(r1, new Operand2(r0)),
        /* fst arg of printf is the format */
        new LDR(r0, new ImmedAddress(msg))
    ));
    instructions.addAll(addCommonPrint());

    return instructions;
  }

  /* print bool */
  private static List<ARMInstruction> addPrintBool(Map<Label, String> dataSegment,
      LabelGenerator labelGenerator) {
    /* add the msgTrue into the data list */
    Label msgTrue = addMsg(PRINT_BOOL_TRUE, dataSegment, labelGenerator);
    /* add the msgFalse into the data list */
    Label msgFalse = addMsg(PRINT_BOOL_FALSE, dataSegment, labelGenerator);

    List<ARMInstruction> instructions = new ArrayList<>(List.of(
        /* add the helper function label */
        new Label(PRINT_BOOL.toString()),
        new Push(Collections.singletonList(LR)),
        /* cmp the content in r0 with 0 */
        new Cmp(r0, new Operand2(0)),
        /* if not equal to 0 LDR true */
        new LDR(r0, new ImmedAddress(msgTrue), LDRNE),
        /* otherwise equal to 0 LDR false */
        new LDR(r0, new ImmedAddress(msgFalse), LDREQ)
    ));
    instructions.addAll(addCommonPrint());
    return instructions;
  }

  private static List<ARMInstruction> addCommonPrint() {
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

  private static Label addMsg(String msgAscii, Map<Label, String> data,
      LabelGenerator labelGenerator) {
    /* add a Msg into the data list */
    Label msgLabel = labelGenerator.getLabel().asArmLabel();
    data.put(msgLabel, msgAscii);
    return msgLabel;
  }
}
