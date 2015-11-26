package com.uofa.ece578.router;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Network of router objects
 */

public class Network {
	
	private List<Router> routers = new LinkedList<Router>();
	private Set<NetworkListener> listeners = new HashSet<NetworkListener>();

	private Router source = null;

	/**
	 * @return Source router of network
	 */
	public Router getSource() {
		return source;
	}
	
	/**
	 * Sets the selected source router in the network to the given router
	 * @param r Source router
	 */
	public void setSource(Router r) {
		source = r;
		update();
	}
	
	/**
	 * Finds a router with the given natural ordering index 
	 * @param index
	 * @return Router at order given by index
	 */
	public Router getRouterByIndex(int index) {
		return routers.get(index);
	}
	
	/**
	 * @return Next unused router ID in network
	 */
	public int getNextValidID() {
		int i = 0;
		while(hasRouterWithID(i))
			i++;
		return i;
	}
	
	public List<Router> getRouters() {
		return routers;
	}
	
	/**
	 * Finds a router in the network with the given id, if it exists
	 * @param id Router id
	 * @return Router with given id
	 */
	public Router getRouterByID(int id) {
		for(Router r : routers) {
			if(r.getID() == id)
				return r;
		}
		return null;
	}
	
	/**
	 * Adds a new router defintiion to the network
	 * @param id Router id
	 * @param x X position of router
	 * @param y Y position of router
	 * @param range Range of router
	 * @return True if router was added
	 */
	public boolean addRouter(int id, float x, float y, float range) {
		if(getRouterByID(id) != null)
			return false;
		routers.add(new Router(id, x, y, range));
		update();
		return true;
	}
	
	public void addListener(NetworkListener listen) {
		listeners.add(listen);
	}
	
	public void removeListener(NetworkListener listen) {
		listeners.remove(listen);
	}
	
	/**
	 * Updates all network listeners and network computational states
	 */
	public void update() {
		for(Router r : routers)
			r.calculateForwardingTable(this);
		for(NetworkListener l : listeners) {
			l.onEditNetwork();
		}
	}
	
	/**
	 * Removes a router with the given id from the network
	 * @param id ID of router to remove
	 * @return True if router was removed
	 */
	public boolean removeRouter(int id) {
		Router r = getRouterByID(id);
		if(r == null)
			return false;
		routers.remove(r);
		update();
		return true;
	}
	
	/**
	 * @param id ID of router to find
	 * @return True if network has router with given id
	 */
	public boolean hasRouterWithID(int id) {
		return getRouterByID(id) != null;
	}
	
	/**
	 * @return Number of routers in network
	 */
	public int size() { 
		return routers.size();
	}
	
	/**
	 * Removes all routers in the network
	 */
	public void clear() {
		source = null;
		routers.clear();
		update();
	}

	/**
	 * Loads a network from a list of x, y, and r values for each router,
	 * separated by commas. 
	 * @param filename Name of file to load from
	 * @return Network defined by given file
	 * @throws Exception If file failed to read
	 */
	public static Network readFromFile(String filename) throws Exception {
		Network n = new Network();
		Scanner in = new Scanner(new File(filename));
		float x, y, r;
		int id = 1;
		while(in.hasNextLine()) {
			String[] split = in.nextLine().split(",");
			x = Float.parseFloat(split[0]);
			y = Float.parseFloat(split[1]);
			r = Float.parseFloat(split[2]);
			n.addRouter(id, x, y, r);
			id++;
		}
		in.close();
		return n;
	}
	
	/**
	 * Loads routers from a list of x, y, and r values for each router,
	 * separated by commas, and appends to the network.
	 * @param filename Name of file to load from
	 * @return True if file was read
	 * @throws Exception If file failed to read
	 */
	public boolean appendFromFile(String filename) throws Exception {
		clear();
		Scanner in = new Scanner(new File(filename));
		float x, y, r;
		int id = 1;
		while(in.hasNextLine()) {
			String[] split = in.nextLine().split(",");
			x = Float.parseFloat(split[0]);
			y = Float.parseFloat(split[1]);
			r = Float.parseFloat(split[2]);
			addRouter(id, x, y, r);
			id++;
		}
		in.close();
		return true;
	}
	
	/**
	 * Loads routers from a list of x, y, and r values for each router,
	 * separated by commas, and appends to the network.
	 * @param filename Name of file to load from
	 * @return True if file was read
	 * @throws Exception If file failed to read
	 */
	public boolean saveToFile(String filename) throws Exception {
		BufferedWriter out = new BufferedWriter(new FileWriter(new File(filename)));
		for(Router rtr : routers) {
			out.write(rtr.getXPos() + ", " + rtr.getYPos() + ", " + rtr.getRange() + "\n");
		}
		out.close();
		return true;
	}
	
	/**
	 * Used to get boundaries of router set
	 * @return Minimum x position in network
	 */
	public float getXMin() {
		float val = Float.MAX_VALUE;
		for(Router r : routers) {
			if(r.getXPos() < val)
				val = r.getXPos();
		}
		return val;
	}

	/**
	 * Used to get boundaries of router set
	 * @return Minimum y position in network
	 */
	public float getYMin() {
		float val = Float.MAX_VALUE;
		for(Router r : routers) {
			if(r.getYPos() < val)
				val = r.getYPos();
		}
		return val;
	}

	/**
	 * Used to get boundaries of router set
	 * @return Maximum x position in network
	 */
	public float getXMax() {
		float val = Float.MIN_VALUE;
		for(Router r : routers) {
			if(r.getXPos() > val)
				val = r.getXPos();
		}
		return val;
	}

	/**
	 * Used to get boundaries of router set
	 * @return Maximum y position in network
	 */
	public float getYMax() {
		float val = Float.MIN_VALUE;
		for(Router r : routers) {
			if(r.getYPos() > val)
				val = r.getYPos();
		}
		return val;
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder("network[");
		for(Router r : routers) {
			str.append(r.toString());
			str.append(',');
		}
		str.append("]");
		return str.toString();
	}
}
