package com.uofa.ece578.router;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


/**
 * Main class composing the command line version of the network router program. See {@link com.uofa.ece578.NetworkRouter} for the
 * application version of the program. If program is run with the '-f' flag, the network will
 * be read from a user specified file instead of through manual input. 
 */

public class NetworkRouter {
	
	private static Network network;
	
	public static void main(String[] args) {
		boolean readFromFile = false;
		
		// Check for runtime flags
		for(String arg : args) {
			if(arg.equalsIgnoreCase("-f")) {
				readFromFile = true;
			}
		}
		
		// Load network either from a file or by user input
		if(!readFromFile) {
			network = new Network();
			String line;
			int numRouters;
			Scanner in = new Scanner(System.in);
			while(true) {
				System.out.print("Enter in the number of routers: ");
				line = in.nextLine();
				try {
					numRouters = Integer.parseInt(line);
				} catch(Exception e) {
					System.out.println("Incorrect format!");
					continue;
				}
				break;
			}
	
			System.out.println("For each router, specify the x position, y position, and transciever range separated by commas: ");
			for(int i = 0; i < numRouters; i++) {
				float x, y, r;
				System.out.print("Router[" + i + "]:");
				String[] split = in.nextLine().split(",");
				if(split.length != 3) {
					System.out.println("Not enough values!");
					i--;
					continue;
				}
				try {
					x = Float.parseFloat(split[0]);
					y = Float.parseFloat(split[0]);
					r = Float.parseFloat(split[0]);
				} catch(Exception e) {
					System.out.println("Incorrect format!");
					i--;
					continue;
				}
				network.addRouter(i, x, y, r);
			}
			in.close();
		} else {
			try {
				 network = Network.readFromFile("network.txt");
				 System.out.println("Loaded " + network.size() + " router definitions from file.");
			} catch (Exception e) {
				System.out.println("Failed to read file!");
				return;
			}
		}
		
		
		// TODO Do stuff with network here, the assignment bits!
	}
}
