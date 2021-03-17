package backend.common;

import java.util.List;

public interface Directive {

  List<String> toStringList();

  int getIndentationLevel();
}
