package es.ucm.fdi.model;

import java.util.Map;

public class DirtRoad extends Road{

	private final String type = "dirt";
	
	public DirtRoad(String newId, Junction src, Junction dest, int max_speed, int newLength) {
		super(newId, src, dest, max_speed, newLength);
	}
	
	public int calculateBaseSpeed() {
		return getMaxSpeed();
	}
	
	public int reduceSpeedFactor(int vehicleFaultyTime, int faultyCounter) {
		if(vehicleFaultyTime > 0) {
			faultyCounter++;
		}
		return 1 + faultyCounter;
	}

	protected void fillReportDetails(Map<String, String> m) {
		m.put("type", type);
		super.fillReportDetails(m);
	}
	
}
