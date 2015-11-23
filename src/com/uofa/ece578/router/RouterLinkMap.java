package com.uofa.ece578.router;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Collection of router-to-router link definitions used for drawing specific link graphs
 * onto the network display.
 */

public class RouterLinkMap {
	
	private Map<Integer, Set<Integer>> links = new HashMap<Integer, Set<Integer>>();
	
	public RouterLinkMap() {
		
	}
	
	public void addLink(int src, int dest) {
		Set<Integer> routers = links.get(src);
		if(routers == null)
			links.put(src, routers = new HashSet<Integer>());
		routers.add(dest);
	}
	
	public void removeLink(int src, int dest) {
		Set<Integer> routers = links.get(src);
		if(routers == null)
			return;
		routers.remove(dest);
	}
	
	public void clear() {
		links.clear();
	}
	
	public Set<Integer> getLinksFrom(int src) {
		Set<Integer> routers = links.get(src);
		if(routers == null)
			links.put(src, routers = new HashSet<Integer>());
		return routers;
	}
	
	public Set<Integer> getLinksTo(int src) {
		Set<Integer> routers = new HashSet<Integer>();
		for(Integer i : links.keySet()) {
			if(links.get(i).contains(src))
				routers.add(i);
		}
		return routers;
	}
}
