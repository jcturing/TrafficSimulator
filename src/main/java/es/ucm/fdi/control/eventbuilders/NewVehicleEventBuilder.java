package es.ucm.fdi.control.eventbuilders;

import java.util.*;

import es.ucm.fdi.control.Event;
import es.ucm.fdi.control.NewVehicleEvent;
import es.ucm.fdi.ini.IniSection;

public class NewVehicleEventBuilder implements EventBuilder{
	
	private final String tagVehicle = "new_vehicle";
	private final String typeKey = "type";
	
	public Event parse(IniSection sec) {
		if(!sec.getTag().equals(tagVehicle) || sec.getKeysMap().containsKey(typeKey)) return null;		
		List<String> newItinerary = new ArrayList<>();
		String[] stringArray = sec.getValue("itinerary").split(",");
		for(String word : stringArray) {
			newItinerary.add(word);
		}
		return new NewVehicleEvent(Integer.parseInt(sec.getValue("time")), sec.getValue("id"),
				Integer.parseInt(sec.getValue("max_speed")), newItinerary);		
	}
	
}
