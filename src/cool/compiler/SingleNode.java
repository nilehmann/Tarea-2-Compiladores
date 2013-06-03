package cool.compiler;





abstract public class SingleNode extends TreeNode{
	/**
	 * Creates a copy of this node.
	 * 
	 * @return a copy of this node
	 * */
	protected SingleNode(int lineNumber) {
		super(lineNumber);
	}


	public abstract SingleNode copy();
	public abstract <R, R2, P1, P2> R accept(ASTVisitor<R, R2, P1, P2> visitor, P1 arg1, P2 arg2);

}
