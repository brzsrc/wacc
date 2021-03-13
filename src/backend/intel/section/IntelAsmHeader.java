package backend.intel.section;

import java.util.List;

import backend.common.Directive;
import java.util.stream.Collectors;

public class IntelAsmHeader implements Directive {

  /**
   * the information below is optional
   * they are mainly used for identification purposes
   */
  private String buildVersion;
  private String fileName;
  private String globlType;
  private String size;

  /**
   * the information below is needed
   */

  private String globl;

  public IntelAsmHeader(String buildVersion, String fileName, String globlType, String size, String globl) {
    this.buildVersion = buildVersion;
    this.fileName = fileName;
    this.globlType = globlType;
    this.size = size;
    this.globl = globl;
  }

  public IntelAsmHeader(String globl) {
    this.globl = globl;
  }

  @Override
  public List<String> toStringList() {
    List<String> str = List.of(".globl" + "\t" + globl);
    return str;
  }

  @Override
  public int getIndentationLevel() {
    return 0;
  }
}
