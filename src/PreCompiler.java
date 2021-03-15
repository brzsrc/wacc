import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import utils.Utils;

public class PreCompiler {

  public static File preCompile(String fileName) throws IOException {
    String pathName = fileName.substring(0, fileName.length()-5);
    String mediateName = pathName + "_mid.wacc";
    String outputName = pathName + "_pre.wacc";

    File input = new File(fileName);
    File mediate = new File(mediateName);
    if (!mediate.exists()) {
      mediate.createNewFile();
    }


    /* Get the import info */
    BufferedReader br = new BufferedReader(new FileReader(input));
    BufferedWriter bw = new BufferedWriter(new FileWriter(mediate));

    List<IncludeInfo> imports = new ArrayList<>();
    List<MacroInfo> macros = new ArrayList<>();

    int lineCounter = 1;
    while (br.ready()) {
      String str = br.readLine();
      if (str.contains("define")) {
        String[] macro = str.split(" ");
        if (macro.length != 3) {
          mediate.delete();
          System.out.println("Invalid macro at line " + lineCounter);
          System.exit(Utils.INTERNAL_ERROR_CODE);
        }
        macros.add(new MacroInfo(macro[1], macro[2]));
      } else if (str.contains("include")) {
        
        String[] tokens = str.split("[<]", 2);
        String libName = tokens[0].split("[ ]")[1];
        /* include lib with type param */
        if (tokens.length > 1) {
          String[] generics = tokens[1].replace(" ", "").split("[,]");
          /* the last type param would have one '>' which should be removed*/
          int last = generics.length - 1;
          generics[last] = generics[last].substring(0, generics[last].length() - 1);
          imports.add(new IncludeInfo(libName, generics));

        /* lib without type param */
        } else {
          imports.add(new IncludeInfo(libName, null));
        }

      } else if (str.contains("import")) {
        bw.write(str + "\n");
      } else if (str.contains("begin")) {
        bw.write(str + "\n");
        break;
      }
      lineCounter++;
    }

    br.close();
    bw.close();

    /* if there is no stdlib including or marcos, just return the source file */
    if (imports.isEmpty() && macros.isEmpty()) {
      mediate.delete();
      return input;
    }

    /* Concat the content of the lib */
    imports.sort((importInfo, t1) -> t1.getMaxTypeLen() - importInfo.getMaxTypeLen());
    for (IncludeInfo i : imports) {
      String libName = i.getLibName();
      String[] generics = i.getGenerics();
      writeTo(new File("src/wacc_lib/" + libName + ".stdlib"), mediate, generics, macros);
    }

    /* Append the rest of the original part */
    br = new BufferedReader(new FileReader(input));
    bw = new BufferedWriter(new FileWriter(mediate, true));

    boolean isBegin = false;
    while (br.ready()) {
      String str = br.readLine();
      if (isBegin) {
        bw.write(str + "\n");
      } else if (str.contains("begin")) {
        isBegin = true;
      }
    }

    br.close();
    bw.close();

    /* if there is no macros, return the mediate file */
    if (macros.isEmpty()) {
      return mediate;
    }

    /* Macro replacement */
    File output = new File(outputName);
    if (!output.exists()) {
      output.createNewFile();
    }

    br = new BufferedReader(new FileReader(mediate));
    bw = new BufferedWriter(new FileWriter(output));
    while (br.ready()) {
      String str = br.readLine();
      if (!str.contains("import")) {
        for (MacroInfo m : macros) {
          str = str.replace(m.getKey(), m.getValue());
        }
      }
      bw.write(str + "\n");
    }

    br.close();
    bw.close();

    mediate.delete();
    return output;
  }

  private static String replaceGenerics(String str, String[] generics) {
    if (generics.length == 1) {
      return str.replace("E", generics[0]);
    }

    for (int i = 0; i < generics.length; i++) {
      str = str.replace("E" + (i+1), generics[i]);
    }

    return str;
  }

  private static void writeTo(
      File src, File dst, String[] generics, List<MacroInfo> macros) throws IOException {

    BufferedReader br = new BufferedReader(new FileReader(src));
    BufferedWriter bw = new BufferedWriter(new FileWriter(dst, true));


    while (br.ready()) {
      String str = br.readLine();
      if (generics != null) {
        str = replaceGenerics(str, generics);
      }

      if (str.contains("define")) {
        String[] macro = str.split(" ");
        /* could throw an error here */
        assert macro.length == 3;
        macros.add(new MacroInfo(macro[1], macro[2]));
      } else {
        bw.write("  " + str + "\n");
      }
    }

    br.close();
    bw.close();
  }

  private static class IncludeInfo {
    private final String libName;
    private final String[] generics;
    private int maxTypeLen;

    private IncludeInfo(String libName, String[] generics) {
      this.libName = libName;
      this.generics = generics;
      maxTypeLen = 0;
      if (generics != null) {
        for (String p : generics) {
          if (p.length() > maxTypeLen) {
            maxTypeLen = p.length();
          }
        }
      }
    }

    public String getLibName() {
      return libName;
    }

    public String[] getGenerics() {
      return generics;
    }

    public int getMaxTypeLen() {
      return maxTypeLen;
    }

    @Override
    public String toString() {
      return libName + ": " + Arrays.toString(generics);
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
