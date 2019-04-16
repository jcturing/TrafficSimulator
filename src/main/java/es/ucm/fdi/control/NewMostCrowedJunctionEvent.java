package es.ucm.fdi.control;

import es.ucm.fdi.model.MostCrowedJunction;
import es.ucm.fdi.model.RoadMap;


public class NewMostCrowedJunctionEvent extends NewJunctionEvent {



	public NewMostCrowedJunctionEvent(int newTime, String newId) {
		super(newTime, newId);
	}

	public void execute(RoadMap roadMap) throws RuntimeException{
		MostCrowedJunction newMostCrowed = new MostCrowedJunction(id);
		roadMap.addJunction(newMostCrowed);
	}

}
