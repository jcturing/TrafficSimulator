package es.ucm.fdi.control.eventbuilders;

import es.ucm.fdi.control.Event;
import es.ucm.fdi.control.NewRoadEvent;
import es.ucm.fdi.ini.IniSection;

public class NewRoadEventBuilder implements EventBuilder{

	private final String tagRoad = "new_road";
	private final String typeKey = "type";
	
	public Event parse(IniSection sec) {
		if(!sec.getTag().equals(tagRoad) || sec.getKeysMap().containsKey(typeKey)) return null;
		return new NewRoadEvent(Integer.parseInt(sec.getValue("time")),
				sec.getValue("id"), sec.getValue("src"),
				sec.getValue("dest"), Integer.parseInt(sec.getValue("max_speed")),
				Integer.parseInt(sec.getValue("length")));
	}
	
}
