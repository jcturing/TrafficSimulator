package es.ucm.fdi.model;


import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import es.ucm.fdi.util.MultiTreeMap;
import es.ucm.fdi.view.Describable;

public class Road extends SimulatedObject implements Describable{

	private int length;
	private int maxSpeed;
	private MultiTreeMap <Integer, Vehicle> vehicles = new MultiTreeMap<>(Collections.reverseOrder());
	private Junction ori;
	private Junction des;
	
	public Road(String newId, Junction src, Junction dest, int max_speed, int newLength) {
		id = newId;
		maxSpeed = max_speed;
		length = newLength;
		src.addOutcomingRoad(this, dest);
		dest.addIncomingRoad(this);
		ori = src;
		des = dest;
	}

	public void enterVehicle(Vehicle v){
		Integer aux = v.getLocation();
		vehicles.putValue(aux,  v);
	}
	
	public void exitVehicle(Vehicle v) {
		Integer aux = v.getLocation();
		vehicles.removeValue(aux, v);
	}
	
	public int calculateBaseSpeed() {
		return Math.min(maxSpeed,
				(maxSpeed/Math.max((int) vehicles.sizeOfValues(), 1)) +1 );
	}
	
	public int reduceSpeedFactor(int vehicleFaultyTime, int faultyCounter) {
		int aux = 1;
		if(vehicleFaultyTime > 0){
			aux = 2;
			faultyCounter++;
		}
		return aux;
	}
	
	public void avanza(){
		int faultyCounter = 0;
		int baseSpeed = calculateBaseSpeed();
		int factorReduccion = 1;
		MultiTreeMap <Integer, Vehicle> nuevo = new MultiTreeMap<>(Collections.reverseOrder());
		
		for(Vehicle _vehicle : vehicles.innerValues()) {
			if(factorReduccion == 1){
				factorReduccion = reduceSpeedFactor(_vehicle.getFaultyTime(), faultyCounter);
			}
			
			int velVehiculo = baseSpeed/factorReduccion;
			if(_vehicle.getLocation() != length) {
				_vehicle.setSpeedActual(velVehiculo);
				_vehicle.avanza();
			}
			nuevo.putValue(_vehicle.getLocation(), _vehicle);
	
		}
		vehicles = nuevo;
	}
	
	public int getLongitud(){
		return length;
	}

	public int getMaxSpeed() {
		return maxSpeed;
	}
	
	public MultiTreeMap<Integer, Vehicle> getVehicles(){
		return vehicles;
	}
	
	public Junction getJuncOri() {
		return ori;
	}
	
	public Junction getJuncDest() {
		return des;
	}
	
	@Override
	protected void fillReportDetails(Map<String, String> m) {
		String aux = "";
		
		for(Vehicle _vehicle : vehicles.innerValues()) {
			aux = aux + "(" + _vehicle.getId() + "," + String.valueOf(_vehicle.getLocation()) + ")";
			aux = aux + ",";
		}	
		if(aux.length() > 0) {
			aux = aux.substring(0, aux.length()-1);
		}

		m.put("state", aux);
	}


	@Override
	protected String getReportHeader() {
		return "road_report";
	}
	
	@Override
	public Map<String, String> describe() {
		Map<String, String> mapa = new HashMap<String, String>();
		String vehiclesString = new String();
		
		for(Vehicle _vehicle : vehicles.innerValues()) {
			vehiclesString = vehiclesString + _vehicle.getId();
			vehiclesString = vehiclesString + ", ";
		}
		if(vehiclesString.length() > 1) {
			vehiclesString = vehiclesString.substring(0, vehiclesString.length()-2);
		}
		
		mapa.put("ID", id);
		mapa.put("Source", ""+ori.id);
		mapa.put("Target", ""+des.id);
		mapa.put("Length", ""+length);
		mapa.put("Max Speed", ""+maxSpeed);
		mapa.put("Vehicles", "[ " + vehiclesString  + " ]");
		
		return mapa;
	
	}	
	
}
