package ch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import common.Node;
import dijkstras.Dijkstras;

public class Contract {
	private List<List<Node>> adj;
	private List<List<Node>> incoming; //Stores incoming nodes
	private List<List<Path>> shortPaths; //For each node, stores a list of nodes that are the shortest paths
	private PriorityQueue<Node> pq;
	private Dijkstras dpq;
	private Boolean[] contracted;
	private List<Set<Integer>> shortcutAdded;
	private int[] priority;
	private int[] deletedN;
	//Contracts nodes
	
	public Contract(List<List<Node>> adj, List<List<Node>> incoming) {
		this.adj = adj;
		this.incoming = incoming;
		contracted = new Boolean[adj.size()];
		shortcutAdded = new ArrayList<Set<Integer>>();
		priority = new int[adj.size()];
		deletedN = new int[adj.size()];
		pq = new PriorityQueue<Node>(adj.size(), new Node());
		dpq = new Dijkstras(adj.size(), this.adj, incoming, contracted);
		shortPaths = new ArrayList<List<Path>>();
		for (int i = 0; i < adj.size(); i++) {
			List<Path> list = new ArrayList<Path>();
			Set<Integer> set = new HashSet<Integer>();
			for (int j = 0; j < adj.get(i).size(); j++) {
				set.add(adj.get(i).get(j).nodeid);
			}
			shortPaths.add(list);
			shortcutAdded.add(set);
			contracted[i] = false;
			deletedN[i] = 0;
		}
	}
	
	private int getDistance(int nodeId1, int nodeId2) {
		List<Node> outNeighbors = adj.get(nodeId1);
		for (int i = 0; i < outNeighbors.size(); i++) {
			if (outNeighbors.get(i).nodeid == nodeId2) {
				return (int) outNeighbors.get(i).cost;
			}
		}
		return 0;
	}
	
	private int getEdgeDifference(int nodeId) {
		List<Node> outNeighbors = adj.get(nodeId);
		List<Node> inNeighbors = incoming.get(nodeId);
		int edgeCount = inNeighbors.size() + outNeighbors.size() - 2;
		int totalShortcuts = 0;
		for (int i = 0; i < inNeighbors.size(); i++) {
			int incoming = inNeighbors.get(i).nodeid;
			int inDist = getDistance(incoming, nodeId);
			for (int j = 1; j < outNeighbors.size(); j++) {
				int outgoing = outNeighbors.get(j).nodeid;
				if (contracted[outgoing]) {
					continue;
				}
				int outDist = getDistance(nodeId, outgoing);
				int pathDist = inDist + outDist;
				int dist = dpq.dijkstraLimit(incoming, outgoing, nodeId, pathDist);
				if (dist > pathDist /*&& priority[incoming] <= priority[outgoing]*/) {
					Path thisPath = new Path(incoming, outgoing, pathDist);
					shortPaths.get(nodeId).add(thisPath); //Finds shortcuts and stores them so that search is not repeated
					totalShortcuts += 1;
				}
			}
		}
		return totalShortcuts - edgeCount + deletedN[nodeId];
	}
	
	/**
	 * Orders nodes using the edge difference and deleted neighbours
	 */
	public void orderNodes() {
		//PriorityQueue<Node> pq = new PriorityQueue<Node>();	
		for (int i = 0; i < adj.size(); i++) {
			int edgeDiff = getEdgeDifference(i);
			pq.add(new Node(i, edgeDiff));	//temporarily uses 'node.cost' instead of 'node.priority'
		}
		return;
	}
	
	private void contract(Node current) {
		adj.get(current.nodeid).get(0).priority = (int) current.cost;
		for (int i = 0; i < shortPaths.get(current.nodeid).size(); i++) {
			Path currentPath = shortPaths.get(current.nodeid).get(i);
			if (shortcutAdded.get(currentPath.incoming).contains(currentPath.outgoing)) {
				continue;
			}
			//if (!contracted[currentPath.outgoing])    !contracted[currentPath.incoming] && 
			//else if ((!contracted[currentPath.outgoing]) ) {
				adj.get(currentPath.incoming).add(new Node(currentPath.outgoing, currentPath.distance));
				deletedN[currentPath.incoming] += 1;
				deletedN[currentPath.outgoing] += 1;
				shortcutAdded.get(currentPath.incoming).add(currentPath.outgoing);
			//}
		}
	}
	
	public int[] contractNodes() throws IOException {
		orderNodes();
		int counter = 0;
		while (!pq.isEmpty()) {
			Node currentNode = pq.remove();
			int newEdgeDiff = getEdgeDifference(currentNode.nodeid);
			if (pq.isEmpty() || newEdgeDiff <= pq.peek().cost) {
				contract(currentNode);
				contracted[currentNode.nodeid] = true;
				priority[currentNode.nodeid] = counter;
				counter++;
			} else {
				pq.add(new Node(currentNode.nodeid, newEdgeDiff));
			}
		}
		CHFileWriter.createGraphFile(adj, priority);
		return priority;
	}
}

class Path {
	int incoming;
	int outgoing;
	int distance;
	
	public Path(int incoming, int outgoing, int distance) {
		this.incoming = incoming;
		this.outgoing = outgoing;
		this.distance = distance;
	}
}
