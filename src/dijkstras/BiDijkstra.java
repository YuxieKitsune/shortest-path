package dijkstras;

import java.util.*;

import common.Node;
import common.SearchPair;


public class BiDijkstra {
	int dist[];					//List of distances from source node
	private int distR[];
	private Set<Integer> settled;		//List of visited nodes
	private Set<Integer> settledR;
	private PriorityQueue<Node> pq;		//PQ for each Dijkstras
	private PriorityQueue<Node> pqR;
	private int V;						//Total nodes
	List<List<Node>> adj;				//Graph structure
	List<List<Node>> adjR;
	
	public BiDijkstra(int V) {
		this.V = V;
		dist = new int[V];
		settled = new HashSet<Integer>();
		pq = new PriorityQueue<Node>(V, new Node());
	}
	
	/**
	 * Flag to differentiate between Dijkstras and Bidirectional.
	 * Ensures that the min required arrays are initialised.
	 * @param V
	 * @param flag - Indicates to initialise the arrays for the reverse graph
	 */
	public BiDijkstra(int V, int flag) {
		this.V = V;
		dist = new int[V];
		distR = new int[V];
		settled = new HashSet<Integer>();
		settledR = new HashSet<Integer>();
		pq = new PriorityQueue<Node>(V, new Node());
		pqR = new PriorityQueue<Node>(V, new Node());
	}
	
	public SearchPair bidijkstra(List<List<Node>> adj, List<List<Node>> adjR, int src, int srcR) {
		this.adj = adj;
		this.adjR = adjR;
		for (int i = 0; i < V; i++) {
			dist[i] = Integer.MAX_VALUE;
			distR[i] = Integer.MAX_VALUE;
		}
		settled = new HashSet<Integer>();
		settledR = new HashSet<Integer>();
		pq = new PriorityQueue<Node>(V, new Node());
		pqR = new PriorityQueue<Node>(V, new Node());
		
		pq.add(new Node(src, 0));
		dist[src] = 0;
		pqR.add(new Node(srcR, 0));
		distR[srcR] = 0;
		int mu = Integer.MAX_VALUE;
		
		while(!pq.isEmpty() && !pqR.isEmpty()) {
			if (pq.peek().cost + pqR.peek().cost > mu) {
				//System.out.println("PQ: " + pq.peek().cost + "\tPQR: " + pqR.peek().cost);
				break;
			}
			
			int currentNode = pq.remove().nodeid;
			if (!settled.contains(currentNode)) {
				settled.add(currentNode);
				mu = expNeighbours(currentNode, mu);
			}
			
			if (!pq.isEmpty() && pq.peek().cost + pqR.peek().cost > mu) {
				//System.out.println("PQ: " + pq.peek().cost + "\tPQR: " + pqR.peek().cost);
				break;
			}
			int currentNodeR = pqR.remove().nodeid;
			if (!settledR.contains(currentNodeR)) {
				settledR.add(currentNodeR);
				mu = expNeighboursR(currentNodeR, mu);
			}
		}
//		System.out.println("Settled: " + (settled.size() + settledR.size()));
//		System.out.println("Distance: " + mu);
		return new SearchPair(mu, settled.size());
	}
	
	private int expNeighbours(int currentNode, int mu) {
		double edgeDist = 0;
		int newDist = 0;
		
		//Loop through every neighbour of currentNode
		for (int i = 1; i < adj.get(currentNode).size(); i++) {
			Node neighbourNode = adj.get(currentNode).get(i);
			
			if (!settled.contains(neighbourNode.nodeid)) {
				edgeDist = neighbourNode.cost;
				newDist = (int) (dist[currentNode] + edgeDist);
				
				if (newDist < dist[neighbourNode.nodeid]) {
					dist[neighbourNode.nodeid] = newDist;
					adj.get(neighbourNode.nodeid).get(0).prevnode = currentNode;
				}
				
				int newMu = newDist + distR[neighbourNode.nodeid];
				if (newMu < mu && newMu > 0) {
					//System.out.println("New Mu: " + newMu);
					mu = newMu;
				}
				
				pq.add(new Node(neighbourNode.nodeid, dist[neighbourNode.nodeid]));
			}
		}
		return mu;
	}
	
	private int expNeighboursR(int currentNodeR, int mu) {
		double edgeDist = 0;
		int newDist = 0;
		
		//Loop through every neighbour of currentNode
		for (int i = 1; i < adjR.get(currentNodeR).size(); i++) {
			Node neighbourNode = adjR.get(currentNodeR).get(i);
			//System.out.println(adj.get(currentNode));
			
			if (!settledR.contains(neighbourNode.nodeid)) {
				edgeDist = neighbourNode.cost;
				newDist = (int) (distR[currentNodeR] + edgeDist);
				
				if (newDist < distR[neighbourNode.nodeid]) {
					distR[neighbourNode.nodeid] = newDist;
					adjR.get(neighbourNode.nodeid).get(0).prevnode = currentNodeR;
				}
				
				int newMu = newDist + dist[neighbourNode.nodeid];
				if (newMu < mu && newMu > 0) {
					//System.out.println("New Mu: " + newMu);
					mu = newMu;
				}
				
				pqR.add(new Node(neighbourNode.nodeid, distR[neighbourNode.nodeid]));
			}
		}
		return mu;
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
	
}
