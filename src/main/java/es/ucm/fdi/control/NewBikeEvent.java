package es.ucm.fdi.control;

import java.util.*;

import es.ucm.fdi.model.Bike;
import es.ucm.fdi.model.Junction;
import es.ucm.fdi.model.RoadMap;

public class NewBikeEvent extends NewVehicleEvent{
	
	public NewBikeEvent(int newTime, String newId, int newMaxSpeed, List<String> newItinerary) {
		super(newTime, newId, newMaxSpeed, newItinerary);
	}

	public void execute(RoadMap roadMap) throws RuntimeException {
		List<Junction> j_itinerary = new ArrayList<>();
		try {
			for(String j : itinerary){
				j_itinerary.add(roadMap.getJunction(j));
			}
		} catch(RuntimeException e) {
			throw new RuntimeException("Vehicle itinerary references a non-existing"
					+ " object of " + e.getLocalizedMessage());
		}
		Bike newBike = new Bike(id, maxSpeed, j_itinerary);
		roadMap.addVehicle(newBike);
	}
	
}
