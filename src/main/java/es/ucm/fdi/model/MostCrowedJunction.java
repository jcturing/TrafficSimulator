package es.ucm.fdi.model;

import java.util.Map;


public class MostCrowedJunction extends JunctionWithTimeSlice{

		private final String type = "mc";
		private int start = 0;
		
		public MostCrowedJunction(String newId) {
			super(newId);
		}
		
		public void avanza() {
			if(trafficLightCounter == 0 && !inRoads.get(inRoadsList.get(trafficLightCounter)).trafficLight) {
				start = 1;
				int indicador = 0, max = 0;
				for(int j = 0; j < inRoadsList.size(); j++) {
					if(inRoads.get(inRoadsList.get(j)).queue.size() > max) {
						max = inRoads.get(inRoadsList.get(j)).queue.size();
						indicador = j;
					}
				}
				trafficLightCounter = indicador;
				inRoads.get(inRoadsList.get(trafficLightCounter)).trafficLight = true;
			}
			super.avanza();
		}
		
		public void switchLights() {
			IncomingRoadWithTimeSlice i = (IncomingRoadWithTimeSlice) inRoads.get(inRoadsList.get(
					trafficLightCounter));
			
			if(i.timeSlice + start == i.timeUnits + 1) {
				int indicador = 0, max = 0;
				if(start == 1) {
					start = 0;
					for(int j = 0; j < inRoadsList.size(); j++) {
						if((inRoads.get(inRoadsList.get(j)).queue.size() > max)) {
							max = inRoads.get(inRoadsList.get(j)).queue.size();
							indicador = j;
						}
					}
				} else {
					i.trafficLight = false;
					for(int j = 0; j < inRoadsList.size(); j++) {
						if(j != trafficLightCounter && ((inRoads.get(inRoadsList.get(j)).queue.size() > max) 
								||  (inRoads.get(inRoadsList.get(trafficLightCounter)).queue.size() == max))) {
							max = inRoads.get(inRoadsList.get(j)).queue.size();
							indicador = j;
						}
					}
					
				}
				i.timeUnits = 0;
				i = (IncomingRoadWithTimeSlice) inRoads.get(inRoadsList.get(indicador));
				i.trafficLight = true;
				i.timeSlice = Math.max((int)((i.queue.size())/2), 1);
				trafficLightCounter = indicador;
			}
			else {
				i.timeUnits = i.timeUnits + 1;
			}
		}
		
		public void addIncomingRoad(Road r) {
			IncomingRoadWithTimeSlice i = new IncomingRoadWithTimeSlice();
			inRoads.put(r, i);
			inRoadsList.add(r);
		}
		
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