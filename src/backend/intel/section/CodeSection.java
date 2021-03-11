package backend.intel.section;

import backend.common.Directive;
import java.util.List;

public class CodeSection implements Directive {

  @Override
  public List<String> toStringList() {
    return null;
  }

  @Override
  public int getIndentationLevel() {
    return 0;
  }
}
