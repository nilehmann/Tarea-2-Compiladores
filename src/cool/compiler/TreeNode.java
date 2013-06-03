package cool.compiler;

import java.io.PrintStream;




abstract public class TreeNode {
	/** line in the source file from which this node came. */
	protected int lineNumber;

	/**
	 * Builds a new tree node
	 * 
	 * @param lineNumber
	 *            the line in the source file from which this node came.
	 * */
	protected TreeNode(int lineNumber) {
		this.lineNumber = lineNumber;
	}



	/**
	 * Sets the values of this node object to the values of a given node.
	 * 
	 * @param other
	 *            the other node
	 * @return this node
	 * */
	public TreeNode set(TreeNode other) {
		this.lineNumber = other.lineNumber;
		return this;
	}

	/**
	 * Retrieves the line number from which this node came.
	 * 
	 * @return the line number
	 * */
	public int getLineNumber() {
		return lineNumber;
	}



	/**
	 * Copies a boolean value.
	 * 
	 * This method is used internally by the generated AST classes
	 * */
	protected Boolean copy_Boolean(Boolean b) {
		return new Boolean(b.booleanValue());
	}

	/**
	 * Copies an AbstractSymbol value.
	 * 
	 * This method is used internally by the generated AST classes
	 * */
	protected String copy_String(String sym) {
		return sym;
	}

	/**
	 * Dumps a printable representation of current line number
	 * 
	 * This method is used internally by the generated AST classes
	 * */
	public void dump_line(PrintStream out, int n) {
		out.println(Utilities.pad(n) + "#" + lineNumber);
	}
}
