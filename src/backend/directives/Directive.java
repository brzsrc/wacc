package backend.directives;

import java.util.List;

public interface Directive {
    List<String> toStringList();
    int getIndentationLevel();
}
