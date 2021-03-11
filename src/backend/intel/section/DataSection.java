package backend.intel.section;

import backend.common.Directive;
import backend.intel.instructions.Label;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import utils.backend.LabelGenerator;

public class DataSection implements Directive {

  private LabelGenerator<Label> labelGenerator;
  private Map<Label, String> data;

  public DataSection() {
    this.data = new LinkedHashMap<>();
    this.labelGenerator = new LabelGenerator<Label>(".LC", Label.class);
  }

  public void addData(String str) {
    Label label = labelGenerator.getLabel().asIntelLabel();
    data.put(label, str);
  }

  @Override
  public List<String> toStringList() {
    List<String> dataText = new ArrayList<>();
    for (Entry<Label, String> e : data.entrySet()) {
      dataText.add(e.getKey().assemble());
      dataText.add(e.getValue());
    }

    return dataText;
  }

  @Override
  public int getIndentationLevel() {
    return 0;
  }
}
