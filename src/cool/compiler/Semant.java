package cool.compiler;
/*
Copyright (c) 2000 The Regents of the University of California.
All rights reserved.

Permission to use, copy, modify, and distribute this software for any
purpose, without fee, and without written agreement is hereby granted,
provided that the above copyright notice and the following two
paragraphs appear in all copies of this software.

IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR
DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES ARISING OUT
OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF THE UNIVERSITY OF
CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY
AND FITNESS FOR A PARTICULAR PURPOSE.  THE SOFTWARE PROVIDED HEREUNDER IS
ON AN "AS IS" BASIS, AND THE UNIVERSITY OF CALIFORNIA HAS NO OBLIGATION TO
PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, OR MODIFICATIONS.
 */

import java.io.FileReader;
import java.io.PrintStream;



/** Static semantics driver class */
class Semant {
	/** Reads AST from from console, and outputs the new AST */
	public static void main(String[] args) {
		
		for (int i = 0; i < args.length; i++) {
			FileReader file = null;
			try {
				file = new FileReader(args[i]);
				CoolLexer lexer = new CoolLexer(file);
				lexer.setFileName(args[i]);
				CoolParser parser = new CoolParser(lexer);
				
				Program result = (Program) parser.parse().value;

				if (parser.omerrs > 0) {
					System.err.println("Compilation halted due to lex and parse errors");
					System.exit(1);
				}
				
				ClassTable classTable = new ClassTable(result.classes);

				
				TypeChecker checker = new TypeChecker(classTable);
				result.accept(checker, new SymbolTable<String, AbstractType>(), null);	
				
				if(classTable.errors())
					System.err.println("Compilation halted due to static semantic errors");
				else{
					ASTVisitor<Void, Void, PrintStream, Integer> dump = new DumpVisitor();				
					((SingleNode) result).accept(dump, System.out, 0);
				}
			} catch (Exception ex) {
				ex.printStackTrace(System.err);
			}
		}
	}
}