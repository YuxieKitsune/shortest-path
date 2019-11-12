package dijkstras;

import java.util.*;

import common.Node;
import common.SearchPair;


public class Dijkstras {
	int dist[];					//List of distances from source node
	private int distR[];
	private Set<Integer> settled;		//List of visited nodes
	private Set<Integer> settledR;
	private PriorityQueue<Node> pq;		//PQ for each Dijkstras
	private PriorityQueue<Node> pqR;
	private int V;						//Total nodes
	public List<List<Node>> adj;				//Graph structure
	public List<List<Node>> adjR;
	public Boolean[] contracted;
	private Set<Integer> visited;
	
	public Dijkstras(int V) {
		this.V = V;
		dist = new int[V];
		settled = new HashSet<Integer>();
		pq = new PriorityQueue<Node>(V, new Node());
	}
	
	public Dijkstras(int V, List<List<Node>> adj, List<List<Node>> adjR, Boolean[] contracted) {
		this.V = V;
		dist = new int[V];
		this.adj = adj;
		this.adjR = adjR;
		this.contracted = contracted;
		for (int i = 0; i < V; i++) {
			dist[i] = Integer.MAX_VALUE;
		}
	}
	
	public SearchPair dijkstra(List<List<Node>> adj, int src, int goal) {
		this.adj = adj;
		for (int i = 0; i < V; i++) {
			dist[i] = Integer.MAX_VALUE;
			
		}
		
		settled = new HashSet<Integer>();
		pq = new PriorityQueue<Node>(V, new Node());
		pq.add(new Node(src, 0));
		dist[src] = 0;
		while (!pq.isEmpty()) {
			//System.out.println("PQ: " + pq);
			int currentNode = pq.remove().nodeid;
			
			if (currentNode == goal) {
				//printPath(currentNode, src, goal);
				break;
			}
			
			if (!settled.contains(currentNode)) {
				settled.add(currentNode);
				expNeighbours(currentNode);
			}
			
		}
//		System.out.println("Settled: " + settled.size());
//		System.out.println("Distance: " + dist[goal]);
		return new SearchPair(dist[goal], settled.size());
	}
	
	/**
	 * Limiting Dijkstras to two hops: Uses expNeighbours2H
	 */
	public int dijkstraLimit(int src, int goal, int vnode, int maxPathLength) {
		settled = new HashSet<Integer>();
		visited = new HashSet<Integer>();
		
		Set<Integer> explorable = new HashSet<Integer>();
		for (int i = 0; i < adj.get(src).size(); i++) {
			int neighbourNode = adj.get(src).get(i).nodeid;
			if (neighbourNode == vnode) {
				continue;
			}
			explorable.add(neighbourNode);
			for (int j = 0; j < adj.get(neighbourNode).size(); j++) {
				int hopNeighbour = adj.get(src).get(i).nodeid;
				if (hopNeighbour == vnode) {
					continue;
				}
				explorable.add(hopNeighbour);
			}
		}
		if (!explorable.contains(goal)) {
			return dist[goal];
		}
		
		pq = new PriorityQueue<Node>(V, new Node());

		pq.add(new Node(src, 0));
		dist[src] = 0;
		visited.add(src);
		while (!pq.isEmpty()) {
			int currentNode = pq.remove().nodeid;
			// || currentNode == goal
			if (dist[currentNode] > maxPathLength || currentNode == goal) {
				break;
			}
			if (!settled.contains(currentNode)) {
				settled.add(currentNode);
				//expNeighbours(currentNode);
				expLimit(currentNode, vnode, explorable);
			}
		}
		
		int value = dist[goal];
		
		//Only set the visited nodes back to MAX_VALUE
		for (Integer x : visited) {
			dist[x] = Integer.MAX_VALUE;
		}
		
		return value;
	}
	
	
	private void expNeighbours(int currentNode) {
		double edgeDist = 0;
		int newDist = 0;
		
		//Loop through every neighbor of currentNode
		for (int i = 1; i < adj.get(currentNode).size(); i++) {
			Node v = adj.get(currentNode).get(i);
			
			if (!settled.contains(v.nodeid)) {
				edgeDist = v.cost;
				newDist = (int) (dist[currentNode] + edgeDist);
				
				if (newDist < dist[v.nodeid]) {
					dist[v.nodeid] = newDist;
					adj.get(v.nodeid).get(0).prevnode = currentNode;
				}
				
				pq.add(new Node(v.nodeid, dist[v.nodeid]));
			}
		}
	}
	
	/**
	 * vnode is the node in the path u-v-w. We do not want to search for this path
	 */
	private void expLimit(int currentNode, int vnode, Set<Integer> explorable) {
		double edgeDist = 0;
		int newDist = 0;
		
		//Loop through every neighbor of currentNode
		for (int i = 1; i < adj.get(currentNode).size(); i++) {
			Node v = adj.get(currentNode).get(i);
			
			if (!explorable.contains(v.nodeid) || contracted[v.nodeid]) {
				continue;
			}
			if (v.nodeid == vnode) {continue;}
			
			if (!settled.contains(v.nodeid)) {
				edgeDist = v.cost;
				newDist = (int) (dist[currentNode] + edgeDist);
				
				if (newDist < dist[v.nodeid]) {
					dist[v.nodeid] = newDist;
					adj.get(v.nodeid).get(0).prevnode = currentNode;
				}
				visited.add(v.nodeid);
				
				pq.add(new Node(v.nodeid, dist[v.nodeid]));
			}
		}
	}
	
	
	private void printPath(int currentNode, int startNode, int endNode) {
		if (currentNode == endNode) {
			List<Integer> path = new ArrayList<>();
			path.add(currentNode);
			while (currentNode != startNode) {
				currentNode = adj.get(currentNode).get(0).prevnode;
				path.add(currentNode);
			}
			Collections.reverse(path);
			System.out.println(path);
			System.out.println(dist[endNode]);
		} else {
			List<Integer> path = new ArrayList<>();
			List<Integer> pathR = new ArrayList<>();
			int savedCurrentNode = currentNode;
			path.add(currentNode);
			while (currentNode != startNode) {
				currentNode = adj.get(currentNode).get(0).prevnode;
				path.add(currentNode);
			}
			while (savedCurrentNode != endNode) {
				savedCurrentNode = adjR.get(savedCurrentNode).get(0).prevnode;
				path.add(savedCurrentNode);
			}
			Collections.reverse(path);
			path.addAll(pathR);
			System.out.println(path);
		}
	}
	
//	public void bidijkstra(List<List<Node>> adj, List<List<Node>> adjR, int src, int srcR) {
//	this.adj = adj;
//	this.adjR = adjR;
//	for (int i = 0; i < V; i++) {
//		dist[i] = Integer.MAX_VALUE;
//		distR[i] = Integer.MAX_VALUE;
//	}
//	
//	pq.add(new Node(src, 0));
//	dist[src] = 0;
//	pqR.add(new Node(srcR, 0));
//	distR[srcR] = 0;
//	int mu = Integer.MAX_VALUE;
//	
//	while(!pq.isEmpty() && !pqR.isEmpty()) {
//		if (pq.peek().cost + pq.peek().cost > mu) {
//			break;
//		}
//		int currentNode = pq.remove().nodeid;
//		if (settledR.contains(currentNode)) {
//			int new_dist = dist[currentNode] + distR[currentNode];
//			if (new_dist < mu) {
////				//printPath(currentNode, src, srcR);
////				break;
////			} else {
//				mu = new_dist;
//			}
//		}
//		if (!settled.contains(currentNode)) {
//			settled.add(currentNode);
//			expNeighbours(currentNode);
//		}
//		
//		if (pq.peek().cost + pq.peek().cost > mu) {
//			break;
//		}
//		int currentNodeR = pqR.remove().nodeid;
//		if (settled.contains(currentNodeR)) {
//			int new_dist = dist[currentNodeR] + distR[currentNodeR];
//			if (new_dist < mu) {
////				break;
////			} else {
//				mu = new_dist;
//			}
//		}
//		if (!settledR.contains(currentNodeR)) {
//			settledR.add(currentNodeR);
//			expNeighboursR(currentNodeR);
//		}
//	}
//	System.out.println("Settled: " + (settled.size() + settledR.size()));
//	System.out.println("Distance: " + mu);
//}
	
//	private void expNeighboursR(int currentNodeR) {
//	int edgeDist = 0;
//	int newDist = 0;
//	
//	//Loop through every neighbour of currentNode
//	for (int i = 1; i < adjR.get(currentNodeR).size(); i++) {
//		Node v = adjR.get(currentNodeR).get(i);
//		//System.out.println(adj.get(currentNode));
//		
//		if (!settledR.contains(v.nodeid)) {
//			edgeDist = v.cost;
//			newDist = distR[currentNodeR] + edgeDist;
//			
//			if (newDist < distR[v.nodeid]) {
//				distR[v.nodeid] = newDist;
//				adjR.get(v.nodeid).get(0).prevnode = currentNodeR;
//			}
//			
//			pqR.add(new Node(v.nodeid, distR[v.nodeid]));
//		}
//	}
//}
	
}
