package cool.compiler;
import java.io.PrintStream;
import java_cup.runtime.Symbol;

public class Utilities {
//	// change this to true to enable table checking
//	private static final boolean checkTables = false;

	// sm: fixed an off-by-one error here; code assumed there were 80 spaces,
	// but
	// in fact only 79 spaces were there; I've made it 80 now
	// 1 2 3 4 5 6 7
	// 01234567890123456789012345678901234567890123456789012345678901234567890123456789
	private static String padding = "                                                                                "; // 80
																														// spaces
																														// for
																														// padding

	/**
	 * Prints error message and exits
	 * 
	 * @param msg
	 *            the error message
	 * */
	public static void fatalError(String msg) {
		(new Throwable(msg)).printStackTrace();
		System.exit(1);
	}

	/**
	 * Prints an appropritely escaped string
	 * 
	 * @param str
	 *            ream
	 * @param s
	 *            the string to print
	 * */
	public static void printEscapedString(PrintStream str, String s) {
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			switch (c) {
			case '\\':
				str.print("\\\\");
				break;
			case '\"':
				str.print("\\\"");
				break;
			case '\n':
				str.print("\\n");
				break;
			case '\t':
				str.print("\\t");
				break;
			case '\b':
				str.print("\\b");
				break;
			case '\f':
				str.print("\\f");
				break;
			default:
				if (c >= 0x20 && c <= 0x7f) {
					str.print(c);
				} else {
					String octal = Integer.toOctalString(c);
					str.print('\\');
					switch (octal.length()) {
					case 1:
						str.print('0');
					case 2:
						str.print('0');
					default:
						str.print(octal);
					}
				}
			}
		}
	}

	
	public static String escapedString( String s) {
		String str = "";
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			switch (c) {
			case '\\':
				str += "\\\\";
				break;
			case '\"':
				str += "\\\"";
				break;
			case '\n':
				str += "\\n";
				break;
			case '\t':
				str += "\\t";
				break;
			case '\b':
				str += "\\b";
				break;
			case '\f':
				str += "\\f";
				break;
			default:
				if (c >= 0x20 && c <= 0x7f) {
					str += c;
				} else {
					String octal = Integer.toOctalString(c);
					str += '\\';
					switch (octal.length()) {
					case 1:
						str += '0';
					case 2:
						str += '0';
					default:
						str += octal;
					}
				}
			}
		}
		return str;
	}
	
	/**
	 * Returns a string representation for a token
	 * 
	 * @param s
	 *            the token
	 * @return the string representation
	 * */
	public static String tokenToString(Symbol s) {
		switch (s.sym) {
		case sym.CLASS:
			return ("CLASS");
		case sym.ELSE:
			return ("ELSE");
		case sym.FI:
			return ("FI");
		case sym.IF:
			return ("IF");
		case sym.IN:
			return ("IN");
		case sym.INHERITS:
			return ("INHERITS");
		case sym.LET:
			return ("LET");
		case sym.LOOP:
			return ("LOOP");
		case sym.POOL:
			return ("POOL");
		case sym.THEN:
			return ("THEN");
		case sym.WHILE:
			return ("WHILE");
		case sym.ASSIGN:
			return ("ASSIGN");
		case sym.CASE:
			return ("CASE");
		case sym.ESAC:
			return ("ESAC");
		case sym.OF:
			return ("OF");
		case sym.DARROW:
			return ("DARROW");
		case sym.NEW:
			return ("NEW");
		case sym.STR_CONST:
			return ("STR_CONST");
		case sym.INT_CONST:
			return ("INT_CONST");
		case sym.BOOL_CONST:
			return ("BOOL_CONST");
		case sym.TYPEID:
			return ("TYPEID");
		case sym.OBJECTID:
			return ("OBJECTID");
		case sym.ERROR:
			return ("ERROR");
		case sym.error:
			return ("ERROR");
		case sym.LE:
			return ("LE");
		case sym.NOT:
			return ("NOT");
		case sym.ISVOID:
			return ("ISVOID");
		case sym.PLUS:
			return ("'+'");
		case sym.DIV:
			return ("'/'");
		case sym.MINUS:
			return ("'-'");
		case sym.MULT:
			return ("'*'");
		case sym.EQ:
			return ("'='");
		case sym.LT:
			return ("'<'");
		case sym.DOT:
			return ("'.'");
		case sym.NEG:
			return ("'~'");
		case sym.COMMA:
			return ("','");
		case sym.SEMI:
			return ("';'");
		case sym.COLON:
			return ("':'");
		case sym.LPAREN:
			return ("'('");
		case sym.RPAREN:
			return ("')'");
		case sym.AT:
			return ("'@'");
		case sym.LBRACE:
			return ("'{'");
		case sym.RBRACE:
			return ("'}'");
		case sym.EOF:
			return ("EOF");
		default:
			return ("<Invalid Token: " + s.sym + ">");
		}
	}

	/**
	 * Prints a token to stderr
	 * 
	 * @param s
	 *            the token
	 * */
	public static void printToken(Symbol s) {
		System.err.print(tokenToString(s));

		String val = null;

		switch (s.sym) {
		case sym.BOOL_CONST:
			System.err.print(" = " + s.value);
			break;
		case sym.INT_CONST:
			val = ((String) s.value);
			System.err.print(" = " + val);
			break;
		case sym.TYPEID:
		case sym.OBJECTID:
			val = ((String) s.value);
			System.err.print(" = " + val);
			break;
		case sym.STR_CONST:
			val = ((String) s.value);
			System.err.print(" = \"");
			printEscapedString(System.err, val);
			System.err.print("\"");
			break;
		case sym.ERROR:
			System.err.print(" = \"");
			printEscapedString(System.err, s.value.toString());
			System.err.print("\"");
			break;
		}
		System.err.println("");
	}

	/**
	 * Dumps a token to the specified stream
	 * 
	 * @param s
	 *            the token
	 * @param str
	 *            the stream
	 * */
	public static void dumpToken(PrintStream str, int lineno, Symbol s) {
		str.print("#" + lineno + " " + tokenToString(s));

		String val = null;

		switch (s.sym) {
		case sym.BOOL_CONST:
			str.print(" " + s.value);
			break;
		case sym.INT_CONST:
			val = ((String) s.value);
			str.print(" " + val);
			break;
		case sym.TYPEID:
		case sym.OBJECTID:
			val = ((String) s.value);
			str.print(" " + val);
			break;
		case sym.STR_CONST:
			val = ((String) s.value);
			str.print(" \"");
			printEscapedString(str, val);
			str.print("\"");
			break;
		case sym.ERROR:
			str.print(" \"");
			printEscapedString(str, s.value.toString());
			str.print("\"");
			break;
		}

		str.println("");
	}

	/**
	 * Returns the specified amount of space padding
	 * 
	 * @param n
	 *            the amount of padding
	 * */
	public static String pad(int n) {
		if (n > 80)
			return padding;
		if (n < 0)
			return "";
		return padding.substring(0, n);
	}
}
