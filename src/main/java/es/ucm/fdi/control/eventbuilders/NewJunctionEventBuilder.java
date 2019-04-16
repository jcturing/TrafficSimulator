package es.ucm.fdi.control.eventbuilders;

import es.ucm.fdi.control.Event;
import es.ucm.fdi.control.NewJunctionEvent;
import es.ucm.fdi.ini.IniSection;

public class NewJunctionEventBuilder implements EventBuilder {

	private final String tagJunction = "new_junction";
	private final String typeKey = "type";
	
	public Event parse(IniSection sec) {
		if(!sec.getTag().equals(tagJunction) || sec.getKeysMap().containsKey(typeKey)) return null;
		return new NewJunctionEvent(Integer.parseInt(sec.getValue("time")), sec.getValue("id"));
	}

}