package es.ucm.fdi.control.eventbuilders;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.control.Event;
import es.ucm.fdi.control.NewCarEvent;
import es.ucm.fdi.ini.IniSection;

public class NewCarEventBuilder implements EventBuilder {

	private final String tagVehicle = "new_vehicle";
	private final String typeKey = "type";
	private final String typeCar = "car";
	
	public Event parse(IniSection sec) {
		if(sec.getTag().equals(tagVehicle) && sec.getKeysMap().containsKey(typeKey)) {
			if(sec.getValue(typeKey).equals(typeCar)) {
				if(Integer.parseInt(sec.getValue("resistance")) < 0) {
					throw new IllegalArgumentException("Value of resistance: " + sec.getValue("resistance") +
							" must be a positive number for event" + "\n" + sec);
				} else if(Double.parseDouble(sec.getValue("fault_probability")) < 0 || 
						Double.parseDouble(sec.getValue("fault_probability")) > 1) {
					throw new IllegalArgumentException("Value of fault_probability: "
								+ sec.getValue("fault_probability") +
								" must be a positive number for event" + "\n" + sec);
				} else if(Integer.parseInt(sec.getValue("max_fault_duration")) < 0) {
					throw new IllegalArgumentException("Value of max_fault_duration: " +
											sec.getValue("max_fault_duration") +
							" must be a positive number for event" + "\n" + sec);
				} else if(Long.parseLong(sec.getValue("seed")) < 0) {
					throw new IllegalArgumentException("Value of seed: " + sec.getValue("seed") +
							" must be a positive number for event" + "\n" + sec);
				}
				
				long newSeed;
				List<String> newItinerary = new ArrayList<>();
				
				String[] stringArray = sec.getValue("itinerary").split(",");
				for(String word : stringArray) {
					newItinerary.add(word);
				}
				
				if(sec.getKeysMap().containsKey("seed")) {
					newSeed = Long.parseLong(sec.getValue("seed"));
				} else {
					newSeed = System.currentTimeMillis();
				}
				
				return new NewCarEvent(Integer.parseInt(sec.getValue("time")),
						sec.getValue("id"), Integer.parseInt(sec.getValue("max_speed")),
						newItinerary, Integer.parseInt(sec.getValue("resistance")),
						Double.parseDouble(sec.getValue("fault_probability")),
						Integer.parseInt(sec.getValue("max_fault_duration")),
						newSeed);		
			}
		}
		return null;
	}

}
