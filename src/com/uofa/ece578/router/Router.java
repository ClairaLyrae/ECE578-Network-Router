package com.uofa.ece578.router;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Router object containing 2-dimensional coordinates and a transceiver range.
 */

public class Router {
	protected float xpos = 0f;
	protected float ypos = 0f;
	protected float range = 0f;
	protected int id = 0;
	
	protected ForwardingTable fTable = new ForwardingTable();
	
	public Router(int id, float xpos, float ypos, float range) {
		this.xpos = xpos;
		this.ypos = ypos;
		this.range = range;
		this.id = id;
	}
	
	public ForwardingTable getForwardingTable() {
		return fTable;
	}

	/**
	 * Creates a forwarding table for this router following dijkstra's algorithm for the
	 * minimum spanning tree from a source node. 
	 * @param n Network to create forwarding table over
	 */
	public void calculateForwardingTable(Network n) {
		// Algorithm maps
		fTable.clear();
		Map<Router, Router> prevRouter = new HashMap<Router, Router>();
		Map<Router, Float> distanceTo = new HashMap<Router, Float>();
		Set<Router> visited = new HashSet<Router>();
		Set<Router> unvisited = new HashSet<Router>();
		// Start with source
		distanceTo.put(this, 0f);
		unvisited.add(this);
		// While we still have unvisited nodes
		while (unvisited.size() > 0) 
		{
			// Get minimum cost unvisited node
			Router node = null;
			for (Router r : unvisited) {
				if (node == null || getShortestDistance(r, distanceTo) < getShortestDistance(node, distanceTo)) {
					node = r;
				}
			}
			visited.add(node);
			unvisited.remove(node);
			// Find neighbors
			List<Router> adjacentNodes = new ArrayList<Router>();
			for (Router r : n.getRouters()) {
				if (!visited.contains(r)) {
					adjacentNodes.add(r);
				}
			}
			// Find minimal distances
			for (Router target : adjacentNodes) {
				if (node.isWithinRange(target) && getShortestDistance(target, distanceTo) > getShortestDistance(node, distanceTo) + node.costTo(target)) {
					distanceTo.put(target, getShortestDistance(node, distanceTo)
							+ node.costTo(target));
					prevRouter.put(target, node);
					unvisited.add(target);
				}
			}
		}
		// Now we backtrack the paths and update the forwarding table
		for(Router r : n.getRouters()) {
			LinkedList<Router> path = new LinkedList<Router>();
			Router step = r;
			// Does path exist?
			if (prevRouter.get(step) != null) {
				path.add(step);
				// Trace path to origin
				while (prevRouter.get(step) != null && prevRouter.get(step) != this) {
					step = prevRouter.get(step);
					path.add(step);
				}
				// Update the forwarding table using cost from path and nexthop id
				fTable.addEntry(r.getID(), path.getLast().getID(), distanceTo.get(r));
			}			
		}
		
	}

	
	
	
	private float getShortestDistance(Router destination, Map<Router, Float> distanceTo) {
		Float d = distanceTo.get(destination);
		if (d == null) {
			return Float.MAX_VALUE;
		} else {
			return d;
		}
	}
	

	public void setPos(float x, float y) {
		xpos = x;
		ypos = y;
	}
	
	public void setRange(float f) {
		range = f;
	}
	
	public float getXPos() {
		return xpos;
	}

	public float getRange() {
		return range;
	}
	
	public float getYPos() {
		return ypos;
	}
	
	public int getID() {
		return id;
	}

	/**
	 * Computes the link cost to another router, proportional to the
	 * power required for line-of-sight transmission
	 * @param r Router to compute cost to
	 * @return Power cost to transmit
	 */
	public float costTo(Router r) {
		float c = (float)(Math.pow(xpos-r.getXPos(), 2) + Math.pow(ypos - r.getYPos(), 2));
		if(c < 1f)
			return 1f;
		return c;
	}
	
	/**
	 * Finds the euclidean distance to the specified router
	 * @param r Router to compute distance to
	 * @return Distance to router
	 */
	public float distanceTo(Router r) {
		return (float)Math.sqrt(Math.pow(xpos-r.getXPos(), 2) + Math.pow(ypos - r.getYPos(), 2));
	}
	
	/**
	 * Checks to see whether the router is in range or not
	 * @param r Router to check range to
	 * @return True if router is in range
	 */
	public boolean isWithinRange(Router r) {
		return distanceTo(r) <= range;
	}
	
	@Override
	public String toString() {
		return "router[" + 
				"id=" + id + 
				",x=" + xpos +
				",y=" + ypos +
				",range=" + range +
				"]";
	}
}
