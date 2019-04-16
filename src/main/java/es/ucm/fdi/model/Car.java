package es.ucm.fdi.model;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class Car extends Vehicle {

	private final String type = "car";
	
	private int resistance;
	private double faultProbability;
	private int maxFaultDuration;
	private long seed;
	private int lastFaulty = 0;
	private Random randomNum;
	
	public Car(String newId, int max_speed, List<Junction> newItinerary, int newResistance,
			double new_fault_probability, int new_max_fault_duration, long newSeed) {
		super(newId, max_speed, newItinerary);
		resistance = newResistance;
		faultProbability = new_fault_probability;
		maxFaultDuration = new_max_fault_duration;
		seed = newSeed;
		randomNum = new Random(seed);
	}
	
	public void avanza() {
		int aux_kilometrage = getKilometrage();
		if(getFaultyTime() == 0 && lastFaulty > resistance && randomNum.nextDouble() < faultProbability) {
			setFaultyTime(randomNum.nextInt(maxFaultDuration) + 1);
			lastFaulty = 0;
		}	
		super.avanza();
		lastFaulty = lastFaulty + getKilometrage() - aux_kilometrage;
	}

	protected void fillReportDetails(Map<String, String> m) {
		m.put("type", type);
		super.fillReportDetails(m);
	}
	
}
