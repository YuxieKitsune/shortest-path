package ch;

import java.util.List;

import common.Node;

public class CHContainer {

	public int V;
	public List<List<Node>> adj;
	public List<List<Node>> adjR;
	public int[] priority;
	
	public CHContainer(int V, List<List<Node>> adj, List<List<Node>> adjR, int[] priority) {
		this.V = V;
		this.adj = adj;
		this.adjR = adjR;
		this.priority = priority;
	}
	
}
