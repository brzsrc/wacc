import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import antlr.*;

public class Compiler {
    public static void main(String[] args) {
        // Creating the file instance for the .wacc file
        File file = new File(args[0]);
        // try-with-resources so that fis can be closed properly even when error occurs
        try (FileInputStream fis = new FileInputStream(file)) {
            // Input stream of the file
            CharStream input = CharStreams.fromStream(fis);
            // Pass the input stream of the file to WACC lexer
            FullLexer lexer = new FullLexer(input);
            // Obtain the internal tokens from the lexer
            CommonTokenStream tokens  = new CommonTokenStream(lexer);
            // Parse the tokens into a syntax tree
            FullParser parser = new FullParser(tokens);
            // Start parsing using the `program` rule defined in antlr_config/FullParser.g4
            ParseTree tree = parser.program();

            System.out.println(tree.toStringTree(parser));
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: the given file is not found. Perhaps you entered a wrong path/filename?");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
