package com.uofa.ece578.router;

/**
 * Listener class to allow other objects to listen to changes in the network graph 
 * and update accordingly. Used primarily for application environment.
 */

public interface NetworkListener {
	
	/**
	 * Called whenever the network is edited in some manner
	 */
	public void onEditNetwork();
	
}
