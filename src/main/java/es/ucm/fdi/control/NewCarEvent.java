package es.ucm.fdi.control;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.model.Car;
import es.ucm.fdi.model.Junction;
import es.ucm.fdi.model.RoadMap;

public class NewCarEvent extends NewVehicleEvent{

	private int resistance;
	private double faultProbability;
	private int maxFaultDuration;
	private long seed;
	
	public NewCarEvent(int newTime, String newId, int newMaxSpeed, List<String> newItinerary,
			int newResistance, double newProbability, int faultDuration, long newSeed) {
		super(newTime, newId, newMaxSpeed, newItinerary);
		resistance = newResistance;
		faultProbability = newProbability;
		maxFaultDuration = faultDuration;
		seed = newSeed;
		
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
			Car newCar = new Car(id, maxSpeed, j_itinerary, resistance,
					faultProbability, maxFaultDuration, seed);
			roadMap.addVehicle(newCar);
		}
	
}
