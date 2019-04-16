package es.ucm.fdi.control.exceptions;

import es.ucm.fdi.model.Simulator.Listener;
import es.ucm.fdi.model.Simulator.UpdateEvent;

public class BatchModeListener implements Listener{
	
	public void update(UpdateEvent ue, String error) {
		switch(ue.getEvent()) {
		case REGISTERED: break;
		case RESET: break;
		case NEWEVENT: break;
		case ADVANCED: break;
		case ERROR:
			System.err.println("ERROR:" + "\n" + error);
			System.exit(1);
			break;
		}
	}
	
}
