package backend.directives;

import backend.instructions.Label;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import utils.backend.LabelGenerator;

public class DataSegment implements Directive {
    Map<Label, String> messages;
    LabelGenerator labelGenerator;
    
    public DataSegment() {
        messages = new HashMap<>();
        labelGenerator = new LabelGenerator("msg_");
    }

    public DataSegment(List<String> messages) {
        this();
        messages.forEach(i -> addData(i));
    }

    public void addData(String str) {
        Label label = labelGenerator.getLabel();
        messages.put(label, str);
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
