package es.ucm.fdi.model;

import java.util.*;

import es.ucm.fdi.control.exceptions.SimulatorException;

public class RoadMap {

	//Mapa de ids y objetos
	private Map<String, SimulatedObject> simObjects = new TreeMap<>();
	
	// listados reales
	private List<Junction> junctions = new ArrayList<>();
	private List<Road> roads = new ArrayList<>();
	private List<Vehicle> vehicles = new ArrayList<>();
	
	public Vehicle getVehicle(String id) throws RuntimeException {
		if(simObjects.get(id) instanceof Vehicle) {
			return (Vehicle) simObjects.get(id);
		} else {
			throw new SimulatorException("Id: " + id + "\n");
		}
	}
	
	public Road getRoad(String id)  throws RuntimeException {
		if(simObjects.get(id) instanceof Road) {
			return (Road) simObjects.get(id);
		} else {
			throw new SimulatorException("Id: " + id + "\n");
		}
	}
	
	public Junction getJunction(String id)  throws RuntimeException {
		if(simObjects.get(id) instanceof Junction) {
			return (Junction) simObjects.get(id);
		} else {
			throw new SimulatorException("Id: " + id + "\n");
		}
	}
	
	public SimulatedObject getSimulatedObject(String id) {
		return simObjects.get(id);
	}
	
	public List<Vehicle> getVehicles(){
		return vehicles;
	}
	
	public List<Road> getRoads(){
		return roads;
	}
	
	public List<Junction> getJunctions(){
		return junctions;
	}
	
	public void addJunction(Junction j) throws RuntimeException {
		if(simObjects.containsKey(j.getId())) {
			throw new SimulatorException("Id " + j.getId() +
					" repeated" + "\n");
		} else {
		junctions.add(j);
		simObjects.put(j.getId(), j);
		}
	}
	
	public void addRoad(Road r) throws RuntimeException {
		if(simObjects.containsKey(r.getId())) {
			throw new SimulatorException("Id: " + r.getId() +
					" repeated" + "\n");
		} else {
		roads.add(r);
		simObjects.put(r.getId(), r);
		}
	}
	
	public void addVehicle(Vehicle v) throws RuntimeException {
		if(simObjects.containsKey(v.getId())) {
			throw new SimulatorException("Id: " + v.getId() +
					" repeated" + "\n");
		} else {
		vehicles.add(v);
		simObjects.put(v.getId(), v);
		}
	}
	
}
