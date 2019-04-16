package es.ucm.fdi.model;

import java.util.Map;

public class RoundRobinJunction extends JunctionWithTimeSlice{

	private int maxTimeSlice;
	private int minTimeSlice;
	private final String type = "rr";
	
	public RoundRobinJunction(String newId, int max_time, int min_time) {
		super(newId);
		maxTimeSlice = max_time;
		minTimeSlice = min_time;
	}
	
	public void avanza() {
		IncomingRoadWithTimeSlice i = (IncomingRoadWithTimeSlice) inRoads.get(
				inRoadsList.get(trafficLightCounter));
			if(!inRoadsList.isEmpty()) {	
				if(!i.queue.isEmpty()){
					i.used = true;
					i.fullyUsedCounter = i.fullyUsedCounter + 1;
					if(i.fullyUsedCounter == i.timeSlice) {
						i.fullyUsed = true;
					}
				}
			}
		super.avanza();
	}
	
	public void switchLights() {
		IncomingRoadWithTimeSlice i = (IncomingRoadWithTimeSlice) inRoads.get(
				inRoadsList.get(trafficLightCounter));
			if(i.timeUnits + 1 == i.timeSlice){
				if(i.fullyUsed) i.timeSlice = Math.min(i.timeSlice + 1, maxTimeSlice);
				else if(!i.used) i.timeSlice = Math.max(i.timeSlice - 1, minTimeSlice);
				i.timeUnits = 0;
				i.fullyUsed = false;
				i.used = false;
				i.fullyUsedCounter = 0;
				super.switchLights();
			}
			else {
				i.timeUnits = i.timeUnits + 1;
			}
	}
	
	public void addIncomingRoad(Road r) {
		IncomingRoadWithTimeSlice i = new IncomingRoadWithTimeSlice();
		i.timeSlice = maxTimeSlice;
		inRoads.put(r, i);
		inRoadsList.add(r);
	}
	
	@Override
	protected void fillReportDetails(Map<String, String> m) {
		String aux = "";
		
		for(Road r : inRoads.keySet()) {
			aux = aux + "(" + String.valueOf(r.getId()) + "," + trafficLightString(inRoads.get(r));
			if(inRoads.get(r).trafficLight) {
				IncomingRoadWithTimeSlice i = (IncomingRoadWithTimeSlice) inRoads.get(r);
				aux = aux + ":" + (i.timeSlice - i.timeUnits);
			}
			aux = aux + "," + queueString(inRoads.get(r)) + ")" + ",";
		}
		
		if(aux.length() > 0) {
			aux = aux.substring(0, aux.length()-1);
		}
		
		m.put("queues", aux);
		m.put("type", type);
	}
	
}