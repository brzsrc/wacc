package backend.arm.segment;

import backend.common.Directive;
import java.util.List;

public class TextSegment implements Directive {

  @Override
  public List<String> toStringList() {
    return List.of("\t.text\n\n");
  }

  @Override
  public int getIndentationLevel() {
    return 1;
  }

}
