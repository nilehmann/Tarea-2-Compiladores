package cool.compiler;

import java.util.ArrayList;
import java.util.Iterator;






abstract public class ListNode extends TreeNode {
	private ArrayList<SingleNode> elements;

	protected ListNode(int lineNumber, ArrayList<SingleNode> elements) {
		super(lineNumber);
		this.elements = elements;
	}

	/**
	 * Builds a new list node
	 * 
	 * @param lineNumber
	 *            line in the source file from which this node came.
	 * */
	protected ListNode(int lineNumber) {
		super(lineNumber);
		elements = new ArrayList<SingleNode>();
	}
	

	public abstract <R, R2, P1, P2> R2 accept(ASTVisitor<R, R2, P1, P2> visitor, P1 arg1, P2 arg2);

	public abstract ListNode copy();

	/**
	 * Creates a deep copy of this list.
	 * 
	 * None of the elements are shared between the lists, e.g. all elements are
	 * duplicated (which is what "deep copy" means).
	 * 
	 * @return a copy of this elements ArrayList
	 * */
	protected ArrayList<SingleNode> copyElements() {
		ArrayList<SingleNode> cp = new ArrayList<SingleNode>();
		for (int i = 0; i < elements.size(); i++)
			cp.add(((SingleNode) elements.get(i)).copy());

		return cp;
	}



	
	/**
	 * Retreives nth element of the list.
	 * 
	 * @param n
	 *            the index of the element
	 * @return the element
	 * */
	public SingleNode getNth(int n) {
		return (SingleNode) elements.get(n);
	}

	/**
	 * Retreives the length of the list.
	 * 
	 * @return the length of the list
	 * */
	public int getLength() {
		return elements.size();
	}

	/**
	 * Retreives the elements of the list as Enumeration.
	 * 
	 * @return the elements
	 * */
	public Iterator<SingleNode> getElements() {
		return elements.iterator();
	}

	/**
	 * Appends an element to the list.
	 * 
	 * <p>
	 * Note: each generated subclass of ListNode also has an appendElement()
	 * method, which calls addElement() and returns the list of the appropriate
	 * type, so that it can be used like this:
	 * <code>l.appendElement(i).appendElement(j).appendElement(k);</code>
	 * 
	 * @param node
	 *            a node to append
	 * */
	public void addElement(SingleNode node) {
		elements.add(node);
	}

	/**
	 * Returns a string representation of this list.
	 * 
	 * @return a string representation
	 * */
	public String toString() {
		return elements.toString();
	}
}
