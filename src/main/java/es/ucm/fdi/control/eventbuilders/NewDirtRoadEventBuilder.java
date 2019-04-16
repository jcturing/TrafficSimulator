package es.ucm.fdi.control.eventbuilders;

import es.ucm.fdi.control.Event;
import es.ucm.fdi.control.NewDirtRoadEvent;
import es.ucm.fdi.ini.IniSection;

public class NewDirtRoadEventBuilder implements EventBuilder {

	private final String tagRoad = "new_road";
	private final String typeKey = "type";
	private final String typeDirt = "dirt";
	
	public Event parse(IniSection sec) {
		if(sec.getTag().equals(tagRoad) && sec.getKeysMap().containsKey(typeKey)) {
			if(sec.getValue(typeKey).equals(typeDirt)) {
				return new NewDirtRoadEvent(Integer.parseInt(sec.getValue("time")),
						sec.getValue("id"), sec.getValue("src"),
						sec.getValue("dest"), Integer.parseInt(sec.getValue("max_speed")),
						Integer.parseInt(sec.getValue("length")), sec.getValue("type"));
			}
		}
		return null;
	}

}
