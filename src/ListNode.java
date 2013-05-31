


import java.io.PrintStream;
import java.util.Vector;
import java.util.Enumeration;

    
abstract class ListNode extends TreeNode {
    private Vector<TreeNode> elements;

    protected ListNode(int lineNumber, Vector<TreeNode> elements) {
	super(lineNumber);
	this.elements = elements;
    }

    /** Builds a new list node
     *
     * @param lineNumber line in the source file from which this node came. 
     * */
    protected ListNode(int lineNumber) {
	super(lineNumber);
	elements = new Vector<TreeNode>();
    }

    /** Creates a deep copy of this list.
     *
     * None of the elements are shared between the lists, e.g. all
     * elements are duplicated (which is what "deep copy" means).
     *
     * @return a copy of this elements vector
     * */
    protected Vector<TreeNode> copyElements() {
	Vector<TreeNode> cp = new Vector<TreeNode>();
	for (int i = 0; i < elements.size(); i++) {
	    cp.addElement(((TreeNode)elements.elementAt(i)).copy());
	}
	return cp;
    }

    /** Returns the class of list elements.
     *
     * @return the element class
     * */
    public abstract Class getElementClass();

    /** Retreives nth element of the list.
     *
     * @param n the index of the element
     * @return the element
     * */
    public TreeNode getNth(int n) {
	return (TreeNode)elements.elementAt(n);
    }

    /** Retreives the length of the list.
     *
     * @return the length of the list
     * */
    public int getLength() {
	return elements.size();
    }

    /** Retreives the elements of the list as Enumeration.
     *
     * @return the elements
     * */
    public Enumeration<TreeNode> getElements() {
	return elements.elements();
    }

    /** Appends an element to the list.
     * 
     * <p>Note: each generated subclass of ListNode also has an
     * appendElement() method, which calls addElement() and returns the
     * list of the appropriate type, so that it can be used like this:
     * <code>l.appendElement(i).appendElement(j).appendElement(k);</code>
     *
     * @param node a node to append
     * */
    public void addElement(TreeNode node) {
	elements.addElement(node);
    }



    /** Returns a string representation of this list.
     *
     * @return a string representation
     * */
    public String toString() {
	return elements.toString();
    }
}
	


    
    

    

    
    
