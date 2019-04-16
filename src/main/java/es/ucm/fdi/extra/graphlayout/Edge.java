package es.ucm.fdi.extra.graphlayout;

import java.util.ArrayList;
import java.util.List;

public class Edge {
	private String _id;
	private Node _source;
	private Node _target;
	private int _length;
	private List<Dot> _dots;
	private boolean green;
	
	public Edge(String id, Node source, Node target, int length, boolean b) {
		_source = source;
		_target = target;
		_id = id;
		_length = length;
		_dots = new ArrayList<>();
		green = b;
	}
	
	public void addDot(Dot e) {
		_dots.add(e);
	}
	public String getId() {
		return _id;
	}
	
	public Node getSource() {
		return _source;
	}
	
	public Node getTarget() {
		return _target;
	}

	public int getLength() {
		return _length;
	}
	
	public List<Dot> getDots() {
		return _dots;
	}
	
	public boolean isGreen() {
		return green;
	}
	
}
