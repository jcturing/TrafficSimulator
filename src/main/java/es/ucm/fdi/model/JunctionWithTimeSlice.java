package es.ucm.fdi.model;

import java.util.HashMap;
import java.util.Map;

import es.ucm.fdi.view.Describable;

public class JunctionWithTimeSlice extends Junction implements Describable{
	
	public class IncomingRoadWithTimeSlice extends IncomingRoad{
		protected int timeUnits = 0;
		protected int timeSlice = 0;
		protected int fullyUsedCounter = 0;
		protected boolean used = false;
		protected boolean fullyUsed = false;
	}
	
	public JunctionWithTimeSlice(String id) {
		super(id);
	}
	
	public Map<String, String> describe() {
		Map<String, String> mapa = new HashMap<String, String>();	
		String red = "";
		String green = "";	
		red = "[ ";
		green = "[ ";
		
		for(Road r : inRoads.keySet()) {
			if(inRoads.get(r).trafficLight) {
				int units = ((IncomingRoadWithTimeSlice) inRoads.get(r)).timeUnits;
				green = green + "("+r.id+ "," + "green:" + units + "," + queueString(inRoads.get(r)) + ")"; 
			} else {
				red = red + "(" + r.id + "," + "red, " + queueString(inRoads.get(r)) + ")";
			}
		}
		
		red = red + " ]";
		green = green + " ]";
		mapa.put("ID", id);
		mapa.put("Green", ""+green);
		mapa.put("Red", ""+red);
		return mapa;
	}
	
}