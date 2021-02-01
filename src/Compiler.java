import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import antlr.*;

public class Compiler {
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
        // try-with-resources so that fis can be closed properly even when error occurs
        try (FileInputStream fis = new FileInputStream(file)) {
            // Input stream of the file
            CharStream input = CharStreams.fromStream(fis);
            // Pass the input stream of the file to WACC lexer
            WACCLexer lexer = new WACCLexer(input);
            // Obtain the internal tokens from the lexer
            CommonTokenStream tokens  = new CommonTokenStream(lexer);
            // Parse the tokens into a syntax tree
            WACCParser parser = new WACCParser(tokens);
            // Start parsing using the `program` rule defined in antlr_config/WACCParser.g4
            ParseTree tree = parser.program();

            // If the `--parse_only` flag is specified, then we do not run semantic analysis
            if (!cmd_ops.contains("--parse_only")) {
                /* semantic check here */
            }
            
            if (cmd_ops.contains("--print_ast")) {
                System.out.println(tree.toStringTree(parser));
            }
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: the given file '" + args[0] + "' is not found.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
