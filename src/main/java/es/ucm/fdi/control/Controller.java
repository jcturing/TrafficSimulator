package es.ucm.fdi.control;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import es.ucm.fdi.control.eventbuilders.EventParser;
import es.ucm.fdi.ini.Ini;
import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.model.RoadMap;
import es.ucm.fdi.model.Simulator;
import es.ucm.fdi.model.Simulator.EventType;
import es.ucm.fdi.util.MultiTreeMap;

public class Controller {
	
	private  Simulator simulator;
	private  InputStream _input;
	private  OutputStream _output;
	private EventParser eventParser = new EventParser();
	
	public Controller(Simulator newSimulator, InputStream input, OutputStream output){
		simulator = newSimulator;
		_input = input;
		_output = output;
	}
	
	public Simulator getSimulator() {
		return simulator;
	}
	
	public InputStream getInput() {
		return _input;
	}
	
	public void run() {
		try {
			simulator.run(10, _output);
		} catch(RuntimeException e) {
			simulator.fireUpdateEvent(EventType.ERROR, e.getLocalizedMessage());
		}
	}
	
	public void run(int steps) {
		try {
			simulator.run(steps, _output);
		} catch(RuntimeException e) {
			simulator.fireUpdateEvent(EventType.ERROR, e.getLocalizedMessage());
		}
	}
	
	public void loadEvents(InputStream input) {
		try {
			Ini ini = new Ini(input);
			for(IniSection _section : ini.getSections()) {
				simulator.addEvent(eventParser.parseEvents(_section));
			}
		} catch(RuntimeException | IOException e) {
			simulator.fireUpdateEvent(EventType.ERROR, e.getLocalizedMessage());
		}
	}
	
	public MultiTreeMap<Integer, Event> getEventsQueue(){
		return simulator.getQueue();
	}
	
	public RoadMap getRoadMap() {
		return simulator.getRoadMap();
	}
	
	
	public void setOut(OutputStream newOutput) {
		_output = newOutput;
	}
	
	public void setNewSimulator() {
		simulator.reset();

	}

}
