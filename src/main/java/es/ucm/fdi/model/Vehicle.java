package es.ucm.fdi.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.ucm.fdi.view.Describable;

public class Vehicle extends SimulatedObject implements Describable{

	private int speedMax;
	private int speedActual = 0;
	private Road actualRoad;
	private int local = 0;
	private int faultyTime = 0;
	private boolean arrived = false;
	private List<Junction> itinerary;
	private int localRoad = 1;
	private int kilometrage = 0;
	
	
	public Vehicle(String newId, int max_speed, List<Junction> newItinerary) {
		id = newId;
		speedMax = max_speed;
		itinerary = newItinerary;
		actualRoad = itinerary.get(0).roadTo(itinerary.get(1));
		actualRoad.enterVehicle(this);
	}
	
	
	
	public void avanza(){
	
		if(faultyTime > 0){
			faultyTime--;
		} else{
			kilometrage = kilometrage - local;
			local = local + speedActual;

			if(local >= actualRoad.getLongitud()){
				local = actualRoad.getLongitud();
				itinerary.get(localRoad).enter(this);				
				speedActual = 0;
			}
			kilometrage = kilometrage + local;
		}
	}
	
	
	public void moveNextRoad(){	
		actualRoad.exitVehicle(this);
		if(localRoad == itinerary.size()-1){
			arrived = true;
			speedActual = 0;
		} else {
			actualRoad = itinerary.get(localRoad).roadTo(itinerary.get(localRoad+1));
			localRoad++;
			local = 0;
			actualRoad.enterVehicle(this);
			
		}	
	}
	
	public void setFaultyTime(int averia){
		faultyTime = faultyTime + averia;
		if(faultyTime > 0) speedActual = 0;
	}
	
	public void setSpeedActual(int vel){
		if(faultyTime == 0) {
			speedActual = vel;
			if(vel > speedMax){
				speedActual = speedMax;
			}
		}
	}
	
	public int getFaultyTime() {
		return faultyTime;
	}
	
	public int getLocation() {
		return local;
	}
	
	public int getKilometrage() {
		return kilometrage;
	}

	public Road getRoad() {
		return actualRoad;
	}
	
	public boolean getArrived() {
		return arrived;
	}

	public int getSpeedActual() {
		return speedActual;
	}
	
	public int getSpeedMax() {
		return speedMax;
	}
	
	@Override
	protected void fillReportDetails(Map<String, String> m) {
		m.put("speed", String.valueOf(speedActual));
		m.put("kilometrage", String.valueOf(kilometrage));
		m.put("faulty", String.valueOf(faultyTime));
		if(!arrived) {
			m.put("location", "(" + actualRoad.getId() + "," + String.valueOf(local) + ")");
		} else {
			m.put("location", "arrived");
		}
		
	}

	@Override
	protected String getReportHeader() {
		return "vehicle_report";
	}	
	
	@Override
	public Map<String, String> describe() {
		Map<String, String> mapa = new HashMap<String, String>();
		String itineraryString = new String();
		
		for(Junction j: itinerary) {
			itineraryString  = itineraryString + j.id + ", ";
		}
		itineraryString = itineraryString.substring(0, itineraryString.length()-2);
		
		mapa.put("ID", id);
		mapa.put("Road", ""+actualRoad.id);
		mapa.put("Location", ""+local);
		mapa.put("Speed", ""+speedActual);
		mapa.put("Km", ""+kilometrage);
		mapa.put("Faulty Units", ""+faultyTime);
		mapa.put("Itinerary", "[ "+itineraryString +" ]");
		
		return mapa;
	}	
	
}
