package backend.directives;

import java.util.List;

public interface Directive {
    public List<String> toStringList();
    public int getIndentationLevel();
}
