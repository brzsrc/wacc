package backend.intel.section;

public class IntelAsmHeader {

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
  public String toString() {
    return "[need to be implemented!]";
  }
}
