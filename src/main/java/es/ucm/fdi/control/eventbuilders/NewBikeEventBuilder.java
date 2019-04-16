package es.ucm.fdi.control.eventbuilders;

import java.util.*;

import es.ucm.fdi.control.Event;
import es.ucm.fdi.control.NewBikeEvent;
import es.ucm.fdi.ini.IniSection;

public class NewBikeEventBuilder implements EventBuilder{

	private final String tagVehicle = "new_vehicle";
	private final String typeKey = "type";
	private final String typeBike = "bike";
	
	public Event parse(IniSection sec) throws RuntimeException {
		if(sec.getTag().equals(tagVehicle) && sec.getKeysMap().containsKey(typeKey)) {
			if(sec.getValue(typeKey).equals(typeBike)) {
				List<String> newItinerary = new ArrayList<>();
				String[] stringArray = sec.getValue("itinerary").split(",");
				for(String word : stringArray) {
					newItinerary.add(word);
				}				
				return new NewBikeEvent(Integer.parseInt(sec.getValue("time")),
						sec.getValue("id"), Integer.parseInt(sec.getValue("max_speed")),
						newItinerary);		
			}
		}
		return null;
	}

}
