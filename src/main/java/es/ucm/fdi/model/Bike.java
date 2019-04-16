package es.ucm.fdi.model;

import java.util.List;
import java.util.Map;

public class Bike extends Vehicle{

	private final String type = "bike";
	
	public Bike(String newId, int max_speed, List<Junction> newItinerary) {
		super(newId, max_speed, newItinerary);
	}
	
	public void setFaultyTime(int averia){
		if(!(getSpeedActual() <= getSpeedMax()/2)) {
			super.setFaultyTime(getFaultyTime() + averia);
		}
	}
	
	protected void fillReportDetails(Map<String, String> m) {
		m.put("type", type);
		super.fillReportDetails(m);
	}
	
}
