package es.ucm.fdi.view;

import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import es.ucm.fdi.extra.graphlayout.Dot;
import es.ucm.fdi.extra.graphlayout.Edge;
import es.ucm.fdi.extra.graphlayout.Graph;
import es.ucm.fdi.extra.graphlayout.GraphComponent;
import es.ucm.fdi.extra.graphlayout.Node;
import es.ucm.fdi.model.Junction;
import es.ucm.fdi.model.Road;
import es.ucm.fdi.model.RoadMap;
import es.ucm.fdi.model.Vehicle;

public class GraphLayout extends JPanel {
		
		private GraphComponent graphComp;	    
	    private RoadMap roadMap;
	    
		public GraphLayout(RoadMap actualRoadMap) {
			super(new BorderLayout());
			roadMap = actualRoadMap;
			graphComp = new GraphComponent();
			add(graphComp);
		}
		public void setRoadMap(RoadMap roadMap) {
			this.roadMap=roadMap;
			generateGraph();
		}

		protected void generateGraph() {
			Graph g = new Graph();
			Map<Junction, Node> js = new HashMap<>();
			
			for (Junction j : roadMap.getJunctions()) {
				Node n = new Node(j.getId());
				js.put(j, n); // <-- para convertir Junction a Node en aristas
				g.addNode(n);
			}
			
			for (Road r : roadMap.getRoads()) {
				Edge e = new Edge(r.getId(), js.get(r.getJuncOri()), js.get(r.getJuncDest()),
						r.getLongitud(), r.getJuncDest().getIncoRoad().get(r).isGreen());				
				for(Vehicle v: r.getVehicles().innerValues()){
					e.addDot(new Dot(v.getId(), v.getLocation()));
				}				
				g.addEdge(e);
			}	
			graphComp.setGraph(g);
		}

}
