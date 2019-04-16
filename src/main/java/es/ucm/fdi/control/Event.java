package es.ucm.fdi.control;

import es.ucm.fdi.model.RoadMap;
import es.ucm.fdi.view.Describable;

public abstract class Event implements Comparable<Event>, Describable {

	protected int time;

	public abstract void execute(RoadMap roadMap);
	
	public int getTime() {
		return this.time;
	}
	
	@Override
	public int compareTo(Event e) {
		return this.time - e.getTime();
	}
	
}
