package es.ucm.fdi.control.eventbuilders;

import es.ucm.fdi.control.Event;
import es.ucm.fdi.ini.IniSection;

public interface EventBuilder {
	
	public Event parse(IniSection sec);
	
	public default boolean isValidId(String id) throws RuntimeException {
		return id.matches("[a-zA-Z0-9_]+");
	}
	
}
