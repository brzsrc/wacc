package backend.directives;

import java.util.List;

public class TextSegment implements Directive {

    @Override
    public List<String> toStringList() {
        return List.of(".text\n\n");
    }
    
}
