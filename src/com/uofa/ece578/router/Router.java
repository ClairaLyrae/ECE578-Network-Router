package com.uofa.ece578.router;

/**
 * Router object containing 2-dimensional coordinates and a transceiver range.
 */

public class Router {
	protected float xpos = 0f;
	protected float ypos = 0f;
	protected float range = 0f;
	protected int id = 0;
	
	protected ForwardingTable forwardTable = new ForwardingTable();
	
	public Router(int id, float xpos, float ypos, float range) {
		this.xpos = xpos;
		this.ypos = ypos;
		this.range = range;
		this.id = id;
	}
	
	public ForwardingTable getForwardingTable() {
		return forwardTable;
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

	public float getYPos() {
		return xpos;
	}

	/**
	 * Computes the link cost to another router, proportional to the
	 * power required for line-of-sight transmission
	 * @param r Router to compute cost to
	 * @return Power cost to transmit
	 */
	public float costTo(Router r) {
		float c = (float)(xpos*r.xpos + ypos*r.ypos);
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
		return (float)Math.sqrt(xpos*r.xpos + ypos*r.ypos);
	}
	
	/**
	 * Checks to see whether the router is in range or not
	 * @param r Router to check range to
	 * @return True if router is in range
	 */
	public boolean isWithinRange(Router r) {
		return distanceTo(r) <= range;
	}
}
