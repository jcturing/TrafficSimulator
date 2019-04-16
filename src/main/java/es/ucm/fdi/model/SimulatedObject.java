package es.ucm.fdi.model;

import java.util.Map;

public abstract class SimulatedObject {
	protected String id;
	
	public String getId() {
		return id;
	}
	
	protected abstract void fillReportDetails(Map<String, String> out);
	protected abstract String getReportHeader();
	
	public void report(int time, Map<String, String> m) {
		m.clear();
		m.put("", getReportHeader());
		m.put("id", id);
		m.put("time", String.valueOf(time));
		fillReportDetails(m);
	}
	
}