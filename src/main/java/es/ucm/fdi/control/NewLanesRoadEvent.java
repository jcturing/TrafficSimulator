package es.ucm.fdi.control;

import es.ucm.fdi.model.LanesRoad;
import es.ucm.fdi.model.RoadMap;

public class NewLanesRoadEvent extends NewRoadEvent{
	
private int lanes;
	
	public NewLanesRoadEvent(int newTime, String newId, String newSrc, String newDest, int newMaxSpeed,
			int newLength, int newLanes) {
		super(newTime, newId, newSrc, newDest, newMaxSpeed, newLength);
		lanes = newLanes;	
	}
	
	public void execute(RoadMap roadMap) throws RuntimeException  {
		LanesRoad newLanes = null;
		try {
			newLanes = new LanesRoad(id, roadMap.getJunction(src), roadMap.getJunction(dest),
					maxSpeed, length, lanes);
		} catch(RuntimeException e) {
			throw new RuntimeException("Road references a non-existing object of " +
						e.getLocalizedMessage());
		}
		roadMap.addRoad(newLanes);
	}
	
}
