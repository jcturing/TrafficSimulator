package es.ucm.fdi.model;

import java.util.Map;

public class LanesRoad extends Road{

	private final String type = "lanes";
	
	private int lanes;
	
	public LanesRoad(String newId, Junction src, Junction dest, int max_speed, int newLength, int newLanes) {
		super(newId, src, dest, max_speed, newLength);
		lanes = newLanes;
	}

	public int calculateBaseSpeed() {
		return Math.min(getMaxSpeed(),
				((getMaxSpeed() * lanes)/
						(Math.max((int) getVehicles().sizeOfValues(), 1))+1));
	}
	
	public int reduceSpeedFactor(int vehicleFaultyTime, int faultyCounter) {
		int aux = 1;
		if(vehicleFaultyTime > 0) {
			faultyCounter++;
			if(faultyCounter >= lanes) aux = 2;
		}
		return aux;
	}
	
	protected void fillReportDetails(Map<String, String> m) {
		m.put("type", type);
		super.fillReportDetails(m);
	}
	
}
