package utils.backend;

import static utils.Utils.BOOL_BASIC_TYPE;
import static utils.Utils.CHAR_BASIC_TYPE;
import static utils.Utils.INT_BASIC_TYPE;
import static utils.Utils.STRING_BASIC_TYPE;

import backend.instructions.BL;
import backend.instructions.Cmp;
import backend.instructions.Instruction;
import backend.instructions.LDR;
import backend.instructions.Label;
import backend.instructions.Mov;
import backend.instructions.LDR.LdrMode;
import backend.instructions.addressing.LabelAddressing;
import backend.instructions.addressing.RegAddressing;
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

public class HelperFunction {

  /* print char would directly call BL putChar instead */
  public enum Helper {
    READ_INT, READ_CHAR, PRINT_INT, PRINT_CHAR, PRINT_BOOL, PRINT_STRING, PRINT_REFERENCE, PRINT_LN;
    /* ... continue with some other helpers like runtime_error checker ... */

    @Override
    public String toString() {
      return "p_" + name().toLowerCase();
    }
  }

  /* char array type would be the same as string for printf */
  private static Type CHAR_ARRAY_TYPE = new ArrayType(CHAR_BASIC_TYPE);

  /* record which helpers already exist, we don't want repeated helper functions */
  private static Set<Helper> alreadyExist = new HashSet<>();

  /* map for addPrintSingle */
  private static Map<Helper, String> printSingleMap = new HashMap<>(){{
    put(Helper.PRINT_INT, "%d");
    put(Helper.PRINT_CHAR, "%c");
    put(Helper.PRINT_REFERENCE, "%p");
  }};

  private static LabelGenerator labelGenerator = new LabelGenerator("msg_");

  public static void addRead(Type type, List<Instruction> instructions, List<String> data,
      List<Instruction> helperFunctions, ARMConcreteRegisterAllocator allocator) {

    /* arg of read must be either int or char */
    assert type.equalToType(INT_BASIC_TYPE) || type.equalToType(CHAR_BASIC_TYPE);

    /* distinguish read_int from read_char */
    Helper helper = (type.equalToType(INT_BASIC_TYPE))? Helper.READ_INT : Helper.READ_CHAR;
    /* call the helper function anyway */
    instructions.add(new BL(helper.toString()));

    /* only add the helper if it doesn't exist yet */
    if (!alreadyExist.contains(helper)) {

      /* add this helper into alreadyExist list */
      alreadyExist.add(helper);
      
      /* add the format into the data list */
      Label msg = addMsg((helper == Helper.READ_INT)? "%d" : "%c", data);

      /* add the helper function label */
      Label label = new Label(helper.toString());
      helperFunctions.add(label);
      helperFunctions.add(new Push(Collections.singletonList(allocator.get(ARMRegisterLabel.SLR))));
      /* fst arg of read is the snd arg of scanf (storing address)*/
      helperFunctions.add(new Mov(allocator.get(1), new Operand2(allocator.get(0))));
      /* fst arg of scanf is the format */
      helperFunctions.add(new LDR(allocator.get(0), new LabelAddressing(msg)));
      /* skip the first 4 byte of the msg which is the length of it */
      helperFunctions.add(new Add(allocator.get(0), allocator.get(0), new Operand2(new Immediate(4, BitNum.CONST8))));
      helperFunctions.add(new BL("scanf"));
      helperFunctions.add(new Pop(Collections.singletonList(allocator.get(ARMRegisterLabel.PC))));
    }
  }

  public static void addPrint(Type type, List<Instruction> instructions, List<String> data,
      List<Instruction> helperFunctions, ARMConcreteRegisterAllocator allocator) {

    if (type.equalToType(INT_BASIC_TYPE)) {
      addPrintSingle(Helper.PRINT_INT, instructions, data, helperFunctions, allocator);
    } else if (type.equalToType(CHAR_BASIC_TYPE)) {
      addPrintSingle(Helper.PRINT_CHAR, instructions, data, helperFunctions, allocator);
    } else if (type.equalToType(BOOL_BASIC_TYPE)) {
      addPrintBool(instructions, data, helperFunctions, allocator);
    } else if (type.equalToType(STRING_BASIC_TYPE) || type.equalToType(CHAR_ARRAY_TYPE)) {
      addPrintMultiple(instructions, data, helperFunctions, allocator);
    } else {
      addPrintSingle(Helper.PRINT_REFERENCE, instructions, data, helperFunctions, allocator);
    }

  }

  public static void addPrintln(List<Instruction> instructions, List<String> data,
      List<Instruction> helperFunctions, ARMConcreteRegisterAllocator allocator) {

    Helper helper = Helper.PRINT_LN;
    /* call the helper function anyway */
    instructions.add(new BL(helper.toString()));

    /* only add the helper if it doesn't exist yet */
    if (!alreadyExist.contains(helper)) {

      /* add this helper into alreadyExist list */
      alreadyExist.add(helper);

      /* add the format into the data list */
      Label msg = addMsg("", data);

      /* add the helper function label */
      Label label = new Label(helper.toString());
      helperFunctions.add(label);
      helperFunctions.add(new Push(Collections.singletonList(allocator.get(ARMRegisterLabel.SLR))));
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

  /* print int, print char or print reference */
  private static void addPrintSingle(Helper helper, List<Instruction> instructions, List<String> data,
      List<Instruction> helperFunctions, ARMConcreteRegisterAllocator allocator) {

    /* call the helper function anyway */
    instructions.add(new BL(helper.toString()));

    /* only add the helper if it doesn't exist yet */
    if (!alreadyExist.contains(helper)) {

      /* add this helper into alreadyExist list */
      alreadyExist.add(helper);

      /* add the format into the data list */
      Label msg = addMsg(printSingleMap.get(helper), data);

      /* add the helper function label */
      Label label = new Label(helper.toString());
      helperFunctions.add(label);
      helperFunctions.add(new Push(Collections.singletonList(allocator.get(ARMRegisterLabel.SLR))));
      /* put the content in r0 int o r1 as the snd arg of printf */
      helperFunctions.add(new Mov(allocator.get(1), new Operand2(allocator.get(0))));
      /* fst arg of printf is the format */
      helperFunctions.add(new LDR(allocator.get(0), new LabelAddressing(msg)));

      addCommonPrint(helperFunctions, allocator);
    }
  }

  /* print bool */
  private static void addPrintBool(List<Instruction> instructions, List<String> data,
      List<Instruction> helperFunctions, ARMConcreteRegisterAllocator allocator) {

    Helper helper = Helper.PRINT_BOOL;
    /* call the helper function anyway */
    instructions.add(new BL(helper.toString()));

    /* only add the helper if it doesn't exist yet */
    if (!alreadyExist.contains(helper)) {

      /* add this helper into alreadyExist list */
      alreadyExist.add(helper);

      /* add the msgTrue into the data list */
      Label msgTrue = addMsg("true", data);
      /* add the msgFalse into the data list */
      Label msgFalse = addMsg("false", data);

      /* add the helper function label */
      Label label = new Label(helper.toString());
      helperFunctions.add(label);
      helperFunctions.add(new Push(Collections.singletonList(allocator.get(ARMRegisterLabel.SLR))));
      /* cmp the content in r0 with 0*/
      helperFunctions.add(new Cmp(allocator.get(0), new Operand2(new Immediate(0, BitNum.CONST8))));
      /* if not equal to 0 LDR true */
      helperFunctions.add(new LDR(allocator.get(0), new LabelAddressing(msgTrue), LdrMode.LDRNE));
      /* otherwise equal to 0 LDR false */
      helperFunctions.add(new LDR(allocator.get(0), new LabelAddressing(msgFalse), LdrMode.LDREQ));

      addCommonPrint(helperFunctions, allocator);
    }
  }

  /* print string (char array included) */
  private static void addPrintMultiple(List<Instruction> instructions, List<String> data,
      List<Instruction> helperFunctions, ARMConcreteRegisterAllocator allocator) {

    Helper helper = Helper.PRINT_STRING;
    /* call the helper function anyway */
    instructions.add(new BL(helper.toString()));

    /* only add the helper if it doesn't exist yet */
    if (!alreadyExist.contains(helper)) {

      /* add this helper into alreadyExist list */
      alreadyExist.add(helper);

      /* add the format into the data list */
      Label msg = addMsg("%.*s", data);

      /* add the helper function label */
      Label label = new Label(helper.toString());
      helperFunctions.add(label);
      helperFunctions.add(new Push(Collections.singletonList(allocator.get(ARMRegisterLabel.SLR))));
      /* put the string length into r1 as snd arg */
      helperFunctions.add(new LDR(allocator.get(1), new RegAddressing(allocator.get(0))));
      /* skip the fst 4 bytes which is the length of the string */
      helperFunctions.add(new Add(allocator.get(2), allocator.get(0), new Operand2(new Immediate(4, BitNum.CONST8))));
      helperFunctions.add(new LDR(allocator.get(0), new LabelAddressing(msg)));

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

  private static Label addMsg(String msgAscii, List<String> data) {
    /* add a Msg into the data list */
    Label msg = labelGenerator.getLabel();
    data.add(msgAscii);

    return msg;
  }

}
