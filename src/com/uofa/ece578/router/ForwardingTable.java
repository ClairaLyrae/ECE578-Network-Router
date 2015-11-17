package com.uofa.ece578.router;

import java.util.HashMap;
import java.util.Map;

public class ForwardingTable {
	private class Entry {
		public float cost;
		public int nextHop;
		
		public Entry(int nextHop, float cost) {
			this.nextHop = nextHop;
			this.cost = cost;
		}
	}
	
	private Map<Integer, Entry> table = new HashMap<Integer, Entry>();
	
	public boolean hasEntry(int destination) {
		return table.containsKey(destination);
	}
	
	public int getNextHop(int destination) {
		return table.get(destination).nextHop;
	}
	
	public float getCost(int destination) {
		return table.get(destination).cost;
	}

	public void clear() {
		table.clear();
	}
	
	public void addEntry(int destination, int next, float cost) {
		table.put(destination, new Entry(next, cost));
	}

	public String toString() {
		return "";
	}
}
