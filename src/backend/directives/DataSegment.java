package backend.directives;

import backend.instructions.Label;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


public class DataSegment implements Directive {
    private Map<Label, String> messages;
    
    public DataSegment(Map<Label, String> messages) {
       this. messages = messages;
    }

    @Override
    public List<String> toStringList() {
        List<String> list = new ArrayList<>();
        list.add("\t.data\n\n");
        for (Entry<Label, String> e : messages.entrySet()) {
            list.add("\t" + e.getKey().assemble());
            list.add("\t\t.word " + (e.getValue().length() + 1) + "\n\t\t.ascii " + e.getValue());
        }

        return list;
    }

    @Override
    public int getIndentationLevel() {
        return 1;
    }
}
