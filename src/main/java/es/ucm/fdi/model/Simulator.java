package es.ucm.fdi.model;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

import javax.swing.SwingUtilities;

import es.ucm.fdi.control.Event;
import es.ucm.fdi.control.exceptions.SimulatorException;
import es.ucm.fdi.ini.Ini;
import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.util.MultiTreeMap;

public class Simulator {

	private RoadMap roadMap = new RoadMap();
	private MultiTreeMap<Integer, Event> eventsQueue = new MultiTreeMap<>();
	private int timer = 0;
	private List<Listener> listeners = new ArrayList<>();
	
	public void addEvent(Event e) {
		if(e.getTime() >= timer) {
			eventsQueue.putValue(e.getTime(), e);
			fireUpdateEvent(EventType.NEWEVENT,null);
		}
	}
	
	public void run(int pasos, OutputStream out) throws RuntimeException { 
		//El fichero de salida puede ser la consola (System.out) o un fichero (FileOutputStream)
		int limiteTiempo = this.timer + pasos - 1;
		while(this.timer <= limiteTiempo) {
			try {
				//1. Ejecutar los eventos correspondientes a ese tiempo
				if(eventsQueue.containsKey(this.timer)) {
					for(Event e : eventsQueue.get(this.timer)) {
					e.execute(roadMap);
					}
					eventsQueue.remove(this.timer);
				}
				
				//2. Invocar metodo avanzar de las carreteras
				for(Road _road : roadMap.getRoads()) {
					_road.avanza();
				}
				
				//3. Invocar metodo avanzar de los cruces
				for(Junction _junction : roadMap.getJunctions()) {
					_junction.avanza();
				}
			
				//4. Aumentar contador
				this.timer++;
				
				//5. Escribir informe en OutputStream en caso de que no sea null
					writeObjects(roadMap.getJunctions(), out);
					writeObjects(roadMap.getRoads(), out);
					writeObjects(roadMap.getVehicles(), out);
				}catch(IOException | RuntimeException e) {
					throw new SimulatorException ("Error in step " + this.timer + "\n" + e.getLocalizedMessage());
				}
				fireUpdateEvent(EventType.ADVANCED,null);
			}
	}
	
	public void writeObjects(List<? extends SimulatedObject> objectList, OutputStream out) throws IOException {
		Map<String, String> reportMap = new LinkedHashMap<>();
		Ini ini = new Ini();
		for(SimulatedObject o : objectList) {
			o.report(this.timer, reportMap);
			ini.addsection(writeReport(reportMap, out));	
		}
		if(out != null) {
			try {
				ini.store(out);
			} catch(IOException e1) {
				throw new IOException(" writing " + "\n" + ini);
			}
		}
	}
	
	private IniSection writeReport(Map<String, String> reportMap, OutputStream out) {
		IniSection sec = new IniSection(reportMap.get(""));
		for(Map.Entry<String, String> e : reportMap.entrySet()) {
			if(!e.getKey().equals("")) {
				sec.setValue(e.getKey(), e.getValue());
			}
		}
		return sec;
	}
	
	public class UpdateEvent {//Clase interna que nos permitirá actualizar la interfaz grafica a partir de los datos actuales del simulador
		
		private EventType tipe;
		
		public UpdateEvent(EventType t) {
			tipe = t;
		}
		
		public EventType getEvent() {
			return tipe;
		}
		
		public List<Vehicle> getVehicles() {
			return roadMap.getVehicles();
		}
		
		public List<Road> getRoads(){
			return roadMap.getRoads();
		}
		
		public List<Junction> getJunctions(){
			return roadMap.getJunctions();
		}
		
		public List<Event> getEvenQueue() {
			return eventsQueue.valuesList();
		}
		
		public RoadMap getRoadMap() {
			return roadMap;
		}
		
		public int getCurrentTime() {
			return timer;
		}
	}
	
	
	public interface Listener {//Interfaz que implementamos en MainWindow que sirve para actualizar la interfaz
		void update(UpdateEvent ue, String error);
	}
	
	public enum EventType {//Enumerado de los distintos tipos que existen de actualizaciones
		REGISTERED, RESET, NEWEVENT, ADVANCED, ERROR
	}
	
	public void addSimulatorListener(Listener l) {//Funcion que añade un nuevo listener a la lista
		listeners.add(l);
		UpdateEvent ue = new UpdateEvent(EventType.REGISTERED);
		SwingUtilities.invokeLater(()->l.update(ue,null));
	}
	
	public void removeListener(Listener l) {
		listeners.remove(l);
	}
	
	public void fireUpdateEvent(EventType type, String error) {//Funcion que crea un tipo de update concreto y lo utiliza en cada listener
		UpdateEvent ue = new UpdateEvent(type);
		for(int i = 0; i < listeners.size();i++) {
			listeners.get(i).update(ue, error);
		}
	}
	
	public MultiTreeMap<Integer, Event> getQueue(){
		return eventsQueue;
	}
	
	public RoadMap getRoadMap() {
		return roadMap;
	}
	
	public void reset() {//Funcion que resetea el simulador
		roadMap = new RoadMap();
		eventsQueue = new MultiTreeMap<>();
		timer = 0;
		fireUpdateEvent(EventType.RESET,null);
	}
	
}
