package es.ucm.fdi.control.eventbuilders;

import es.ucm.fdi.control.Event;
import es.ucm.fdi.control.NewMostCrowedJunctionEvent;
import es.ucm.fdi.ini.IniSection;

public class NewMostCrowedJunctionEventBuilder implements EventBuilder{

	private final String tagJunction = "new_junction";
	private final String typeKey = "type";
	private final String typeMostCrowed = "mc";
	
	public Event parse(IniSection sec) {
		if(sec.getTag().equals(tagJunction) && sec.getKeysMap().containsKey(typeKey)) {
			if(sec.getValue(typeKey).equals(typeMostCrowed)) {				
				return new NewMostCrowedJunctionEvent(Integer.parseInt(sec.getValue("time")),
						sec.getValue("id"));	
			}
		}
		return null;
	}	

}