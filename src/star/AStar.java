package star;

import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import common.Coords;
import common.Node;
import common.SearchPair;

public class AStar {
	private int dist[];					//List of distances from source node
	private Set<Integer> settled;		//List of visited nodes
	private PriorityQueue<Node> pq;		//PQ
	private int V;						//Total nodes
	List<List<Node>> adj;				//Graph structure
	List<Coords> coords;
	
	public AStar(int V, List<Coords> coords) {
		this.V = V;
		this.coords = coords;
		dist = new int[V];
		settled = new HashSet<Integer>();
		pq = new PriorityQueue<Node>(V, new Node());
	}
	
	public SearchPair search(List<List<Node>> adj, int src, int dest) {
		this.adj = adj;
		for (int i = 0; i < V; i++) {
			dist[i] = Integer.MAX_VALUE;
		}
		pq = new PriorityQueue<Node>(V, new Node());
		settled = new HashSet<Integer>();
		
		double heuristic = euclidean(src, dest);
		pq.add(new Node(src, heuristic));
		dist[src] = 0;
		while (!pq.isEmpty()) {
			int currentNode = pq.remove().nodeid;
			if (currentNode == dest) {
				break;
			}
			if (settled.contains(currentNode)) {
				continue;
			}
			settled.add(currentNode);
			expNeighbours(currentNode, dest);
		}
//		System.out.println("Expanded: " + settled.size());
//		System.out.println("Distance: " + dist[dest]);
		return new SearchPair(dist[dest], settled.size());
	}
	
	private void expNeighbours(int currentNode, int dest) {
		//Loop through every neighbour of currentNode
		for (int i = 0; i < adj.get(currentNode).size(); i++) {
			Node neighbour = adj.get(currentNode).get(i);
			
			if (!settled.contains(neighbour.nodeid)) {
				double edgeDist = neighbour.cost;
				double heuristic = euclidean(neighbour.nodeid, dest);
				//System.out.println("Heuristic: " + heuristic + "\t" + currentNode + ":" + dest);
				double newDist = dist[currentNode] + edgeDist;
				
				if (newDist < dist[neighbour.nodeid]) {
					dist[neighbour.nodeid] = (int) newDist;
					neighbour.prevnode = currentNode;
				}
				
				pq.add(new Node(neighbour.nodeid, newDist + heuristic));
			}
		}
	}
	
	/**
	 * Bit shift euclidean
	 */
	private int euclideanBitShift(int currentNode, int endNode) {
		double lon1 = coords.get(currentNode).longitude;
		double lat1 = coords.get(currentNode).latitude;
		double lon2 = coords.get(endNode).longitude;
		double lat2 = coords.get(endNode).latitude;
		
		int lat = (int) (Math.abs(lat1 - lat2) * 111319);
		int lon = (int) (Math.abs(lon1 - lon2) * 83907);
		int min = (lat > lon) ? lon : lat;
		int max = (lat > lon) ? lat : lon;
		int approx = (max * 1007) + (min * 441);
		if (max < (min << 4)) {
			approx -= max * 40;
		}
		return (approx + 512) >> 10;
	}
	
	//Accurate haversine
	private double haversine(int currentNode, int endNode) {
		double lon1 = coords.get(currentNode).longitude;
		double lat1 = coords.get(currentNode).latitude;
		double lon2 = coords.get(endNode).longitude;
		double lat2 = coords.get(endNode).latitude;
		
		double dLat = deg2rad(lat2) - deg2rad(lat1);
		double dLon = deg2rad(lon2) - deg2rad(lon1);
		double a = (Math.sin(dLat/2) * Math.sin(dLat/2)) +
	            Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * 
	            (Math.sin(dLon/2) * Math.sin(dLon/2));
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
	    double d = 6371 * c; // Distance in km
	    return d * 1000; //distance in m
	}
	
	private double deg2rad(double d) {
		return d * (Math.PI/180);
	}
	
	//Modified Euclidean using cos
	private double euclidean(int currentNode, int endNode) {
		double lon1 = coords.get(currentNode).longitude;
		double lat1 = coords.get(currentNode).latitude;
		double lon2 = coords.get(endNode).longitude;
		double lat2 = coords.get(endNode).latitude;
		//System.out.println("Lat + Lon: " + lat1 + " " + lon1);
		double lat = lat1 - lat2;
		double lon = (lon1 - lon2) * Math.cos(lat1);
		return 111319 * (Math.sqrt(lat*lat + lon*lon));
	}
	
	//Inaccurate harversine
	private double euclideanMeters(int currentNode, int endNode) {
		double lon1 = coords.get(currentNode).longitude;
		double lat1 = coords.get(currentNode).latitude;
		double lon2 = coords.get(endNode).longitude;
		double lat2 = coords.get(endNode).latitude;
		//System.out.println("Lat + Lon: " + lat1 + " " + lon1);
		
		double lat = lat1 - lat2;
		double lon = lon1 - lon2;
		double newLat = lat * lat;
		double newLon = lon * lon;
		return (Math.sqrt(newLat + newLon) * 111319);
	}
	
	//Inaccurate harversine
	private double euclidean2(int currentNode, int endNode) {
		double lon1 = coords.get(currentNode).longitude;
		double lat1 = coords.get(currentNode).latitude;
		double lon2 = coords.get(endNode).longitude;
		double lat2 = coords.get(endNode).latitude;
		//System.out.println("Lat + Lon: " + lat1 + " " + lon1);
		
		double lat = lat1 - lat2;
		double lon = lon1 - lon2;
		lat = lat * lat;
		lon = lon * lon;
		return (Math.sqrt(lat + lon));
	}

}
