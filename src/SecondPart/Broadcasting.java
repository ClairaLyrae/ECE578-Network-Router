package SecondPart;

import java.util.ArrayList;

public class Broadcasting {
	public static final double MAX_DISTANCE = 99999;
	public static final boolean check = false;
	
	private ArrayList<Router> totalRouters;
	private Router sourceRouter;
	private ForwardingTable forwardingTable;
	private ArrayList<Router> unreachableRouters;
	
	
	
	public Broadcasting(Router src)
	{
		totalRouters = new ArrayList<Router>();
		forwardingTable = new ForwardingTable();
		sourceRouter = src;
		unreachableRouters = new ArrayList<Router>();
	}
	
	public Broadcasting(Router src , ArrayList<Router> nodeList)
	{
		totalRouters = nodeList;
		forwardingTable = new ForwardingTable();
		sourceRouter = src;
		unreachableRouters = new ArrayList<Router>();
		initializeAllRoutersinList_toInitialRouterState();
		
	}
	
	
	public void prims_Algorithm()
	{

		
		sourceRouter.set_linkcost(0);
		sourceRouter.setstatus(Router.alreadyVisited);
		 Router nxtFndRouter_edge= foundNextShortEdgeDistance();
		
		if(check)
		{
			System.out.println("Routers added to Empty tree:");
		}
		 
		while(nxtFndRouter_edge!=null)
		{
			nxtFndRouter_edge= foundNextShortEdgeDistance();
			
			if(false && nxtFndRouter_edge!=null) {
				System.out.println(nxtFndRouter_edge.printRouterInfo());
			}
		}
		
		for(Router n : totalRouters){
			if(n.getstatus()==Router.notVisited){
			unreachableRouters.add(n);
			}	
		}
		
		
		
	}
	
public Router foundNextShortEdgeDistance(){
		Router nextRouter_AddTree=null;
		Router nextRouter_AddTree_Parent=null;
		Double DistBtwParentChild=MAX_DISTANCE;
		
		for(Router n : totalRouters){ 
			if(n.getstatus()==Router.alreadyVisited){
				ArrayList<Router> tempList = n.getroutersInRange();
				
				for(Router ndeInRnge : tempList){
					if(ndeInRnge.getstatus()==Router.notVisited && nextRouter_AddTree!=null ){
						
						double tempVal  = Calculations.getRoutersDistance(n, ndeInRnge);
						
						if(DistBtwParentChild> tempVal)
						{		
							nextRouter_AddTree = ndeInRnge;
							nextRouter_AddTree_Parent = n;
							DistBtwParentChild = tempVal;
											
						}
					}
					else if(ndeInRnge.getstatus()==Router.notVisited && nextRouter_AddTree==null)
					{
						nextRouter_AddTree = ndeInRnge;
						nextRouter_AddTree_Parent = n;
						DistBtwParentChild = Calculations.getRoutersDistance(n, ndeInRnge);
					}
				}
			}
		}
		
		
		if(nextRouter_AddTree==null)
		{
			return null;
		}
		
		nextRouter_AddTree.setprevRouter(nextRouter_AddTree_Parent);
		nextRouter_AddTree.set_linkcost(Calculations.getRoutersDistance(nextRouter_AddTree_Parent, nextRouter_AddTree));
		nextRouter_AddTree.setstatus(Router.alreadyVisited);
		
		return nextRouter_AddTree;
		
		
		
}
	


public String showTotalBraodcastTransmissionPower(){
		String result="";
		double totalCost=0;
		int totalTransmits=0;
		result += "Source Router of Tree is Router ID : "+sourceRouter.getrouterNumber()+"\n";
		result+="Source of Transmission \tSent To\t\tEnergy-Cost to Transmit\n";
		initializeAllRouters_toUnvisited();
		
		sourceRouter.setstatus(Router.alreadyVisited);
		
		boolean finished=false;
		while(!finished){
			finished = true;
			for(Router n: totalRouters){
				if(n.getstatus()==Router.alreadyVisited){
					//transmission to children
					double temp = transmitToNextRoutersinRange(n);
					if(temp!=0.0){	
						//transmission initiated
						//another node to transmit 
						ArrayList<Router> tempChildren = getChildrenToParentInTree(n);
						totalCost+=temp;
						totalTransmits++;
						result+="Router ID : "+n.getrouterNumber()+
								"\t\tRouters:";
						for(Router listRouter: tempChildren){ 
						result+="-"+listRouter.getrouterNumber();
						}
						result+="-\t\t  "+
						String.format("%,10.2f", temp)+" Units\n";
					}
					n.setstatus(Router.doneVisited);
					finished = false;
				}	
			}
		}
		
		result+= "\nTotal Transmissions: "+totalTransmits+
				"\nTotal Energy Cost of Transmissions: "+
				String.format("%,10.3f", totalCost)+"\n";
		
		return result;
}

	
	
private ArrayList<Router> getChildrenToParentInTree(Router currRouter){
		ArrayList<Router> resultList = new ArrayList<Router>();
		for(Router n : currRouter.getroutersInRange()){
			if(n.getprevRouter()==currRouter){
				resultList.add(n);
			}		
		}	
		return resultList;
	}
	
	
	public double transmitToNextRoutersinRange(Router currRouter){
		if(currRouter.getroutersInRange().size()==0){
			return 0.0;
		}
		
		
		Router farthestRouter=null;
		double linkCost=0.0;
	
		for(Router child: currRouter.getroutersInRange()){	
			if(child.getprevRouter()==currRouter){
				child.setstatus(Router.alreadyVisited);
				if(farthestRouter==null ){
					farthestRouter=child;
					linkCost = Calculations.getRoutersCost(currRouter, child);
					
				}
				else if(Calculations.getRoutersCost(currRouter, child)>linkCost){
					farthestRouter=child;
					linkCost = Calculations.getRoutersCost(currRouter, child);
					
				}
			}		
		}		
		return linkCost;
}
	
	
public String printPowerTransCost_TransIndividualMessage_toEveryRouter(){
		String result="";
		double totalCost=0;
		int totalTransmits=0;
		result+="Source\tDestination\tTransmits   Energy\n";
		
		for(Router n: totalRouters){
			if(n!=sourceRouter){

				double tempCost=costToTransmittoSingleRouterThroughFullPath(n);
				int tempTransmits=numberofTransmitsTo(n);
				
				result+="  "+sourceRouter.getrouterNumber()+"\t    "+n.getrouterNumber()+
						"\t\t   "+tempTransmits+"\t"+String.format("%,10.3f", tempCost)+"\n";
				totalCost+=tempCost;
				totalTransmits+=tempTransmits;
			}
		}
		result+="\nTotalEnergy: \t"+String.format("%,10.3f",totalCost)+"\nTotalTransmits: \t"+totalTransmits;	
		return result;
}
	
	
private int numberofTransmitsTo(Router destination)
	{
		int transmits=0;
		
		while(destination.getprevRouter()!=null)
		{
			transmits++;
			destination = destination.getprevRouter();
		}
		return transmits;
		
	}
	
	
	private double costToTransmittoSingleRouterThroughFullPath(Router destination)
	{
		double sum=0;
	
		while(destination.getprevRouter()!=null)
		{
			sum+=Calculations.getRoutersCost(destination, destination.getprevRouter());
			destination = destination.getprevRouter();
		}
		return sum;
}

	
	
	
private void initializeAllRoutersinList_toInitialRouterState(){
		for(int index=0 ; index<totalRouters.size();index++)
		{
			totalRouters.get(index).setstatus(Router.notVisited);
			totalRouters.get(index).set_linkcost(MAX_DISTANCE);
			totalRouters.get(index).setprevRouter(null);
		}
		
		
}
	
private void initializeAllRouters_toUnvisited(){
		for(int index=0 ; index<totalRouters.size();index++){
			totalRouters.get(index).setstatus(Router.notVisited);
		}
}
	
		
	
	public ArrayList<Router> gettotalRouters() {
		return totalRouters;
	}
	public void settotalRouters(ArrayList<Router> totalRouters) {
		this.totalRouters = totalRouters;
	}
	public Router getsourceRouter() {
		return sourceRouter;
	}
	public void setsourceRouter(Router sourceRouter) {
		this.sourceRouter = sourceRouter;
	}
	public ForwardingTable getforwardingTable() {
		return forwardingTable;
	}
	public void setforwardingTable(ForwardingTable forwardingTable) {
		this.forwardingTable = forwardingTable;
	}
	

	
	public String printUnreachableRouters()
	{
		String result="";
		
		if(unreachableRouters==null || unreachableRouters.size()==0)
		{
			result = "All Routers Reached";
			return result;
		}
		
		for(Router n : unreachableRouters)
		{
			result+="Router : "+n.printRouterInfo()+"/n";
			
		}
		
		
		return result;
	}
	

}
