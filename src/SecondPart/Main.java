package SecondPart;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
			
			public static void main(String[] args) {
				
				if (args.length != 3){
					System.out.println("Input: coordinatesFile commonRange sourceRouter");
				}
				ArrayList<Double> numbers = new ArrayList<>();
				ArrayList<Router> network_linkstate = new ArrayList<Router>();
				ArrayList<Router> network_broadcast = new ArrayList<Router>();
				Router srcRouter=null;
				Scanner scanner = null;
				try {
					scanner = new Scanner(new File(args[0]));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				while(scanner.hasNextDouble()){
					numbers.add(scanner.nextDouble());
				}
				for (int i = 0; i < numbers.size(); i += 2){		
					network_linkstate.add(new Router(numbers.get(i),numbers.get(i+1), Double.valueOf(args[1])));
					network_broadcast.add(new Router(numbers.get(i),numbers.get(i+1), Double.valueOf(args[1])));
				}
				
				for(int index=0 ; index<network_linkstate.size();index++)
				{
					network_linkstate.get(index).setrouterNumber(index+1);
				}
				for(int index=0 ; index<network_broadcast.size();index++)
				{
					network_broadcast.get(index).setrouterNumber(index+1);
				}
				
				for (Router n : network_linkstate){
					n.findRoutersInRange(network_linkstate);
				}
				for (Router n : network_broadcast){
					n.findRoutersInRange(network_broadcast);
				}
				for(Router n : network_linkstate){
					if(n.getrouterNumber()==(new Integer(args[2]))){srcRouter=n; break;}
				}
				
				
				
				System.out.println("\nBroadcasting Routing Spanning Tree Using Prim's Algorithm\n");	
				Broadcasting broadcastRouting= new Broadcasting(network_broadcast.get(0), network_broadcast);
				broadcastRouting.prims_Algorithm();
				System.out.println(broadcastRouting.showTotalBraodcastTransmissionPower());
				System.out.println("\nEnergy needed without Broadcasting advantage\n");
				System.out.println(broadcastRouting.ShowPowerTransCost_TransIndividualMessage_toEveryRouter());
				
				try{
					GraphingNetwork g = new GraphingNetwork(network_broadcast, 1, 2);
				}catch(Exception e){}
				
				return;
			}
	}
