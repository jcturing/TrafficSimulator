package es.ucm.fdi.control;

import java.util.HashMap;
import java.util.Map;

import es.ucm.fdi.model.Road;
import es.ucm.fdi.model.RoadMap;
import es.ucm.fdi.view.Describable;

public class NewRoadEvent extends Event implements Describable{

	protected String id;
	protected String src;
	protected String dest;
	protected int maxSpeed;
	protected int length;
	
	public NewRoadEvent(int newTime, String newId, String newSrc, String newDest, int newMaxSpeed, int newLength) {
		time = newTime;
		id = newId;
		src = newSrc;
		dest = newDest;
		maxSpeed = newMaxSpeed;
		length = newLength;
	}

	public void execute(RoadMap roadMap) throws RuntimeException {
		Road newRoad = null;
		try {
			newRoad = new Road(id, roadMap.getJunction(src), roadMap.getJunction(dest),
					maxSpeed, length);
		} catch(RuntimeException e) {
			throw new RuntimeException("Road references a non-existing object of " +
						e.getLocalizedMessage());
		}
		roadMap.addRoad(newRoad);
	}
	
	
	@Override
	public Map<String, String> describe() {
		Map<String, String> mapa = new HashMap<String, String>();
		
		mapa.put("Time", ""+time);
		mapa.put("Type", "New Road " + id);
			
		return mapa;
	}
}
