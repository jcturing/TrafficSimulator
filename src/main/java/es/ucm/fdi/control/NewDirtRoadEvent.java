package es.ucm.fdi.control;

import es.ucm.fdi.model.DirtRoad;
import es.ucm.fdi.model.RoadMap;

public class NewDirtRoadEvent extends NewRoadEvent{
	
	public NewDirtRoadEvent(int newTime, String newId, String newSrc, String newDest,
			int newMaxSpeed, int newLenght, String newType) {
		super(newTime, newId, newSrc, newDest, newMaxSpeed, newLenght);
	}

	public void execute(RoadMap roadMap) throws RuntimeException {
		DirtRoad newDirt = null;
		try {
			newDirt = new DirtRoad(id, roadMap.getJunction(src), roadMap.getJunction(dest),
					maxSpeed, length);
		} catch(RuntimeException e) {
			throw new RuntimeException("Road references a non-existing object of "
						+ e.getLocalizedMessage());
		}
		roadMap.addRoad(newDirt);
	}

}
