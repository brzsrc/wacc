package backend.directives;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import utils.backend.LabelGenerator;

class Message {
    private String ascii;

    public Message(String ascii) {
        this.ascii = ascii;
    }

    @Override
    public String toString() {
        return ".word " + ascii.length() + "\n\t\t.ascii " + ascii;
    }
}

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
            list.add("\t" + e.getKey().toString());
            list.add("\t\t.word " + e.getValue().length() + "\n\t\t.ascii " + e.getValue());
        }

        return list;
    }
}
