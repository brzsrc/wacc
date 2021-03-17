import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static utils.Utils.*;

public class PreCompiler {

  private final List<IncludeInfo> imports = new ArrayList<>();
  private final List<MacroInfo> macros = new ArrayList<>();

  private final File sourceFile;
  private final String pathName;

  public PreCompiler(File sourceFile) {
    this.sourceFile = sourceFile;
    String sourceFilePath = sourceFile.getPath();
    pathName = sourceFilePath.substring(0, sourceFilePath.length() - waccFormatName.length());
  }

  public File preCompile() throws IOException {

    File mediateFile = new File(pathName + mediateFileSuffix + waccFormatName);
    if (!mediateFile.exists()) {
      mediateFile.createNewFile();
    }

    /* Visit the contents before entering the program body (begin) */
    visitFileHeader(sourceFile, mediateFile);

    /* If there is no stdlib including or marcos, just return the source file */
    if (imports.isEmpty() && macros.isEmpty()) {
      mediateFile.delete();
      return sourceFile;
    }

    /* Concat the content of all libs */
    concatAllLibs(mediateFile);

    /* Append the contents of the program body */
    visitFileBody(sourceFile, mediateFile);

    /* If there is no macros, just return the mediateFile file */
    if (macros.isEmpty()) {
      return mediateFile;
    }

    File outputFile = new File(pathName + outputFileSuffix + waccFormatName);
    if (!outputFile.exists()) {
      outputFile.createNewFile();
    }

    /* Macro replacement */
    replaceMacros(mediateFile, outputFile);

    mediateFile.delete();
    return outputFile;
  }


  private void visitFileHeader(File sourceFile, File mediateFile) throws IOException {
    BufferedReader br = new BufferedReader(new FileReader(sourceFile));
    BufferedWriter bw = new BufferedWriter(new FileWriter(mediateFile));

    int lineCounter = 1;
    while (br.ready()) {
      String str = br.readLine();
      str = str.split(commentMark, 2)[0];
      if (str.contains(defineRuleContext)) {
        /* process and store the macros info */
        String[] macro = str.split(" ", 3);
        if (macro.length != 3 || macro[0].length() != defineRuleContext.length() - 1) {
          mediateFile.delete();
          System.out.println("Invalid macro at line " + lineCounter);
          System.exit(SYNTAX_ERROR_CODE);
        }
        macros.add(new MacroInfo(macro[1], macro[2]));
      } else if (str.contains(includeRuleContext)) {
        /* process and store the stdlib including info */
        String[] tokens = str.split("<", 2);
        /* e.g. include lib<a, b, c>, tokens[0] = include lib, tokens[1] = a, b, c> */
        String[] prefix = tokens[0].split(" ", 2);
        String libName = prefix[1];
        List<String> typeParams = (tokens.length == 1) ? new ArrayList<>() : getTypeParam(tokens[1],
            lineCounter, prefix[0].length() == includeRuleContext.length() - 1, mediateFile);
        imports.add(new IncludeInfo(libName, typeParams, lineCounter));
      } else if (str.contains(programBodyMark)) {
        bw.write(str + "\n");
        break; /* reach the end of file header */
      } else {
        bw.write(str + "\n"); /* copy the content */
      }

      lineCounter++;
    }

    br.close();
    bw.close();
  }

  private void visitFileBody(File sourceFile, File mediateFile) throws IOException {
    BufferedReader br = new BufferedReader(new FileReader(sourceFile));
    BufferedWriter bw = new BufferedWriter(new FileWriter(mediateFile, true));

    boolean isInProgramBody = false;
    /* only concat the contents of program body */
    while (br.ready()) {
      String str = br.readLine();
      str = str.split(commentMark, 2)[0];
      if (isInProgramBody) {
        bw.write(str + "\n");
      } else if (str.contains(programBodyMark)) {
        isInProgramBody = true;
      }
    }

    br.close();
    bw.close();
  }

  private void concatAllLibs(File mediateFile) throws IOException {
    imports.sort((importInfo, t1) -> t1.getMaxTypeLen() - importInfo.getMaxTypeLen());
    for (IncludeInfo i : imports) {
      List<String> typeParams = i.getTypeParams();
      concatOneLib(new File(stdlibPath + i.getLibName() + stdlibFormatName),
          mediateFile, typeParams, i.getLineNum());
    }
  }

  /* helper function which is used to concat the contents of one lib to the dst file */
  private void concatOneLib(File lib, File dst, List<String> typeParams, int lineNum) throws IOException {
    BufferedReader br = null;
    try {
      br = new BufferedReader(new FileReader(lib));
    } catch (FileNotFoundException e) {
      dst.delete();
      System.out.println("Library: " + lib.getPath() + " at line " + lineNum + " not found");
      System.exit(SEMANTIC_ERROR_CODE);
    }
    BufferedWriter bw = new BufferedWriter(new FileWriter(dst, true));

    while (br.ready()) {
      String str = br.readLine();
      str = str.split(commentMark, 2)[0];
      /* replace all the generics with the given typeParams */
      if (typeParams.size() == 1) {
        str = str.replace(genericMark, typeParams.get(0));
      } else if (typeParams.size() > 1) {
        for (int i = 0; i < typeParams.size(); i++) {
          str = str.replace(genericMark + (i+1), typeParams.get(i));
        }
      }

      if (str.contains(defineRuleContext)) {
        /* get the lib's macro info, assume macros in stdlib are all valid */
        String[] macro = str.split(" ", 3);
        macros.add(new MacroInfo(macro[1], macro[2]));
      } else {
        bw.write("  " + str + "\n"); /* copy the content */
      }
    }

    br.close();
    bw.close();
  }

  private void replaceMacros(File mediateFile, File outputFile) throws IOException {
    BufferedReader br = new BufferedReader(new FileReader(mediateFile));
    BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile));

    boolean isInProgramBody = false;
    /* only do text replacement in the program body */
    while (br.ready()) {
      String str = br.readLine();
      str = str.split(commentMark, 2)[0];
      if (isInProgramBody) {
        for (MacroInfo m : macros) {
          str = str.replace(m.getKey(), m.getValue());
        }
      } else if (str.contains(programBodyMark)) {
        isInProgramBody = true;
      }
      bw.write(str + "\n");
    }

    br.close();
    bw.close();
  }

  /* helper function which is used to get the corresponding type params */
  private List<String> getTypeParam(String str, int lineNum, boolean matchInclude, File mediateFile) {
    List<String> typeParams = new ArrayList<>();
    int depth = 1;
    int i;

    StringBuilder token = new StringBuilder();
    for (i = 0; i < str.length(); i++) {
      char c = str.charAt(i);
      if (c == '<') depth++;
      if (c == '>') depth--;

      if (depth == 0) {
        typeParams.add(token.toString());
        break;
      } else  if (depth == 1 && c == ',') {
        typeParams.add(token.toString());
        token = new StringBuilder();
      } else if (c != ' ') {
        token.append(c);
      }
    }

    boolean hasJunkAtEnd = false;
    for (i = i + 1; i < str.length(); i++) {
      if (str.charAt(i) != ' ') {
        hasJunkAtEnd = true;
        break;
      }
    }

    if (hasJunkAtEnd || !matchInclude || depth != 0) {
      mediateFile.delete();
      System.out.println("Invalid include statement at line " + lineNum);
      System.exit(SYNTAX_ERROR_CODE);
    }

    return typeParams;
  }

  private static class IncludeInfo {
    private final String libName;
    private final List<String> typeParams;
    private final int lineNum;
    private int maxTypeLen;

    private IncludeInfo(String libName, List<String> typeParams, int lineNum) {
      this.libName = libName;
      this.typeParams = typeParams;
      this.lineNum = lineNum;
      maxTypeLen = 0;
      for (String p : typeParams) {
        if (p.length() > maxTypeLen) {
          maxTypeLen = p.length();
        }
      }
    }

    public String getLibName() {
      return libName;
    }

    public List<String> getTypeParams() {
      return typeParams;
    }

    public int getLineNum() {
      return lineNum;
    }

    public int getMaxTypeLen() {
      return maxTypeLen;
    }
  }

  private static class MacroInfo {
    private final String key;
    private final String value;

    private MacroInfo(String key, String value) {
      this.key = key;
      this.value = value;
    }

    public String getKey() {
      return key;
    }

    public String getValue() {
      return value;
    }
  }

}
