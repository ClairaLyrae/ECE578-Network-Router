package SecondPart;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

public class InfoTable {
	private Router destRouter;
	private double cost ; 
	private int nextHopId;
	
	
	public InfoTable()
	{
		destRouter = null;
		cost = -1;
		nextHopId = -1;
		
		
	}
	
	public InfoTable(Router dest , double Rcost , int nxtHop)
	{
		this.destRouter = dest;
		this.cost = Rcost;
		this.nextHopId = nxtHop;
	}
	

	@Override
	public String toString() {
		
		
		
		String result ="";
		
		if(destRouter ==null){
			return "No Destination Router";
		}
		
		result  = destRouter.getrouterNumber()+"\t\t"+String.format("%,10.3f", cost)+"\t"+nextHopId;
		return result;
	}
	
	public Router getDestRouter() {
		return destRouter;
	}
	
	public void setDestRouter(Router destRouter) {
		this.destRouter = destRouter;
	}
	
	public double getCost() {
		return cost;
	}
	
	public void setCost(double cost) {
		this.cost = cost;
	}
	
	public int getNextHopID() {
		return nextHopId;
	}
	
	public void setNextHopID(int nextHopID) {
		this.nextHopId = nextHopID;
	}
	
}
