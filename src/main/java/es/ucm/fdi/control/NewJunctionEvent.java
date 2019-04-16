package es.ucm.fdi.control;

import java.util.HashMap;
import java.util.Map;

import es.ucm.fdi.model.Junction;
import es.ucm.fdi.model.RoadMap;
import es.ucm.fdi.view.Describable;

public class NewJunctionEvent extends Event implements Describable{
	
protected String id;
	
	public NewJunctionEvent(int newTime, String newId) {
		time = newTime;
		id = newId;
	}
	
	public void execute(RoadMap roadMap) throws RuntimeException  {
		Junction newJunction = new Junction(id);
		roadMap.addJunction(newJunction);
	}

	@Override
	public Map<String, String> describe() {
		Map<String, String> mapa = new HashMap<String, String>();
		
		mapa.put("Time", String.valueOf(time));
		mapa.put("Type", "New Junction " + id);
			
		return mapa;
	}
	
}
