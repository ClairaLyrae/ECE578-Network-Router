package com.uofa.ece578.router;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NavigableSet;
import java.util.TreeMap;

public class ForwardingTable {
	
	/**
	 * Entry to forwarding table
	 */
	public class Entry {
		public float cost;
		public int nextHop;
		public boolean visited;
		
		public Entry(int nextHop, float cost) {
			this.nextHop = nextHop;
			this.cost = cost;
			this.visited = false;
		}
	}
	
	private TreeMap<Integer, Entry> table = new TreeMap<Integer, Entry>();
	
	public boolean hasEntry(int destination) {
		return table.containsKey(destination);
	}
	
	public int getNextHop(int destination) {
		return table.get(destination).nextHop;
	}
	
	public float getCost(int destination) {
		return table.get(destination).cost;
	}
	
	public void setCost(int destination, float c) {
		Entry e = table.get(destination);
		if(e == null)
			return;
		e.cost = c;
	}
	
	public int size() {
		return table.size();
	}
	
	public boolean isVisited(int node) {
		Entry e = table.get(node);
		if(e == null)
			return false;
		return e.visited;
	}
	
	/**
	 * @return Iterator over destination ID's of table
	 */
	public Iterator<Integer> iterator() {
		return table.navigableKeySet().iterator();
	}

	public void clear() {
		table.clear();
	}
	
	public void addEntry(int destination, int next, float cost) {
		table.put(destination, new Entry(next, cost));
	}
	
	public Entry getEntry(int destination) {
		return table.get(destination);
	}
	
	public void removeEntry(int destination) {
		table.remove(destination);
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder("fTable[");
		for(Integer i : table.keySet()) {
			Entry e = table.get(i);
			str.append("[dest=" + i + ",hop=" + e.nextHop + ",cost=" + e.cost + "]");
			str.append(',');
		}
		str.append("]");
		return str.toString();
	}
}
