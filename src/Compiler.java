import backend.ARMInstructionGenerator;
import backend.ARMInstructionPrinter;
import backend.directives.CodeSegment;
import backend.directives.DataSegment;
import backend.directives.TextSegment;
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
import java.util.List;

import optimize.ConstantPropagation;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import utils.NodeVisitor;
import utils.frontend.ParserErrorHandler;

public class Compiler {

  private static Object OptimizationLevel;

  public static void main(String[] args) {
    // Processing command line input
    if (args.length < 1) {
      System.out.println("No file/path has been supplied! Please specifiy a wacc file to compile!");
      return;
    }

    List<String> cmd_ops = new ArrayList<>();
    Collections.addAll(cmd_ops, Arrays.copyOf(args, args.length));

    // Creating the file instance for the .wacc file
    File file = new File(args[0]);

    // System.out.println(file.getName());
    // try-with-resources so that fis can be closed properly even when error occurs
    try (FileInputStream fis = new FileInputStream(file)) {
      // Input stream of the file
      CharStream input = CharStreams.fromStream(fis);
      // Pass the input stream of the file to WACC lexer
      WACCLexer lexer = new WACCLexer(input);
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
        SemanticChecker semanticChecker = new SemanticChecker();
        program = semanticChecker.visitProgram(tree);

        int optimise_cmd_index = cmd_ops.indexOf("--optimise");
        String optimise_level = cmd_ops.get(optimise_cmd_index + 1);
        System.out.println(optimise_level);
        switch (optimise_level) {
          case "0":
            break;
          case "1":
            NodeVisitor<Node> constPropOptimiser = new ConstantPropagation();
            program = constPropOptimiser.visit(program);
            System.out.println("optimising");
            break;
          default:
            System.out.println("unsupported optimisation level: " + optimise_level);
        }

        for (String cmd : cmd_ops) {
          System.out.println(cmd);
        }

        /* print optimised ast tree */
        if (cmd_ops.contains("--print_ast")) {
          ASTPrinter painter = new ASTPrinter();
          painter.visit(program);
        }

        if (cmd_ops.contains("--assembly")) {
          ARMInstructionGenerator generator = new ARMInstructionGenerator();
          generator.visit(program);
          DataSegment data = new DataSegment(generator.getDataSegmentMessages());
          TextSegment text = new TextSegment();
          CodeSegment code = new CodeSegment(generator.getInstructions());
          ARMInstructionPrinter printer = new ARMInstructionPrinter(data, text, code,
              ARMInstructionPrinter.OptimizationLevel.NONE);

          File asmFile = new File(file.getName().replaceFirst("[.][^.]+$", "") + ".s");

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
    } catch (FileNotFoundException e) {
      System.out.println("ERROR in Compile.java: the given file '" + args[0] + "' is not found.");
    } catch (IOException e) {
      System.out.println("ERROR in Compile.java: IOException has been raised in Compile.java");
    }
  }
}
