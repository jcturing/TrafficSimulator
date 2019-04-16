package es.ucm.fdi.control;

import es.ucm.fdi.model.RoadMap;
import es.ucm.fdi.model.RoundRobinJunction;

public class NewRoundRobinJunctionEvent extends NewJunctionEvent{
	
	private int maxTimeSlice;
	private int minTimeSlice;
	
	public NewRoundRobinJunctionEvent(int newTime, String newId, int max_time, int min_time) {
		super(newTime, newId);
		maxTimeSlice = max_time;
		minTimeSlice = min_time;
	}
	
	public void execute(RoadMap roadMap) throws RuntimeException {
		RoundRobinJunction newRoundRobin = new RoundRobinJunction(id, maxTimeSlice, minTimeSlice);
		roadMap.addJunction(newRoundRobin);
	}
	
}
