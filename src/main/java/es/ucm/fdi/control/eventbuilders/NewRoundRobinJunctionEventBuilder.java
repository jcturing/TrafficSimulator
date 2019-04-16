package es.ucm.fdi.control.eventbuilders;

import es.ucm.fdi.control.Event;
import es.ucm.fdi.control.NewRoundRobinJunctionEvent;
import es.ucm.fdi.ini.IniSection;

public class NewRoundRobinJunctionEventBuilder implements EventBuilder {

	private final String tagJunction = "new_junction";
	private final String typeKey = "type";
	private final String typeRound = "rr";
	
	public Event parse(IniSection sec) {
		if(sec.getTag().equals(tagJunction) && sec.getKeysMap().containsKey(typeKey)) {
			if(sec.getValue(typeKey).equals(typeRound)) {
				if(Integer.parseInt(sec.getValue("max_time_slice")) < 0) {
					throw new IllegalArgumentException("Value of max_time_slice: " + sec.getValue("max_time_slice") +
							" must be a positive number for event" + "\n" + sec);
				} else if(Integer.parseInt(sec.getValue("min_time_slice")) < 0) {
					throw new IllegalArgumentException("Value of min_time_slice: " + sec.getValue("min_time_slice") +
							" must be a positive number for event" + "\n" + sec);
				}		
				return new NewRoundRobinJunctionEvent(Integer.parseInt(sec.getValue("time")), sec.getValue("id"),
						Integer.parseInt(sec.getValue("max_time_slice")),
						Integer.parseInt(sec.getValue("min_time_slice")));	
			}
		}
		return null;
	}

}