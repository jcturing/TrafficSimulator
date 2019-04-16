package es.ucm.fdi.model;

import java.util.*;

import es.ucm.fdi.view.Describable;

public class Junction extends SimulatedObject implements Describable {

	
	protected Map<Road, IncomingRoad> inRoads = new LinkedHashMap<>();
	
	public class IncomingRoad{
		protected List<Vehicle> queue = new ArrayList<>();
		protected boolean trafficLight = false;
		
		public boolean isGreen() {
			return trafficLight;
		}
	}
	
	protected List<Road> inRoadsList = new ArrayList<>();
	protected int trafficLightCounter = 0;
	private Map<Junction, Road> outRoads = new LinkedHashMap<>();
	
	
	public Junction(String newId) {
		id = newId;
	}
	
	public void addIncomingRoad(Road r) {
		inRoads.put(r, new IncomingRoad());
		inRoadsList.add(r);
	}
	
	public void addOutcomingRoad(Road r, Junction dest) {
		outRoads.put(dest, r);
	}
	
	public void enter(Vehicle v) {
		inRoads.get(v.getRoad()).queue.add(v);
	}

	public void avanza() {
		if(!inRoadsList.isEmpty()) {
			Road current = inRoadsList.get(trafficLightCounter);
			List<Vehicle> queue = inRoads.get(current).queue;
			if(!queue.isEmpty()){
				if(!queue.get(0).getArrived()) {
					queue.get(0).moveNextRoad();
				}
				queue.remove(0);
			}
			if(trafficLightCounter == 0 && inRoads.get(current).trafficLight == false) {
				inRoads.get(current).trafficLight = true;
			} else {
				switchLights();
			}
		}
	}
	
	protected void switchLights() {
		inRoads.get(inRoadsList.get(trafficLightCounter)).trafficLight = false;
		if((trafficLightCounter == (inRoads.size()-1))) {
			trafficLightCounter = 0;
		} else {
			trafficLightCounter++;
		}
		inRoads.get(inRoadsList.get(trafficLightCounter)).trafficLight = true;
	}
	
	public Road roadTo(Junction j) {
		return outRoads.get(j);
	}
	
	public Map<Road, IncomingRoad> getIncoRoad(){
		return inRoads;
	}

	protected String trafficLightString(IncomingRoad ir) {
		String aux = "";
		
		if(ir.trafficLight) {
			aux = "green";
		} else {
			aux = "red";
		}
		return aux;
	}
	
	protected String queueString(IncomingRoad ir) {
		String aux = "[";
		
		for(Vehicle v : ir.queue) {
			aux = aux + v.getId() + ",";
		}
		
		if(aux.charAt(aux.length()-1) == ',') {
			aux = aux.substring(0, aux.length()-1);
		}
	
		aux = aux + "]";
		return aux;
	}
	
	@Override
	protected void fillReportDetails(Map<String, String> m) {
		String aux = "";
		
		for(Road r : inRoads.keySet()) {
			aux = aux + "(" + String.valueOf(r.getId()) + "," + trafficLightString(inRoads.get(r));
			aux = aux + "," + queueString(inRoads.get(r)) + ")" + ",";
		}
		
		if(aux.length() > 0) {
			aux = aux.substring(0, aux.length()-1);
		}	
		m.put("queues", aux);
	}

	@Override
	protected String getReportHeader() {
		return "junction_report";
	}	

	public Map<String, String> describe() {
		Map<String, String> mapa = new HashMap<String, String>();	
		String red = "";
		String green = "";	
		red = "[ ";
		green = "[ ";
		
		for(Road r : inRoads.keySet()) {
			if(inRoads.get(r).trafficLight) {
				green = green + "("+r.id+ "," + "green," + queueString(inRoads.get(r)) + ")"; 
			} else {
				red = red + "(" + r.id + "," + "red," + queueString(inRoads.get(r)) + ")";
			}
		}
		
		red = red + " ]";
		green = green + " ]";
		mapa.put("ID", id);
		mapa.put("Green", ""+green);
		mapa.put("Red", ""+red);
		return mapa;
	}
	
	public Map<Junction, Road> getOutRoad(){
		return outRoads;
	}
	
}
