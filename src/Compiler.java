import backend.InstructionGenerator;
import backend.InstructionPrinter;
import backend.arm.ARMInstructionGenerator;
import backend.arm.ARMInstructionPrinter;
import backend.arm.segment.CodeSegment;
import backend.arm.segment.DataSegment;
import backend.arm.segment.TextSegment;
import backend.intel.IntelInstructionGenerator;
import backend.intel.IntelInstructionPrinter;
import backend.intel.section.CodeSection;
import backend.intel.section.DataSection;
import backend.intel.section.IntelAsmHeader;
import frontend.ASTPrinter;
import frontend.SemanticChecker;
import frontend.antlr.WACCLexer;
import frontend.antlr.WACCParser;
import frontend.antlr.WACCParser.ProgramContext;
import frontend.node.Node;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import optimize.ConstantPropagation;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import utils.NodeVisitor;
import utils.Utils.AssemblyArchitecture;
import utils.frontend.ParserErrorHandler;

public class Compiler {

  private static Object OptimizationLevel;

  public static void main(String[] args) throws IOException {
    // Processing command line input
    if (args.length < 1) {
      System.out.println("No file/path has been supplied! Please specifiy a wacc file to compile!");
      return;
    }

    List<String> cmd_ops = new ArrayList<>();
    Collections.addAll(cmd_ops, Arrays.copyOf(args, args.length));

    // Creating the file instance for the .wacc file
    File sourceFile = new File(args[0]);
    File file = new PreCompiler(sourceFile).preCompile();

    // System.out.println(file.getName());
    // try-with-resources so that fis can be closed properly even when error occurs
    try (FileInputStream fis = new FileInputStream(file)) {
      // Input stream of the file
      CharStream input = CharStreams.fromStream(fis);
      // Pass the input stream of the file to WACC lexer
      WACCLexer lexer = new WACCLexer(input);
      // delete the pre-compiled file
      if (!file.getName().equals(sourceFile.getName())) {
        file.delete();
      }
      // Obtain the internal tokens from the lexer
      CommonTokenStream tokens = new CommonTokenStream(lexer);
      // Parse the tokens into a syntax tree
      WACCParser parser = new WACCParser(tokens);
      parser.setErrorHandler(new ParserErrorHandler());
      // Start parsing using the `program` rule defined in antlr_config/WACCParser.g4
      ProgramContext tree = parser.program();

      Node program;
      // If the `--parse_only` flag is specified, then we do not run semantic analysis
      if (!cmd_ops.contains("--parse_only")) {
        AssemblyArchitecture arch = AssemblyArchitecture.ARMv6;
        if (cmd_ops.contains("--intel")) {
          arch = AssemblyArchitecture.Intelx86;
        }
        SemanticChecker semanticChecker = new SemanticChecker(new HashSet<>(), arch);
        semanticChecker.setPath(file.getParent() + "/");
        program = semanticChecker.visitProgram(tree);

        int optimise_cmd_index = cmd_ops.indexOf("--optimise");

        if (optimise_cmd_index != 0) {
          String optimise_level = cmd_ops.get(optimise_cmd_index + 1);
          switch (optimise_level) {
            case "0":
              break;
            case "1":
              System.out.println("optimising using const propagate");
              NodeVisitor<Node> constPropOptimiser = new ConstantPropagation(arch);
              program = constPropOptimiser.visit(program);
              break;
            default:
              System.out.println("unsupported optimisation level: " + optimise_level);
          }

          switch (optimise_level) {
            case "0":
              break;
            case "1":
              NodeVisitor<Node> constPropOptimiser = new ConstantPropagation(arch);
              program = constPropOptimiser.visit(program);
              break;
            default:
              System.out.println("unsupported optimisation level: " + optimise_level);
          }

          /* print optimised ast tree */
          if (cmd_ops.contains("--print_ast")) {
            ASTPrinter painter = new ASTPrinter();
            painter.visit(program);
          }

          if (cmd_ops.contains("--assembly")) {
            InstructionGenerator generator = null;
            InstructionPrinter printer = null;
            if (cmd_ops.contains("--intel")) {
              generator = new IntelInstructionGenerator();
              generator.visit(program);
              IntelAsmHeader header = new IntelAsmHeader("main");
              DataSection data = new DataSection(
                  ((IntelInstructionGenerator) generator).getDataSection());
              CodeSection code = new CodeSection(
                  ((IntelInstructionGenerator) generator).getInstructions());
              printer = new IntelInstructionPrinter(header, data, code);
            } else {
              generator = new ARMInstructionGenerator();
              generator.visit(program);
              DataSegment data = new DataSegment(
                  ((ARMInstructionGenerator) generator).getDataSegmentMessages());
              TextSegment text = new TextSegment();
              CodeSegment code = new CodeSegment(
                  ((ARMInstructionGenerator) generator).getInstructions());
              printer = new ARMInstructionPrinter(data, text, code,
                  ARMInstructionPrinter.OptimizationLevel.NONE);
            }

            File asmFile = new File(sourceFile.getName().replaceFirst("[.][^.]+$", "") + ".s");

            System.out.println("Assembly file created!");
            try (FileWriter asmWriter = new FileWriter(asmFile)) {
              asmWriter.write(printer.translate());
              asmWriter.close();
              System.out.println("Assembly has been written to the file!");
            }
          } else {
            System.out.println("File already exists");
          }
        }
      }
    } catch (FileNotFoundException e){
      System.out.println("ERROR in Compile.java: the given file '" + args[0] + "' is not found.");
    } catch (IOException e){
      System.out.println("ERROR in Compile.java: IOException has been raised in Compile.java");
    }
  }
}
