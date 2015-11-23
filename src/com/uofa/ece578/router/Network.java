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
import java.util.TreeMap;

/**
 * Network of router objects
 */

public class Network {
	
	private List<Router> routers = new LinkedList<Router>();
	private Set<NetworkListener> listeners = new HashSet<NetworkListener>();

	public Network() {
		
	}

	public Router getRouterByIndex(int index) {
		return routers.get(index);
	}
	
	public int getNextValidID() {
		int i = 0;
		while(hasRouterWithID(i))
			i++;
		return i;
	}
	
	public List<Router> getRouters() {
		return routers;
	}
	
	public Router getRouterByID(int id) {
		for(Router r : routers) {
			if(r.getID() == id)
				return r;
		}
		return null;
	}
	
	public boolean addRouter(int id, float x, float y, float range) {
		if(getRouterByID(id) != null)
			return false;
		routers.add(new Router(id, x, y, range));
		callEvent();
		return true;
	}
	
	public void addListener(NetworkListener listen) {
		listeners.add(listen);
	}
	
	public void removeListener(NetworkListener listen) {
		listeners.remove(listen);
	}
	
	public void callEvent() {
		for(NetworkListener l : listeners) {
			l.onEditNetwork();
		}
	}
	
	public boolean removeRouter(int id) {
		Router r = getRouterByID(id);
		if(r == null)
			return false;
		routers.remove(r);
		callEvent();
		return true;
	}
	
	public boolean hasRouterWithID(int id) {
		return getRouterByID(id) != null;
	}
	
	public int size() { 
		return routers.size();
	}
	
	public void clear() {
		routers.clear();
		callEvent();
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
		int id = 0;
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
		int id = 0;
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
}
