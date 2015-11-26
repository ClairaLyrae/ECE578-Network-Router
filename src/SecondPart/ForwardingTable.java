package SecondPart;

import java.util.ArrayList;

public class ForwardingTable {
	private ArrayList<InfoTable> row;
	private Integer sourceRouterId;
	

	public ForwardingTable()
	{
		row = new ArrayList<InfoTable>();
		sourceRouterId = -1;
		
	}
	
	public ForwardingTable(int sourceRouter)
	{
		
		row = new ArrayList<InfoTable>();
		this.sourceRouterId = sourceRouter;
		
	}
	
	public void setSourceRouterID(int ID)
	{
		sourceRouterId = ID;
	}
	
	public ArrayList<InfoTable> getTable_LinkedList()
	{
		return row;
	}
	 
	public void setTable(ArrayList<InfoTable> atable)
	{
		row = atable;
		
	}
	
	public void addRow(InfoTable rRow)
	{
		row.add(rRow);
		
	}
	
	public String printoutCurrentTable()
	{
		String outString="";
		
		if(row.size()==0){
			
			return "Forwarding Table for Router ID: "+sourceRouterId.toString()+" EMPTY\n";
		}
		outString = "Forwarding Table for Router ID : "+ sourceRouterId.toString()+"\n";
		
		
		outString += "Destination\t    Cost\tNextHop\n";
		
		for (int index = 0; index<row.size();index++)
		{		
			outString +="   "+  row.get(index).toString()+"\n";
			
		}
		
		return outString;	
	}

}
