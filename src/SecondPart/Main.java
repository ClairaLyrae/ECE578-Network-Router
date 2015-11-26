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
				
				network_broadcast.add(new Router(numbers.get(i),numbers.get(i+1), Double.valueOf(args[1])));
			}
			
			
			for(int index=0 ; index<network_broadcast.size();index++)
			{
				network_broadcast.get(index).setrouterNumber(index);
			}
			
			for (Router n : network_broadcast){
				n.findRoutersInRange(network_broadcast);
			}
			
			
			
			
			System.out.println("\nBroadcast Routing Spanning Tree Using Prim's Algorithm\n");
			
			
			Broadcasting broadcastRouting= new Broadcasting(network_broadcast.get(0), network_broadcast);
			broadcastRouting.prims_Algorithm();
			//System.out.println(broadcastRouting.printPowerTransMissionCost_TransmitSingleMessage_toEveryNode());
			System.out.println(broadcastRouting.showTotalBraodcastTransmissionPower());
			System.out.println("\nEnergy required without Broadcast advantage and only Unicast");
			System.out.println(broadcastRouting.ShowPowerTransCost_TransIndividualMessage_toEveryRouter());
			
			try{
				GraphingNetwork g = new GraphingNetwork(network_broadcast, 1, 2);
			}catch(Exception e){}
			
			return;
		}

	}
