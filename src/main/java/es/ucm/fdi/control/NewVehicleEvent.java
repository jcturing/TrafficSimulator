package es.ucm.fdi.control;

import java.util.*;

import es.ucm.fdi.model.Junction;
import es.ucm.fdi.model.RoadMap;
import es.ucm.fdi.model.Vehicle;
import es.ucm.fdi.view.Describable;

public class NewVehicleEvent extends Event implements Describable{
	
	protected String id;
	protected int maxSpeed;
	protected List<String> itinerary;
	
	
	public NewVehicleEvent(int newTime, String newId, int newMaxSpeed, List<String> newItinerary) {
		time = newTime;
		id = newId;
		maxSpeed = newMaxSpeed;
		itinerary = newItinerary;
	}
	
	public void execute(RoadMap roadMap) throws RuntimeException  {
		List<Junction> j_itinerary = new ArrayList<>();
		try {
			for(String j : itinerary){
				j_itinerary.add(roadMap.getJunction(j));
			}
		} catch(RuntimeException e) {
			throw new RuntimeException("Vehicle itinerary references a non-existing"
					+ " object of " + e.getLocalizedMessage());
		}	
		Vehicle newVehicle = new Vehicle(id, maxSpeed, j_itinerary);
		roadMap.addVehicle(newVehicle);
	}

	@Override
	public Map<String, String> describe() {
		Map<String, String> mapa = new HashMap<String, String>();
		
		mapa.put("Time", ""+time);
		mapa.put("Type", "New Vehicle " + id);
			
		return mapa;
	}
	
}
