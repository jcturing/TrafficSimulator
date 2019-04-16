package es.ucm.fdi.control;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.ucm.fdi.model.RoadMap;

public class MakeVehicleFaultyEvent extends Event {

	private int duration;
	private List<String> vehiclesFaulty;
	
	public MakeVehicleFaultyEvent(int newTime, int newDuration, List<String> newVehiclesFaulty) {
		time = newTime;
		duration = newDuration;
		vehiclesFaulty = newVehiclesFaulty;
	}
	
	public void execute(RoadMap roadMap) throws RuntimeException {
		try {
			for(String _vehicles : vehiclesFaulty) {
				roadMap.getVehicle(_vehicles).setFaultyTime(duration);
			}
		} catch(RuntimeException e) {
			throw new RuntimeException("Vehicle faulty event references a non-existing object of "
										+ e.getLocalizedMessage());
		}
	}

	@Override
	public Map<String, String> describe() {
		Map<String, String> mapa = new HashMap<String, String>();
		
		String aux = vehiclesFaulty.toString();
		mapa.put("Time", ""+time);
		mapa.put("Type", "Break Vehicles " + aux);
			
		return mapa;
	}
	
}
