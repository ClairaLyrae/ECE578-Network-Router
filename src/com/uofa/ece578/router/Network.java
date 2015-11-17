package com.uofa.ece578.router;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Network {
	private Set<Router> routers = new HashSet<Router>();

	public Network() {
		
	}
	
	public void addRouter(Router r) {
		routers.add(r);
	}
	
	public void removeRouter(Router r) {
		routers.remove(r);
	}
	
	public int size() { 
		return routers.size();
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
		while(in.hasNextLine()) {
			String[] split = in.nextLine().split(",");
			x = Float.parseFloat(split[0]);
			y = Float.parseFloat(split[1]);
			r = Float.parseFloat(split[2]);
			n.addRouter(new Router(x, y, r));
		}
		in.close();
		return n;
	}
}
