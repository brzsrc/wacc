package utils.backend;

import static utils.Utils.BOOL_BASIC_TYPE;
import static utils.Utils.CHAR_BASIC_TYPE;
import static utils.Utils.INT_BASIC_TYPE;
import static utils.Utils.STRING_BASIC_TYPE;

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
import frontend.type.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import utils.Utils;
import utils.Utils.RoutineInstruction;
import utils.Utils.SystemCallInstruction;

import static utils.Utils.ARRAY_TYPE;
import static utils.Utils.PAIR_TYPE;

public class ARMInstructionRoutines {

  /* record which routines have already been added so that we only add it once */
  private static Set<RoutineInstruction> alreadyExist;
  /* generate labels for the .data section of the assembly */
  private static LabelGenerator labelGenerator;
  /* the .data section reference shared between this class and ARMInstructionGenerator */
  private static Map<Label, String> dataSegmentMessages;
  public ARMInstructionRoutines(Map<Label, String> dataSegmentMessages) {
    this.dataSegmentMessages = dataSegmentMessages;
    this.alreadyExist = new HashSet<>();
    this.labelGenerator = new LabelGenerator("msg_");
  }

  /* static ARM register references */
  private static final ARMConcreteRegister r0 = new ARMConcreteRegister(ARMRegisterLabel.R0);
  private static final ARMConcreteRegister r1 = new ARMConcreteRegister(ARMRegisterLabel.R1);
  private static final ARMConcreteRegister LR = new ARMConcreteRegister(ARMRegisterLabel.LR);
  private static final ARMConcreteRegister PC = new ARMConcreteRegister(ARMRegisterLabel.PC);

  public List<Instruction> addRead(RoutineInstruction routine) {
    List<Instruction> instructions = new ArrayList<>();
    /* only add the helper if it doesn't exist yet */
    if (!alreadyExist.contains(routine)) {
      alreadyExist.add(routine);

      /* create the read msg label and add it to the .data section at the same time */
      Label msgLabel = addMsgAndReturn(Utils.routineMsgMapping.get(routine));
      /* add the helper function label */
      Label readLabel = new Label(routine.toString());

      instructions = List.of(readLabel, new Push(Collections.singletonList(LR)),
          new Mov(r1, new Operand2(r0)), new LDR(r0, new LabelAddressing(msgLabel)),
          new Add(r0, r0, new Operand2(4)), new BL(SystemCallInstruction.SCANF.toString()),
          new Pop(Collections.singletonList(PC)));
    }

    return instructions;
  }

  public static void addPrint(Type type, List<Instruction> instructions, Map<Label, String> data,
      List<Instruction> helperFunctions, ARMConcreteRegisterAllocator allocator) {

    /* TODO: need to refactor here */
    if (type.equalToType(INT_BASIC_TYPE)) {
      instructions.add(new BL(RoutineInstruction.PRINT_INT.toString()));
      addPrintSingle(RoutineInstruction.PRINT_INT, data, helperFunctions, allocator);
    } else if (type.equalToType(CHAR_BASIC_TYPE)) {
      instructions.add(new BL("putchar"));
      // addPrintSingle(Helper.PRINT_CHAR, data, helperFunctions, allocator);
    } else if (type.equalToType(BOOL_BASIC_TYPE)) {
      instructions.add(new BL(RoutineInstruction.PRINT_BOOL.toString()));
      addPrintBool(data, helperFunctions, allocator);
    } else if (type.equalToType(STRING_BASIC_TYPE) || type.equalToType(Utils.CHAR_ARRAY_TYPE)) {
      instructions.add(new BL(RoutineInstruction.PRINT_STRING.toString()));
      addPrintMultiple(data, helperFunctions, allocator);
    } else {
      instructions.add(new BL(RoutineInstruction.PRINT_REFERENCE.toString()));
      addPrintSingle(RoutineInstruction.PRINT_REFERENCE, data, helperFunctions, allocator);
    }

  }

  public static void addPrintln(List<Instruction> instructions, Map<Label, String> data,
      List<Instruction> helperFunctions, ARMConcreteRegisterAllocator allocator) {

    RoutineInstruction routineInstruction = RoutineInstruction.PRINT_LN;
    /* call the helper function anyway */
    instructions.add(new BL(routineInstruction.toString()));

    /* only add the helper if it doesn't exist yet */
    if (!alreadyExist.contains(routineInstruction)) {

      /* add this helper into alreadyExist list */
      alreadyExist.add(routineInstruction);

      /* add the format into the data list */
      Label msg = addMsgAndReturn("\"\\0\"");

      /* add the helper function label */
      Label label = new Label(routineInstruction.toString());
      helperFunctions.add(label);
      helperFunctions.add(new Push(Collections.singletonList(allocator.get(ARMRegisterLabel.LR))));
      helperFunctions.add(new LDR(allocator.get(0), new LabelAddressing(msg)));
      /* skip the first 4 byte of the msg which is the length of it */
      helperFunctions.add(new Add(allocator.get(0), allocator.get(0), new Operand2(new Immediate(4, BitNum.CONST8))));
      helperFunctions.add(new BL("puts"));
      /* refresh the r0 and buffer */
      helperFunctions.add(new Mov(allocator.get(0), new Operand2(new Immediate(0, BitNum.CONST8))));
      helperFunctions.add(new BL("fflush"));
      helperFunctions.add(new Pop(Collections.singletonList(allocator.get(ARMRegisterLabel.PC))));
    }

  }

  public static void addFree(Type type, Map<Label, String> data, List<Instruction> helperFunctions,
    ARMConcreteRegisterAllocator allocator) {
      RoutineInstruction routineInstruction = (type.equalToType(ARRAY_TYPE))? RoutineInstruction.FREE_ARRAY : RoutineInstruction.FREE_PAIR;

      /* only add the helper if it doesn't exist yet */
      if (!alreadyExist.contains(routineInstruction)) {

        /* add this helper into alreadyExist list */
        alreadyExist.add(routineInstruction);

        Label msg = null;
        for (Label msg_ : data.keySet()) {
          if(data.get(msg_).equals("\"NullReferenceError: dereference a null reference\\n\\0\"")) {
            msg = msg_;
            break;
          }
        }
        if(msg == null)
          msg = addMsgAndReturn("\"NullReferenceError: dereference a null reference\\n\\0\"");

        /* add the helper function label */
        Label label = new Label(routineInstruction.toString());
        helperFunctions.add(label);
        helperFunctions.add(new Push(Collections.singletonList(allocator.get(ARMRegisterLabel.LR))));
        helperFunctions.add(new Cmp(allocator.get(0), new Operand2(new Immediate(0, BitNum.CONST8))));
        helperFunctions.add(new LDR(allocator.get(0), new LabelAddressing(msg), LdrMode.LDREQ));
        helperFunctions.add(new B(Cond.EQ, "p_throw_runtime_error"));
        if(type.equalToType(PAIR_TYPE)) {
          helperFunctions.add(new Push(Collections.singletonList(allocator.get(0))));
          helperFunctions.add(new LDR(allocator.get(0), new RegAddressing(allocator.get(0))));
          helperFunctions.add(new BL("free"));
          helperFunctions.add(new LDR(allocator.get(0), new RegAddressing(allocator.get(13))));
          helperFunctions.add(new LDR(allocator.get(0), new AddressingMode2(AddrMode2.OFFSET, allocator.get(0), new Immediate(4, BitNum.CONST8))));
          helperFunctions.add(new BL("free"));
          helperFunctions.add(new Pop(Collections.singletonList(allocator.get(0))));
        }
        helperFunctions.add(new BL("free"));
        helperFunctions.add(new Pop(Collections.singletonList(allocator.get(15))));
        addThrowRuntimeError(data, helperFunctions, allocator);
      }
  }

  public static void addCheckNullPointer(Map<Label, String> data, List<Instruction> helperFunctions,
      ARMConcreteRegisterAllocator allocator) {
    RoutineInstruction routineInstruction = RoutineInstruction.CHECK_NULL_POINTER;

    if (alreadyExist.contains(routineInstruction)) {
      return;
    }
    
    /* add this helper into alreadyExist list */
    alreadyExist.add(routineInstruction);

    /* add the error message into the data list */
    
    Label msg = addMsgAndReturn("\"NullReferenceError: dereference a null reference\\n\\0\"");

    /* add the helper function label */
    Label label = new Label(routineInstruction.toString());
    helperFunctions.add(label);
    helperFunctions.add(new Push(Collections.singletonList(allocator.get(14))));
    helperFunctions.add(new Cmp(allocator.get(0), new Operand2(new Immediate(0, BitNum.CONST8))));
    helperFunctions.add(new LDR(allocator.get(0), new LabelAddressing(msg), LdrMode.LDREQ));
    helperFunctions.add(new BL(Cond.EQ, RoutineInstruction.THROW_RUNTIME_ERROR.toString()));
    helperFunctions.add(new Pop(Collections.singletonList(allocator.get(15))));
    addThrowRuntimeError(data, helperFunctions, allocator);

  }

  public static void addCheckDivByZero(Map<Label, String> data, List<Instruction> helperFunctions,
      ARMConcreteRegisterAllocator allocator) {
    RoutineInstruction routineInstruction = RoutineInstruction.CHECK_DIVIDE_BY_ZERO;
    /*
     * add this instr outside this func cuz only DIV and MOD will need to call this
     * func
     */

    /* only add the helper if it doesn't exist yet */
    if (!alreadyExist.contains(routineInstruction)) {

      /* add this helper into alreadyExist list */
      alreadyExist.add(routineInstruction);

      /* add the error message into the data list */
      Label msg = addMsgAndReturn("\"DivideByZeroError: divide or modulo by zero\\n\\0\"");

      /* add the helper function label */
      Label label = new Label(routineInstruction.toString());
      helperFunctions.add(label);
      helperFunctions.add(new Push(Collections.singletonList(allocator.get(14))));
      helperFunctions.add(new Cmp(allocator.get(1), new Operand2(new Immediate(0, BitNum.CONST8))));
      helperFunctions.add(new LDR(allocator.get(0), new LabelAddressing(msg), LdrMode.LDREQ));
      helperFunctions.add(new BL(Cond.EQ, RoutineInstruction.THROW_RUNTIME_ERROR.toString()));
      helperFunctions.add(new Pop(Collections.singletonList(allocator.get(15))));
      addThrowRuntimeError(data, helperFunctions, allocator);
    }
  }

  public static void addCheckArrayBound(Map<Label, String> data, List<Instruction> helperFunctions,
      ARMConcreteRegisterAllocator allocator) {
    RoutineInstruction routineInstruction = RoutineInstruction.CHECK_ARRAY_BOUND;

    if (!alreadyExist.contains(routineInstruction)) {
      /* add this helper into alreadyExist list */
      alreadyExist.add(routineInstruction);

      Label negativeIndexLabel = addMsgAndReturn("\"ArrayIndexOutOfBoundsError: negative index\\n\\0\"");
      Label indexOutOfBoundLabel = addMsgAndReturn("\"ArrayIndexOutOfBoundsError: index too large\\n\\0\"");

      helperFunctions.add(new Label("p_check_array_bounds"));
      helperFunctions.add(new Push(Collections.singletonList(allocator.get(14))));
      helperFunctions.add(new Cmp(allocator.get(0), new Operand2(new Immediate(0, BitNum.CONST8))));
      helperFunctions.add(new LDR(allocator.get(0), new LabelAddressing(negativeIndexLabel), LdrMode.LDRLT));
      helperFunctions.add(new BL(Cond.LT, "p_throw_runtime_error"));
      helperFunctions.add(new LDR(allocator.get(1), new AddressingMode2(AddrMode2.OFFSET, allocator.get(1))));
      helperFunctions.add(new Cmp(allocator.get(0), new Operand2(allocator.get(1))));
      helperFunctions.add(new LDR(allocator.get(0), new LabelAddressing(indexOutOfBoundLabel), LdrMode.LDRCS));
      helperFunctions.add(new BL(Cond.CS, "p_throw_runtime_error"));
      helperFunctions.add(new Pop(Collections.singletonList(allocator.get(15))));
    }
  }

  public static void addThrowOverflowError(Map<Label, String> data, List<Instruction> helperFunctions,
      ARMConcreteRegisterAllocator allocator) {
    RoutineInstruction routineInstruction = RoutineInstruction.THROW_OVERFLOW_ERROR;

    if (!alreadyExist.contains(routineInstruction)) {
      /* add this helper into alreadyExist list */
      alreadyExist.add(routineInstruction);

      Label msg = addMsgAndReturn("\"OverflowError: the result is too small/large to store in a 4-byte signed-integer.\\n\\0\"");
      helperFunctions.add(new Label("p_throw_overflow_error"));
      helperFunctions.add(new LDR(allocator.get(0), new LabelAddressing(msg), LdrMode.LDR));
      helperFunctions.add(new BL("p_throw_runtime_error"));
      addThrowRuntimeError(data, helperFunctions, allocator);
    }
  }

  public static void addThrowRuntimeError(Map<Label, String> data, List<Instruction> helperFunctions,
      ARMConcreteRegisterAllocator allocator) {
    RoutineInstruction routineInstruction = RoutineInstruction.THROW_RUNTIME_ERROR;

    /* only add the helper if it doesn't exist yet */
    if (!alreadyExist.contains(routineInstruction)) {

      /* add this helper into alreadyExist list */
      alreadyExist.add(routineInstruction);

      /* add the helper function label */
      Label label = new Label("p_throw_runtime_error");
      helperFunctions.add(label);
      helperFunctions.add(new BL(RoutineInstruction.PRINT_STRING.toString()));
      helperFunctions.add(new Mov(allocator.get(0), new Operand2(new Immediate(-1, BitNum.CONST8))));
      helperFunctions.add(new BL("exit"));
      addPrintMultiple(data, helperFunctions, allocator);
    }
  }

  /* print string (char array included) */
  private static void addPrintMultiple(Map<Label, String> data, List<Instruction> helperFunctions,
      ARMConcreteRegisterAllocator allocator) {

    RoutineInstruction routineInstruction = RoutineInstruction.PRINT_STRING;

    /* only add the helper if it doesn't exist yet */
    if (!alreadyExist.contains(routineInstruction)) {

      /* add this helper into alreadyExist list */
      alreadyExist.add(routineInstruction);

      /* add the format into the data list */
      Label msg = addMsgAndReturn("\"%.*s\\0\"");

      /* add the helper function label */
      Label label = new Label(routineInstruction.toString());
      helperFunctions.add(label);
      helperFunctions.add(new Push(Collections.singletonList(allocator.get(ARMRegisterLabel.LR))));
      /* put the string length into r1 as snd arg */
      helperFunctions.add(new LDR(allocator.get(1), new RegAddressing(allocator.get(0))));
      /* skip the fst 4 bytes which is the length of the string */
      helperFunctions.add(new Add(allocator.get(2), allocator.get(0), new Operand2(new Immediate(4, BitNum.CONST8))));
      helperFunctions.add(new LDR(allocator.get(0), new LabelAddressing(msg)));

      addCommonPrint(helperFunctions, allocator);
    }
  }

  /* print int, print char or print reference */
  private static void addPrintSingle(RoutineInstruction routineInstruction, Map<Label, String> data, List<Instruction> helperFunctions,
      ARMConcreteRegisterAllocator allocator) {

    /* only add the helper if it doesn't exist yet */
    if (!alreadyExist.contains(routineInstruction)) {

      /* add this helper into alreadyExist list */
      alreadyExist.add(routineInstruction);

      /* add the format into the data list */
      Label msg = addMsgAndReturn(Utils.routineMsgMapping.get(routineInstruction));

      /* add the helper function label */
      Label label = new Label(routineInstruction.toString());
      helperFunctions.add(label);
      helperFunctions.add(new Push(Collections.singletonList(allocator.get(ARMRegisterLabel.LR))));
      /* put the content in r0 int o r1 as the snd arg of printf */
      helperFunctions.add(new Mov(allocator.get(1), new Operand2(allocator.get(0))));
      /* fst arg of printf is the format */
      helperFunctions.add(new LDR(allocator.get(0), new LabelAddressing(msg)));

      addCommonPrint(helperFunctions, allocator);
    }
  }

  /* print bool */
  private static void addPrintBool(Map<Label, String> data, List<Instruction> helperFunctions,
      ARMConcreteRegisterAllocator allocator) {

    RoutineInstruction routineInstruction = RoutineInstruction.PRINT_BOOL;

    /* only add the helper if it doesn't exist yet */
    if (!alreadyExist.contains(routineInstruction)) {

      /* add this helper into alreadyExist list */
      alreadyExist.add(routineInstruction);

      /* add the msgTrue into the data list */
      Label msgTrue = addMsgAndReturn("\"true\\0\"");
      /* add the msgFalse into the data list */
      Label msgFalse = addMsgAndReturn("\"false\\0\"");

      /* add the helper function label */
      Label label = new Label(routineInstruction.toString());
      helperFunctions.add(label);
      helperFunctions.add(new Push(Collections.singletonList(allocator.get(ARMRegisterLabel.LR))));
      /* cmp the content in r0 with 0 */
      helperFunctions.add(new Cmp(allocator.get(0), new Operand2(new Immediate(0, BitNum.CONST8))));
      /* if not equal to 0 LDR true */
      helperFunctions.add(new LDR(allocator.get(0), new LabelAddressing(msgTrue), LdrMode.LDRNE));
      /* otherwise equal to 0 LDR false */
      helperFunctions.add(new LDR(allocator.get(0), new LabelAddressing(msgFalse), LdrMode.LDREQ));

      addCommonPrint(helperFunctions, allocator);
    }
  }

  private static void addCommonPrint(List<Instruction> helperFunctions, ARMConcreteRegisterAllocator allocator) {
    /* skip the first 4 byte of the msg which is the length of it */
    helperFunctions.add(new Add(allocator.get(0), allocator.get(0), new Operand2(new Immediate(4, BitNum.CONST8))));
    helperFunctions.add(new BL("printf"));
    /* refresh the r0 and buffer */
    helperFunctions.add(new Mov(allocator.get(0), new Operand2(new Immediate(0, BitNum.CONST8))));
    helperFunctions.add(new BL("fflush"));
    helperFunctions.add(new Pop(Collections.singletonList(allocator.get(ARMRegisterLabel.PC))));
  }

  public static Label addMsgAndReturn(String msgAscii) {
    /* add a Msg into the data list */
    Label msgLabel = labelGenerator.getLabel();
    dataSegmentMessages.put(msgLabel, msgAscii);
    return msgLabel;
  }
}
