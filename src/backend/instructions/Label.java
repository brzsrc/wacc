package backend.instructions;

public class Label extends Instruction {

  /* Counter for blocks L0, L1 ... Ln */
  private static int blockCounter = 0;
  /* Counter for messages msg_0, msg_1 ... msg_n */
  private static int msgCounter = 0;

  private String name;

  private Label(String name) {
    this.name = name;
  }

  private Label(boolean isBlock) {
    name = (isBlock)? "L" + (blockCounter++) : "msg_" + (msgCounter++);
  }

  /* Factory Constructors */
  public static Label getFuncLabel(String name) {
    return new Label(name);
  }

  public static Label getBlockLabel() {
    return new Label(true);
  }

  public static Label getMsgLabel() {
    return new Label(false);
  }

  @Override
  public String assemble() {
    return name + ":";
  }

}
