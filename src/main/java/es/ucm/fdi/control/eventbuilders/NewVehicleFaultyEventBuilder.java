package es.ucm.fdi.control.eventbuilders;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.control.Event;
import es.ucm.fdi.control.MakeVehicleFaultyEvent;
import es.ucm.fdi.ini.IniSection;

public class NewVehicleFaultyEventBuilder implements EventBuilder{
	
	private final String tagFaulty = "make_vehicle_faulty";
	
	public Event parse(IniSection sec) {
		if(!sec.getTag().equals(tagFaulty)) return null;
		if(Integer.parseInt(sec.getValue("duration")) < 0) {
			throw new IllegalArgumentException("Value of duration: " + sec.getValue("duration") +
					" must be a positive number for event" + "\n" + sec);
		}
		
		List<String> vehiclesFaulty = new ArrayList<>();
		String[] stringArray = sec.getValue("vehicles").split(",");
		for(String word : stringArray) {
			vehiclesFaulty.add(word);
		}
		return new MakeVehicleFaultyEvent(Integer.parseInt(sec.getValue("time")),
				Integer.parseInt(sec.getValue("duration")), vehiclesFaulty);		
	}

}
