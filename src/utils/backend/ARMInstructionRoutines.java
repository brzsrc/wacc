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
import backend.instructions.operand.Operand2;

import java.util.*;

import utils.Utils;
import utils.Utils.RoutineInstruction;
import utils.Utils.SystemCallInstruction;
import static utils.backend.ARMConcreteRegister.*;

import static utils.Utils.RoutineInstruction.*;

public class ARMInstructionRoutines {
  /*
   * record which helpers already exist, we don't want repeated helper functions
   */
  private static Set<RoutineInstruction> alreadyExist = new HashSet<>();


  /* map for addPrintSingle */
  private static Map<RoutineInstruction, String> printSingleMap = new HashMap<>() {
    {
      put(RoutineInstruction.PRINT_INT, "\"%d\\0\"");
      put(RoutineInstruction.PRINT_REFERENCE, "\"%p\\0\"");
    }
  };

  public static RoutineFunction addRead = (routine, labelGenerator, dataSegment) -> {
    /* add the helper function label */
    Label readLabel = new Label(routine.toString());

    /* add the format into the data list */
    String ascii = routine == READ_INT ? "\"%d\\0\"" : "\" %c\\0\"";
    Label msgLabel = labelGenerator.getLabel();
    dataSegment.put(msgLabel, ascii);

    List<Instruction> instructions = List.of(
            readLabel,
            new Push(Collections.singletonList(LR)),
    /* fst arg of read is the snd arg of scanf (storing address) */
            new Mov(r1, new Operand2(r0)),
    /* fst arg of scanf is the format */
            new LDR(r0, new LabelAddressing(readLabel)),
    /* skip the first 4 byte of the msg which is the length of it */
            new Add(r0, r0, new Operand2(4)), new BL(SystemCallInstruction.SCANF.toString()),
            new Pop(Collections.singletonList(PC)));

    return instructions;
  };

  public static RoutineFunction addPrint = (routine, labelGenerator, dataSegment) -> {

    Label msgLabel = labelGenerator.getLabel();
    switch (routine) {
      case PRINT_CHAR:
        return new ArrayList<>();
      case PRINT_BOOL:
        /* add the printing true into .data section */
//        dataSegment.put(msgLabel, Utils.routineMsgMapping.get(RoutineInstruction.PRINT_BOOL).get(0));
//        Label sndLabel = labelGenerator.getLabel();
//        dataSegment.put(sndLabel, Utils.routineMsgMapping.get(RoutineInstruction.PRINT_BOOL).get(1));
        return addPrintBool(dataSegment, labelGenerator);
      case PRINT_STRING:
        return addPrintMultiple(dataSegment, labelGenerator);
      case PRINT_INT:
        return addPrintSingle(PRINT_INT, dataSegment, labelGenerator);
      case PRINT_REFERENCE:
      default:
        dataSegment.put(msgLabel, "\"%p\\0\"");
        return addPrintSingle(PRINT_REFERENCE, dataSegment, labelGenerator);
    }
  };

  public static RoutineFunction addPrintln = (routine, labelGenerator, dataSegment) -> {
    /* overwrite, routine has to be PRINTLN */
    routine = RoutineInstruction.PRINT_LN;

    Label printlnMsgLabel = labelGenerator.getLabel();
    dataSegment.put(printlnMsgLabel, "\"\\0\"");

    /* add the helper function label */
    Label label = new Label(routine.toString());
    List<Instruction> instructions = List.of(
            label,
            new Push(Collections.singletonList(LR)), new LDR(r0, new LabelAddressing(printlnMsgLabel)),
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
    instructions.addAll(addPrintMultiple(dataSegment, labelGenerator));

    return instructions;
  };

  public static RoutineFunction addFree = (routine, labelGenerator, dataSegment)->{
    List<Instruction> instructions = new ArrayList<>();

    Label msg = null;
    for(Label msg_ : dataSegment.keySet()) {
      if(dataSegment.get(msg_).equals("\"NullReferenceError: dereference a null reference\\n\\0\"")) {
        msg = msg_;
        break;
      }
    }
    if(msg == null) {
      msg = addMsg("\"NullReferenceError: dereference a null reference\\n\\0\"", dataSegment, labelGenerator);
    }

    /* add the helper function label */
    Label freeLabel = new Label(routine.toString());

    instructions.add(freeLabel);
    instructions.add(new Push(Collections.singletonList(LR)));
    instructions.add(new Cmp(r0, new Operand2(0)));
    instructions.add(new LDR(r0, new LabelAddressing(msg), LdrMode.LDREQ));
    instructions.add(new B(Cond.EQ, RoutineInstruction.THROW_RUNTIME_ERROR.toString()));

    if(routine.equals(RoutineInstruction.FREE_PAIR)) {
      instructions.add(new Push(Collections.singletonList(r0)));
      instructions.add(new LDR(r0, new RegAddressing(r0)));
      instructions.add(new BL(SystemCallInstruction.FREE.toString()));
      instructions.add(new LDR(r0, new RegAddressing(SP)));
      instructions.add(new LDR(r0, new AddressingMode2(AddrMode2.OFFSET, r0, 4)));
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

    Label msgLabel = labelGenerator.getLabel();
    dataSegment.put(msgLabel, "\"NullReferenceError: dereference a null reference\\n\\0\"");
    Label printlnLabel = labelGenerator.getLabel();
    dataSegment.put(printlnLabel, Utils.routineMsgMapping.get(RoutineInstruction.CHECK_NULL_POINTER).get(1));

    /* add the helper function label */
    Label label = new Label(RoutineInstruction.CHECK_NULL_POINTER.toString());
    instructions.add(label);
    instructions.add(new Push(Collections.singletonList(LR)));
    instructions.add(new Cmp(r0, new Operand2(0)));
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
    dataSegment.put(msgLabel, "\"DivideByZeroError: divide or modulo by zero\\n\\0\"");

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
    instructions.add(new Cmp(r0, new Operand2(0)));
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
    dataSegment.put(overflowMsgLabel, "\"OverflowError: the result is too small/large to store in a 4-byte signed-integer.\\n\\0\"");

    instructions.add(new Label("p_throw_overflow_error"));
    instructions.add(new LDR(r0, new LabelAddressing(overflowMsgLabel), LdrMode.LDR));
    instructions.add(new BL("p_throw_runtime_error"));
    instructions.addAll(addThrowRuntimeError.routineFunctionAssemble(RoutineInstruction.THROW_RUNTIME_ERROR, labelGenerator, dataSegment));

    return instructions;
  };

  /* print string (char array included) */
  private static List<Instruction> addPrintMultiple(Map<Label, String> dataSegment, LabelGenerator labelGenerator) {
      List<Instruction> instructions = new ArrayList<>();

    /* only add the helper if it doesn't exist yet */
    if (!alreadyExist.contains(PRINT_STRING)) {

      /* add this helper into alreadyExist list */
      alreadyExist.add(PRINT_STRING);

      /* add the format into the data list */
      Label msg = addMsg("\"%.*s\\0\"", dataSegment, labelGenerator);

      /* add the helper function label */
      Label label = new Label(PRINT_STRING.toString());
      instructions.add(label);
      instructions.add(new Push(Collections.singletonList(LR)));
      /* put the string length into r1 as snd arg */
      instructions.add(new LDR(r1, new RegAddressing(r0)));
      /* skip the fst 4 bytes which is the length of the string */
      instructions.add(new Add(r2, r0, new Operand2(4)));
      instructions.add(new LDR(r0, new LabelAddressing(msg)));

      instructions.addAll(addCommonPrint());
    }

    return instructions;
  }

  /* print int, print char or print reference */
  private static List<Instruction> addPrintSingle (RoutineInstruction routine, Map<Label, String> dataSegment, LabelGenerator labelGenerator) {
    List<Instruction> instructions = new ArrayList<>();


    /* only add the helper if it doesn't exist yet */
    if (!alreadyExist.contains(routine)) {

      /* add this helper into alreadyExist list */
      alreadyExist.add(routine);

      /* add the format into the data list */
      Label msg = addMsg(printSingleMap.get(routine), dataSegment, labelGenerator);

      /* add the helper function label */
      Label label = new Label(routine.toString());
      instructions.add(label);
      instructions.add(new Push(Collections.singletonList(LR)));
      /* put the content in r0 int o r1 as the snd arg of printf */
      instructions.add(new Mov(r1, new Operand2(r0)));
      /* fst arg of printf is the format */
      instructions.add(new LDR(r0, new LabelAddressing(msg)));

      instructions.addAll(addCommonPrint());
    }

    return instructions;
  }

  /* print bool */
  private static List<Instruction> addPrintBool(Map<Label, String> dataSegment, LabelGenerator labelGenerator) {
    List<Instruction> instructions = new ArrayList<>();

    /* only add the helper if it doesn't exist yet */
    if (!alreadyExist.contains(PRINT_BOOL)) {

      /* add this helper into alreadyExist list */
      alreadyExist.add(PRINT_BOOL);

      /* add the msgTrue into the data list */
      Label msgTrue = addMsg("\"true\\0\"", dataSegment, labelGenerator);
      /* add the msgFalse into the data list */
      Label msgFalse = addMsg("\"false\\0\"", dataSegment, labelGenerator);

      /* add the helper function label */
      Label label = new Label(PRINT_BOOL.toString());
      instructions.add(label);
      instructions.add(new Push(Collections.singletonList(LR)));
      /* cmp the content in r0 with 0 */
      instructions.add(new Cmp(r0, new Operand2(0)));
      /* if not equal to 0 LDR true */
      instructions.add(new LDR(r0, new LabelAddressing(msgTrue), LdrMode.LDRNE));
      /* otherwise equal to 0 LDR false */
      instructions.add(new LDR(r0, new LabelAddressing(msgFalse), LdrMode.LDREQ));

      instructions.addAll(addCommonPrint());
    }

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

  private static Label addMsg(String msgAscii, Map<Label, String> data, LabelGenerator labelGenerator) {
    /* add a Msg into the data list */
    Label msgLabel = labelGenerator.getLabel();
    data.put(msgLabel, msgAscii);
    return msgLabel;
  }

}
