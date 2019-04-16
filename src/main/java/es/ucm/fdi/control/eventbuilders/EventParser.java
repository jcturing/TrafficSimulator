package es.ucm.fdi.control.eventbuilders;

import es.ucm.fdi.control.Event;
import es.ucm.fdi.control.exceptions.IllegalArgumentsException;
import es.ucm.fdi.ini.IniSection;

public class EventParser {

	private static final EventBuilder[] availableEvents = {
			new NewJunctionEventBuilder(),
			new NewRoadEventBuilder(), new NewVehicleEventBuilder(),
			new NewVehicleFaultyEventBuilder(),
			new NewCarEventBuilder(), new NewBikeEventBuilder(),
			new NewLanesRoadEventBuilder(),
			new NewDirtRoadEventBuilder(),
			new NewMostCrowedJunctionEventBuilder(),
			new NewRoundRobinJunctionEventBuilder()
		};
	
	public Event parseEvents(IniSection sec) throws RuntimeException {
		Event result = null;
		
		for(EventBuilder _eventsparser : availableEvents) {
			if((result = _eventsparser.parse(sec)) != null) {
				if(Integer.parseInt(sec.getValue("time")) < 0) {
					throw new IllegalArgumentException("Value of time: " + sec.getValue("time") +
							" must be a positive number for event" + "\n" + sec);
				}
				
				if(!sec.getTag().equals("make_vehicle_faulty")) {
					if(!_eventsparser.isValidId(sec.getValue("id"))) {
						throw new IllegalArgumentsException("Id: " + sec.getValue("id") + 
								" not valid for event" + "\n" + sec);
					}
				}
				
				if(sec.getTag().equals("new_vehicle")) {
					if(Integer.parseInt(sec.getValue("max_speed")) < 0) {
						throw new IllegalArgumentException("Value of max_speed: " + sec.getValue("max_speed") +
								" must be a positive number for event" + "\n" + sec);
					}
				} else if(sec.getTag().equals("new_road")) {
					if(Integer.parseInt(sec.getValue("max_speed")) < 0) {
						throw new IllegalArgumentException("Value of max_speed: " + sec.getValue("max_speed") +
								" must be a positive number for event" + "\n" + sec);
					}
					if(Integer.parseInt(sec.getValue("length")) < 0) {
						throw new IllegalArgumentException("Value of length: " + sec.getValue("length") +
								" must be a positive number for event" + "\n" + sec);
					}
				}
				break;
			}
		}
		
		if(result == null) {
			throw new IllegalArgumentsException("Event not found" + "\n" + sec);
		}

		return result;
	}
	
}
