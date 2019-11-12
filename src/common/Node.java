package common;

import java.util.*;

public class Node implements Comparator<Node>{
	public int nodeid;
	public double cost;
	public int prevnode;
	public int lon;
	public int lat;
	public int priority;
	
	public Node() {}
	
	public Node(int nodeid, double cost) {
		this.nodeid = nodeid;
		this.cost = cost;
		prevnode = -1;
	}
	
	public Node(int nodeid, double cost, int priority) {
		this.nodeid = nodeid;
		this.cost = cost;
		this.priority = priority;
		prevnode = -1;
	}
	
	public Node(int nodeid, double cost, int lon, int lat) {
		this.nodeid = nodeid;
		this.cost = cost;
		this.lon = lon;
		this.lat = lat;
		prevnode = -1;
	}

//	@Override
//	public int compare(Node node1, Node node2) {
//		if (node1.cost < node2.cost) {
//			return -1;
//		}
//		if (node1.cost > node2.cost) {
//			return 1;
//		}
//		return 0;
//	}
	@Override
	public int compare(Node node1, Node node2) {
		return Double.compare(node1.cost, node2.cost);
	}

	@Override
	public String toString() {
		return "[nodeid="
				+ nodeid + ", cost=" + cost + ", prevnode=" + prevnode + "]";
	}
}
