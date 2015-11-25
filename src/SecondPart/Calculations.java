package SecondPart;

public class Calculations {
	public static double getDistance(double x1, double y1, double x2, double y2){
	return Math.sqrt(Math.pow(x2-x1, 2)+Math.pow(y2-y1, 2));
}

public static double getRoutersDistance(Router n1, Router n2){
	return Calculations.getDistance(n1.getX(), n1.getY(), n2.getX(), n2.getY());
}

public static double getRoutersCost(Router n1 , Router n2){
	
	return Math.pow(getRoutersDistance( n1,  n2), 2);
	
}

}
