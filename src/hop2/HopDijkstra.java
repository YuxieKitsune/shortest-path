package hop2;

import java.util.*;

import common.Node;


public class HopDijkstra {
	int dist[];					//List of distances from source node
	private int distR[];
	private Set<Integer> settled;		//List of visited nodes
	private PriorityQueue<Node> pq;		//PQ for each Dijkstras
	private int V;						//Total nodes
	public List<List<Node>> outNodes;				//Graph structure
	public List<List<Node>> inNodes;
	private Set<Integer> visited;
	private Set<Integer> labelled;

	public HopDijkstra(int V, List<List<Node>> outNodes, List<List<Node>> inNodes, Set<Integer> labelled) {
		this.V = V;
		dist = new int[V];
		this.outNodes = outNodes;
		this.inNodes = inNodes;
		this.labelled = labelled;
		for (int i = 0; i < V; i++) {
			dist[i] = Integer.MAX_VALUE;
		}
	}
	
	/**
	 * From src node, find all distances to other nodes
	 * Works just like a normal Dijkstra's search using outNodes
	 * @param src: Node to search from
	 */
	public List<Label> inLabelSearch(int src) {
		visited = new HashSet<Integer>();
		settled = new HashSet<Integer>();
		for (int i = 0; i < V; i++) {
			dist[i] = Integer.MAX_VALUE;
		}
		
		pq = new PriorityQueue<Node>(V, new Node());
		pq.add(new Node(src, 0));
		dist[src] = 0;
		visited.add(src);
		while (!pq.isEmpty()) {
			int currentNode = pq.remove().nodeid;
			if (!settled.contains(currentNode)) {
				settled.add(currentNode);
				expNeighboursIn(currentNode);
			}
		}
		
		List<Label> labelList = createLabelList();
		
		return labelList;
	}
	
	/**
	 * For all nodes, find distance to src node
	 * Can use the reversed graph, or InNodes, to search in reverse from src node
	 * @param src: Node to search to
	 */
	public List<Label> outLabelSearch(int src) {
		visited = new HashSet<Integer>();
		settled = new HashSet<Integer>();
		for (int i = 0; i < V; i++) {
			dist[i] = Integer.MAX_VALUE;
		}
		
		pq = new PriorityQueue<Node>(V, new Node());
		pq.add(new Node(src, 0));
		dist[src] = 0;
		visited.add(src);
		while (!pq.isEmpty()) {
			int currentNode = pq.remove().nodeid;
			if (!settled.contains(currentNode)) {
				settled.add(currentNode);
				expNeighboursOut(currentNode);
			}
		}
		
		List<Label> labelList = createLabelList();
		
		return labelList;
	}
	
	
	private List<Label> createLabelList() {
		List<Label> labelList = new ArrayList<Label>();
		for (int x : visited) {
			Label newLabel = new Label(x, dist[x]);
			labelList.add(newLabel);
		}
		return labelList;
	}
	

	private void expNeighboursIn(int currentNode) {
		double edgeDist = 0;
		int newDist = 0;

		for (int i = 1; i < outNodes.get(currentNode).size(); i++) {
			Node v = outNodes.get(currentNode).get(i);
			
			if (!settled.contains(v.nodeid) && !labelled.contains(v.nodeid)) {
				edgeDist = v.cost;
				newDist = (int) (dist[currentNode] + edgeDist);
				
				if (newDist < dist[v.nodeid]) {
					dist[v.nodeid] = newDist;
					outNodes.get(v.nodeid).get(0).prevnode = currentNode;
				}
				visited.add(v.nodeid);
				
				pq.add(new Node(v.nodeid, dist[v.nodeid]));
			}
		}
	}
	
	private void expNeighboursOut(int currentNode) {
		double edgeDist = 0;
		int newDist = 0;

		for (int i = 1; i < inNodes.get(currentNode).size(); i++) {
			Node v = inNodes.get(currentNode).get(i);
			
			if (!settled.contains(v.nodeid) && !labelled.contains(v.nodeid)) {
				edgeDist = v.cost;
				newDist = (int) (dist[currentNode] + edgeDist);
				
				if (newDist < dist[v.nodeid]) {
					dist[v.nodeid] = newDist;
					inNodes.get(v.nodeid).get(0).prevnode = currentNode;
				}
				visited.add(v.nodeid);
				
				pq.add(new Node(v.nodeid, dist[v.nodeid]));
			}
		}
	}
}
