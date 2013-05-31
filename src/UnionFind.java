import java.util.HashMap;
import java.util.Map;


public class UnionFind {
	private Map<String, String> parent;
	private Map<String, Integer> rank;
	private int maxRank;
	
	
	public UnionFind(){
		parent = new HashMap<String, String>();
		rank = new HashMap<String, Integer>();
		maxRank = 0;
	}
	
	
	public String find(String a){
		if(!parent.containsKey(a))
			parent.put(a, a);
		
		if(!parent.get(a).equals(a))
			parent.put(a, find(parent.get(a)));
		
		return parent.get(a);
	}
	
	public void union(String a, String b){
		String pa = find(a);
		String pb = find(b);
		
		if(pa.equals(pb))
			return;
		
		if(!rank.containsKey(pa))
			rank.put(pa, 1);
		if(!rank.containsKey(pb))
			rank.put(pb, 1);
		
		parent.put(pa, pb);
		rank.put(pb, rank.get(pb)+rank.get(pa));
		
		if(rank.get(pb) > maxRank)
			maxRank = rank.get(pb);
	}
	
	public int getRank(String a){
		if(!rank.containsKey(a))
			return 1;
		return rank.get(a);
	}
	
	public int getMaxRank(){
		return maxRank;
	}
	
}
