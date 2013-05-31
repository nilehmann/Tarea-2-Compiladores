import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * Implements the symbol table data abstraction. Cool symbol tables are
 * implemented using Java hashtables. Each hashtable represents a scope and
 * associates a symbol with some data. The ``data'' is whatever data the
 * programmer wishes to associate with each identifier. An example illustrating
 * the use of symbol tables is in the file SymtabExample.java.
 * 
 * @see AbstractSymbol
 * @see SymtabExample
 * */
class SymbolTable<K, V> {
	private Stack<Map<K, V>> tbl;

	/** Creates an empty symbol table. */
	public SymbolTable() {
		tbl = new Stack<Map<K, V>>();
	}

	/**
	 * Enters a new scope. A scope must be entered before anything can be added
	 * to the table.
	 * */
	public void enterScope() {
		tbl.push(new HashMap<K, V>());

	}

	/** Exits the most recently entered scope. */
	public void exitScope() {
		if (tbl.empty()) {
			Utilities.fatalError("existScope: can't remove scope from an empty symbol table.");
		}
		tbl.pop();
	}

	/**
	 * Adds a new entry to the symbol table.
	 * 
	 * @param id
	 *            the symbol
	 * @param info
	 *            the data asosciated with id
	 * */
	public void addId(K id, V info) {
		if (tbl.empty()) {
			Utilities.fatalError("addId: can't add a symbol without a scope.");
		}
		tbl.peek().put(id, info);
	}

	/**
	 * Looks up an item through all scopes of the symbol table. If found it
	 * returns the associated information field, if not it returns
	 * <code>null</code>.
	 * 
	 * @param sym
	 *            the symbol
	 * @return the info associated with sym, or null if not found
	 * */
	public V lookup(K sym) {
		if (tbl.empty()) {
			Utilities.fatalError("lookup: no scope in symbol table.");
		}
		// I break the abstraction here a bit by knowing that stack is
		// really a vector...
		for (int i = tbl.size() - 1; i >= 0; i--) {
			V info = tbl.elementAt(i).get(sym);
			if (info != null)
				return info;
		}
		return null;
	}

	/**
	 * Probes the symbol table. Check the top scope (only) for the symbol
	 * <code>sym</code>. If found, return the information field. If not return
	 * <code>null</code>.
	 * 
	 * @param sym
	 *            the symbol
	 * @return the info associated with sym, or null if not found
	 * */
	public V probe(K sym) {
		if (tbl.empty()) {
			Utilities.fatalError("lookup: no scope in symbol table.");
		}
		return tbl.peek().get(sym);
	}

	/**
	 * Gets the string representation of the symbol table.
	 * 
	 * @return the string rep
	 * */
	public String toString() {
		String res = "";
		// I break the abstraction here a bit by knowing that stack is
		// really a vector...
		for (int i = tbl.size() - 1, j = 0; i >= 0; i--, j++) {
			res += "Scope " + j + ": " + tbl.elementAt(i) + "\n";
		}
		return res;
	}
}
