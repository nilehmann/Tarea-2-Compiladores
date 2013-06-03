package cool.compiler;

import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java_cup.runtime.Symbol;

/** The lexer driver class */
class Lexer {

    /** Loops over lexed tokens, printing them out to the console */
    public static void main(String[] args) {

		for (int i = 0; i < args.length; i++) {
		    FileReader file = null;
		    try {
				file = new FileReader(args[i]);

				System.out.println("#name \"" + args[i] + "\"");
				CoolLexer lexer = new CoolLexer(file);
				Symbol s;
				while ((s = lexer.next_token()).sym != sym.EOF) {
					
				    Utilities.dumpToken(System.out, lexer.getCurrentLine()+1, s);
				}
		    } catch (FileNotFoundException ex) {
		    	Utilities.fatalError("Could not open input file " + args[i]);
		    } catch (IOException ex) {
		    	Utilities.fatalError("Unexpected exception in lexer");
		    }
		}
    }
}