package SecondPart;

import java.util.ArrayList;

public class Router {
	public static final int notVisited = 0;
	public static final int alreadyVisited = 1;
	public static final int doneVisited=3;
	
	private double[] coordinates;
	private double commonRange;
	private double cost;
	private ForwardingTable forwardingTable;
	private ArrayList<Router> routersInRange;
	private int routerNumber;
	private int status;
	private Router prevRouter;
	
	static int numberOfRouters = 0;
	
	public Router(){
		coordinates = new double[2];
		coordinates[0] = 0;
		coordinates[1] = 0;
		commonRange = 0;
		forwardingTable = null;
		routersInRange = new ArrayList<Router>();
		routerNumber = ++Router.numberOfRouters;
	}
	
	public Router(double x, double y, double commRange ){
		coordinates = new double[2];
		coordinates[0] = x;
		coordinates[1] = y;
		commonRange = commRange;
		forwardingTable = null;
		routersInRange = new ArrayList<Router>();
		routerNumber = ++Router.numberOfRouters;
	}
	
	public void findRoutersInRange(ArrayList<Router> network){
		for (Router n : network){
			if (n != this){
				if (Calculations.getDistance(getX(), getY(), n.getX(), n.getY()) <= commonRange){
					routersInRange.add(n);
				}
			}
		}
	}

	@Override
	public String toString() {
		String result = routerNumber + ": (" + getX() + "," + getY() + ") ";
		return result;
	}
	

	public double[] getcoordinates() {
		return coordinates;
	}

	public void setcoordinates(double[] coordinates) {
		this.coordinates = coordinates;
	}

	public double getcommonRange() {
		return commonRange;
	}

	public void setcommonRange(double commonRange) {
		this.commonRange = commonRange;
	}

	public double getlinkcost() {
		return status;
	}

	public void setlinkcost(double distance) {
		this.cost = distance;
	}

	public ForwardingTable getforwardingTable() {
		return forwardingTable;
	}

	public void setforwardingTable(ForwardingTable forwardingTable) {
		this.forwardingTable = forwardingTable;
	}

	public ArrayList<Router> getroutersInRange() {
		return routersInRange;
	}

	public void setroutersInRange(ArrayList<Router> routersInRange) {
		this.routersInRange = routersInRange;
	}

	public int getrouterNumber() {
		return routerNumber;
	}

	public void setrouterNumber(int routerNumber) {
		this.routerNumber = routerNumber;
	}

	public int getstatus() {
		return status;
	}


	public void setstatus(int status) {
		this.status = status;
	}

	public Router getprevRouter() {
		return prevRouter;
	}

	public void setprevRouter(Router prevRouter) {
		this.prevRouter = prevRouter;
	}
	
	public double getX(){
		return coordinates[0];
	}
	
	public double getY(){
		return coordinates[1];
	}

	public String printRouterInfo()
	{
		String result="";
		
		result += "Coordinates ("+coordinates[0]+","+coordinates[1]+") RouterID: "+
					routerNumber;
		
		return result;
	}
}
