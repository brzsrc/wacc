package utils.backend;

import static utils.Utils.BOOL_BASIC_TYPE;
import static utils.Utils.CHAR_BASIC_TYPE;
import static utils.Utils.INT_BASIC_TYPE;
import static utils.Utils.STRING_BASIC_TYPE;

import backend.ARMInstructionGenerator.SpecialInstruction;
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
import frontend.type.ArrayType;
import frontend.type.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static utils.Utils.ARRAY_TYPE;
import static utils.Utils.PAIR_TYPE;

public class HelperFunction {

  /* print char would directly call BL putChar instead */
  private enum Helper {
    READ_INT, READ_CHAR, PRINT_INT, PUTCHAR, PRINT_BOOL, PRINT_STRING, PRINT_REFERENCE, PRINT_LN,
    CHECK_DIVIDE_BY_ZERO, THROW_RUNTIME_ERROR, CHECK_ARRAY_BOUND, FREE_ARRAY, FREE_PAIR, CHECK_NULL_POINTER,
    THROW_OVERFLOW_ERROR;
    /* ... continue with some other helpers like runtime_error checker ... */

    @Override
    public String toString() {
      return "p_" + name().toLowerCase();
    }
  }

  /* char array type would be the same as string for printf */
  private static Type CHAR_ARRAY_TYPE = new ArrayType(CHAR_BASIC_TYPE);

  /*
   * record which helpers already exist, we don't want repeated helper functions
   */
  private static Set<Helper> alreadyExist = new HashSet<>();

  /* map for addPrintSingle */
  private static Map<Helper, String> printSingleMap = new HashMap<>() {
    {
      put(Helper.PRINT_INT, "\"%d\\0\"");
      put(Helper.PUTCHAR, "\"%c\\0\"");
      put(Helper.PRINT_REFERENCE, "\"%p\\0\"");
    }
  };

  private static LabelGenerator labelGenerator = new LabelGenerator("msg_");

  public static void addRead(Type type, List<Instruction> instructions, Map<Label, String> data,
      List<Instruction> helperFunctions, ARMConcreteRegisterAllocator allocator) {

    /* arg of read must be either int or char */
    assert type.equalToType(INT_BASIC_TYPE) || type.equalToType(CHAR_BASIC_TYPE);

    /* distinguish read_int from read_char */
    Helper helper = (type.equalToType(INT_BASIC_TYPE)) ? Helper.READ_INT : Helper.READ_CHAR;
    /* call the helper function anyway */
    instructions.add(new BL(helper.toString()));

    /* only add the helper if it doesn't exist yet */
    if (!alreadyExist.contains(helper)) {

      /* add this helper into alreadyExist list */
      alreadyExist.add(helper);

      /* add the format into the data list */
      Label msg = addMsg((helper == Helper.READ_INT) ? "\"%d\\0\"" : "\"%c\\0\"", data);

      /* add the helper function label */
      Label label = new Label(helper.toString());
      helperFunctions.add(label);
      helperFunctions.add(new Push(Collections.singletonList(allocator.get(ARMRegisterLabel.LR))));
      /* fst arg of read is the snd arg of scanf (storing address) */
      helperFunctions.add(new Mov(allocator.get(1), new Operand2(allocator.get(0))));
      /* fst arg of scanf is the format */
      helperFunctions.add(new LDR(allocator.get(0), new LabelAddressing(msg)));
      /* skip the first 4 byte of the msg which is the length of it */
      helperFunctions.add(new Add(allocator.get(0), allocator.get(0), new Operand2(new Immediate(4, BitNum.CONST8))));
      helperFunctions.add(new BL("scanf"));
      helperFunctions.add(new Pop(Collections.singletonList(allocator.get(ARMRegisterLabel.PC))));
    }
  }

  public static void addPrint(Type type, List<Instruction> instructions, Map<Label, String> data,
      List<Instruction> helperFunctions, ARMConcreteRegisterAllocator allocator) {

    /* TODO: need to refactor here */
    if (type.equalToType(INT_BASIC_TYPE)) {
      instructions.add(new BL(Helper.PRINT_INT.toString()));
      addPrintSingle(Helper.PRINT_INT, data, helperFunctions, allocator);
    } else if (type.equalToType(CHAR_BASIC_TYPE)) {
      instructions.add(new BL(Helper.PUTCHAR.toString()));
      addPrintSingle(Helper.PUTCHAR, data, helperFunctions, allocator);
    } else if (type.equalToType(BOOL_BASIC_TYPE)) {
      instructions.add(new BL(Helper.PRINT_BOOL.toString()));
      addPrintBool(data, helperFunctions, allocator);
    } else if (type.equalToType(STRING_BASIC_TYPE) || type.equalToType(CHAR_ARRAY_TYPE)) {
      instructions.add(new BL(Helper.PRINT_STRING.toString()));
      addPrintMultiple(data, helperFunctions, allocator);
    } else {
      instructions.add(new BL(Helper.PRINT_REFERENCE.toString()));
      addPrintSingle(Helper.PRINT_REFERENCE, data, helperFunctions, allocator);
    }

  }

  public static void addPrintln(List<Instruction> instructions, Map<Label, String> data,
      List<Instruction> helperFunctions, ARMConcreteRegisterAllocator allocator) {

    Helper helper = Helper.PRINT_LN;
    /* call the helper function anyway */
    instructions.add(new BL(helper.toString()));

    /* only add the helper if it doesn't exist yet */
    if (!alreadyExist.contains(helper)) {

      /* add this helper into alreadyExist list */
      alreadyExist.add(helper);

      /* add the format into the data list */
      Label msg = addMsg("\"\\0\"", data);

      /* add the helper function label */
      Label label = new Label(helper.toString());
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
      Helper helper = (type.equalToType(ARRAY_TYPE))? Helper.FREE_ARRAY : Helper.FREE_PAIR;

      /* only add the helper if it doesn't exist yet */
      if (!alreadyExist.contains(helper)) {

        /* add this helper into alreadyExist list */
        alreadyExist.add(helper);

        Label msg = null;
        for(Label msg_ : data.keySet()) {
          if(data.get(msg_).equals("\"NullReferenceError: dereference a null reference\\n\\0\"")) {
            msg = msg_;
            break;
          }
        }
        if(msg == null)
          msg = addMsg("\"NullReferenceError: dereference a null reference\\n\\0\"", data);

        /* add the helper function label */
        Label label = new Label(helper.toString());
        helperFunctions.add(label);
        helperFunctions.add(new Push(Collections.singletonList(allocator.get(ARMRegisterLabel.LR))));
        helperFunctions.add(new Cmp(allocator.get(0), new Operand2(new Immediate(0, BitNum.CONST8))));
        helperFunctions.add(new LDR(allocator.get(0), new LabelAddressing(msg), LdrMode.LDRNE));
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
    Helper helper = Helper.CHECK_NULL_POINTER;

    if (alreadyExist.contains(helper)) {
      return;
    }
    
    /* add this helper into alreadyExist list */
    alreadyExist.add(helper);

    /* add the error message into the data list */
    
    Label msg = addMsg("\"NullReferenceError: dereference a null reference\\n\\0\"", data);

    /* add the helper function label */
    Label label = new Label(helper.toString());
    helperFunctions.add(label);
    helperFunctions.add(new Push(Collections.singletonList(allocator.get(14))));
    helperFunctions.add(new Cmp(allocator.get(0), new Operand2(new Immediate(0, BitNum.CONST8))));
    helperFunctions.add(new LDR(allocator.get(0), new LabelAddressing(msg), LdrMode.LDREQ));
    helperFunctions.add(new BL(Cond.EQ, Helper.THROW_RUNTIME_ERROR.toString()));
    helperFunctions.add(new Pop(Collections.singletonList(allocator.get(15))));
    addThrowRuntimeError(data, helperFunctions, allocator);

  }

  public static void addCheckDivByZero(Map<Label, String> data, List<Instruction> helperFunctions,
      ARMConcreteRegisterAllocator allocator) {
    Helper helper = Helper.CHECK_DIVIDE_BY_ZERO;
    /*
     * add this instr outside this func cuz only DIV and MOD will need to call this
     * func
     */

    /* only add the helper if it doesn't exist yet */
    if (!alreadyExist.contains(helper)) {

      /* add this helper into alreadyExist list */
      alreadyExist.add(helper);

      /* add the error message into the data list */
      Label msg = addMsg("\"DivideByZeroError: divide or modulo by zero\\n\\0\"", data);

      /* add the helper function label */
      Label label = new Label(helper.toString());
      helperFunctions.add(label);
      helperFunctions.add(new Push(Collections.singletonList(allocator.get(14))));
      helperFunctions.add(new Cmp(allocator.get(1), new Operand2(new Immediate(0, BitNum.CONST8))));
      helperFunctions.add(new LDR(allocator.get(0), new LabelAddressing(msg), LdrMode.LDREQ));
      helperFunctions.add(new BL(Cond.EQ, Helper.THROW_RUNTIME_ERROR.toString()));
      helperFunctions.add(new Pop(Collections.singletonList(allocator.get(15))));
      addThrowRuntimeError(data, helperFunctions, allocator);
    }
  }

  public static void addCheckArrayBound(Map<Label, String> data, List<Instruction> helperFunctions,
      ARMConcreteRegisterAllocator allocator) {
    Helper helper = Helper.CHECK_ARRAY_BOUND;

    if (!alreadyExist.contains(helper)) {
      /* add this helper into alreadyExist list */
      alreadyExist.add(helper);

      Label negativeIndexLabel = addMsg("\"ArrayIndexOutOfBoundsError: negative index\\n\\0\"", data);
      Label indexOutOfBoundLabel = addMsg("\"ArrayIndexOutOfBoundsError: index too large\\n\\0\"", data);

      helperFunctions.add(new Label("p_check_array_bounds"));
      helperFunctions.add(new Push(Collections.singletonList(allocator.get(14))));
      helperFunctions.add(new Cmp(allocator.get(0), new Operand2(new Immediate(0, BitNum.CONST8))));
      helperFunctions.add(new LDR(allocator.get(0), new LabelAddressing(indexOutOfBoundLabel), LdrMode.LDRLT));
      helperFunctions.add(new BL(Cond.LT, "p_throw_runtime_error"));
      helperFunctions.add(new LDR(allocator.get(1), new AddressingMode2(AddrMode2.OFFSET, allocator.get(1))));
      helperFunctions.add(new Cmp(allocator.get(0), new Operand2(allocator.get(1))));
      helperFunctions.add(new LDR(allocator.get(0), new LabelAddressing(negativeIndexLabel), LdrMode.LDRCS));
      helperFunctions.add(new BL(Cond.CS, "p_throw_runtime_error"));
      helperFunctions.add(new Pop(Collections.singletonList(allocator.get(15))));
    }
  }

  public static void addThrowOverflowError(Map<Label, String> data, List<Instruction> helperFunctions,
      ARMConcreteRegisterAllocator allocator) {
    Helper helper = Helper.CHECK_ARRAY_BOUND;

    Label msg = addMsg("\"OverflowError: the result is too small/large to store in a 4-byte signed-integer.\n\"", data);

    if (!alreadyExist.contains(helper)) {
      /* add this helper into alreadyExist list */
      alreadyExist.add(helper);
      helperFunctions.add(new LDR(allocator.get(0), new LabelAddressing(msg), LdrMode.LDR));
      helperFunctions.add(new BL("p_throw_runtime_error"));
    }
  }

  public static void addThrowRuntimeError(Map<Label, String> data, List<Instruction> helperFunctions,
      ARMConcreteRegisterAllocator allocator) {
    Helper helper = Helper.THROW_RUNTIME_ERROR;

    /* only add the helper if it doesn't exist yet */
    if (!alreadyExist.contains(helper)) {

      /* add this helper into alreadyExist list */
      alreadyExist.add(helper);

      /* add the helper function label */
      Label label = new Label(helper.toString());
      helperFunctions.add(label);
      helperFunctions.add(new BL(Helper.PRINT_STRING.toString()));
      helperFunctions.add(new Mov(allocator.get(0), new Operand2(new Immediate(-1, BitNum.CONST8))));
      helperFunctions.add(new BL("exit"));
      addPrintMultiple(data, helperFunctions, allocator);
    }
  }

  /* print string (char array included) */
  private static void addPrintMultiple(Map<Label, String> data, List<Instruction> helperFunctions,
      ARMConcreteRegisterAllocator allocator) {

    Helper helper = Helper.PRINT_STRING;

    /* only add the helper if it doesn't exist yet */
    if (!alreadyExist.contains(helper)) {

      /* add this helper into alreadyExist list */
      alreadyExist.add(helper);

      /* add the format into the data list */
      Label msg = addMsg("\"%.*s\\0\"", data);

      /* add the helper function label */
      Label label = new Label(helper.toString());
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
  private static void addPrintSingle(Helper helper, Map<Label, String> data, List<Instruction> helperFunctions,
      ARMConcreteRegisterAllocator allocator) {

    /* only add the helper if it doesn't exist yet */
    if (!alreadyExist.contains(helper)) {

      /* add this helper into alreadyExist list */
      alreadyExist.add(helper);

      /* add the format into the data list */
      Label msg = addMsg(printSingleMap.get(helper), data);

      /* add the helper function label */
      Label label = new Label(helper.toString());
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

    Helper helper = Helper.PRINT_BOOL;

    /* only add the helper if it doesn't exist yet */
    if (!alreadyExist.contains(helper)) {

      /* add this helper into alreadyExist list */
      alreadyExist.add(helper);

      /* add the msgTrue into the data list */
      Label msgTrue = addMsg("\"true\\0\"", data);
      /* add the msgFalse into the data list */
      Label msgFalse = addMsg("\"false\\0\"", data);

      /* add the helper function label */
      Label label = new Label(helper.toString());
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

  public static Label addMsg(String msgAscii, Map<Label, String> data) {
    /* add a Msg into the data list */
    Label msgLabel = labelGenerator.getLabel();
    data.put(msgLabel, msgAscii);
    return msgLabel;
  }

}
