package es.ucm.fdi.control.eventbuilders;

import es.ucm.fdi.control.Event;
import es.ucm.fdi.control.NewLanesRoadEvent;
import es.ucm.fdi.ini.IniSection;

public class NewLanesRoadEventBuilder implements EventBuilder{

	private final String tagRoad = "new_road";
	private final String typeKey = "type";
	private final String typeLanes = "lanes";
	
	public Event parse(IniSection sec) {
		if(sec.getTag().equals(tagRoad) && sec.getKeysMap().containsKey(typeKey)) {
			if(sec.getValue(typeKey).equals(typeLanes)) {
			if(Integer.parseInt(sec.getValue("lanes")) < 0) {
					throw new IllegalArgumentException("Value of lanes: " + sec.getValue("lanes") +
							" must be a positive number for event" + "\n" + sec);
				}	
			return new NewLanesRoadEvent(Integer.parseInt(sec.getValue("time")),
					sec.getValue("id"), sec.getValue("src"),
					sec.getValue("dest"), Integer.parseInt(sec.getValue("max_speed")),
					Integer.parseInt(sec.getValue("length")), Integer.parseInt(sec.getValue("lanes")));	
			}
		}
		return null;
	}
	
}
