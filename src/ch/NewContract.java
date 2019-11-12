package ch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import common.Node;
import dijkstras.Dijkstras;

public class NewContract {
	private List<List<Node>> outNodes;
	private List<List<Node>> inNodes;
	private PriorityQueue<Node> pq;
	private List<Set<Integer>> pathAdded;
	private List<Set<Path>> pathList;
	private Dijkstras dpq;
	private Boolean[] contracted;
	private int[] contractedCount;
	private int totalNodes;
	private int[] priority;
	
	public NewContract(List<List<Node>> outNodes, List<List<Node>> inNodes) {
		this.outNodes = outNodes;
		this.inNodes = inNodes;
		totalNodes = outNodes.size();
		pq = new PriorityQueue<Node>(totalNodes, new Node());
		contracted = new Boolean[totalNodes];
		pathAdded = new ArrayList<Set<Integer>>();
		//pathAdded2 = new ArrayList<Set<Integer>>();
		pathList = new ArrayList<Set<Path>>();
		contractedCount = new int[totalNodes];
		priority = new int[totalNodes];
		dpq = new Dijkstras(totalNodes, this.outNodes, this.inNodes, contracted);
		for (int i = 0; i < totalNodes; i++) {
			priority[i] = Integer.MAX_VALUE;
			contracted[i] = false;
			contractedCount[i] = 0;
			Set<Integer> set = new HashSet<Integer>();
			//Set<Integer> set2 = new HashSet<Integer>();
			Set<Path> pathSet = new HashSet<Path>();
			pathAdded.add(set);
			//pathAdded2.add(set2);
			pathList.add(pathSet);
		}
	}
	
	private int getDistance(int nodeid1, int nodeid2) {
		List<Node> outNeighbors = outNodes.get(nodeid1);
		for (int i = 0; i < outNeighbors.size(); i++) {
			if (outNeighbors.get(i).nodeid == nodeid2) {
				return (int) outNeighbors.get(i).cost;
			}
		}
		return 0;
	}
	
	public int getEdgeDiff(int nodeid) {
		List<Node> outList = outNodes.get(nodeid);
		List<Node> inList = inNodes.get(nodeid);
		int totalShortcuts = 0;
		int totalEdges = 0;
		if (outList.size() == inList.size()) {
			totalEdges = inList.size() - 1;
		} else {
			totalEdges = Math.abs(inList.size() - outList.size());
			if (inList.size() > outList.size()) {
				totalEdges += inList.size() - 1;
			} else {
				totalEdges += outList.size() - 1;
			}
		}
		
		for (int i = 1; i < outList.size(); i++) {
			int currentOutNode = outList.get(i).nodeid;
			if (contracted[currentOutNode]) {
				continue;
			}
			int currentOutDist = getDistance(nodeid, currentOutNode);
			for (int j = 1; j < inList.size(); j++) {
				int currentInNode = inList.get(j).nodeid;
				if (contracted[currentInNode] || pathAdded.get(currentInNode).contains(currentOutNode)) {
					continue;
				}
				int currentInDist = getDistance(currentInNode, nodeid);
				int totalDist = currentInDist + currentOutDist;
				int dijkstraDist = dpq.dijkstraLimit(currentInNode, currentOutNode, nodeid, totalDist);
				if (dijkstraDist > totalDist) {
					totalShortcuts++;
					Path newPath = new Path(currentInNode, currentOutNode, totalDist);
					pathList.get(nodeid).add(newPath);
				}
			}
		}
		return totalShortcuts - totalEdges + contractedCount[nodeid];
		//return totalShortcuts - totalEdges;
	}
	
	private void orderNodes() {
		for (int i = 0; i < totalNodes; i++) {
			int edgeDiff = getEdgeDiff(i);
			pq.add(new Node(i, edgeDiff));
		}
	}
	
	public void contract(int nodeid) {
		for (Path path : pathList.get(nodeid)) {
			if (contracted[path.incoming] || contracted[path.outgoing] || pathAdded.get(path.incoming).contains(path.outgoing)) {
				continue;
			}
			outNodes.get(path.incoming).add(new Node(path.outgoing, path.distance));
			inNodes.get(path.outgoing).add(new Node(path.incoming, path.distance));
			contractedCount[path.incoming]++;
			contractedCount[path.outgoing]++;
			pathAdded.get(path.incoming).add(path.outgoing);
		}
		contracted[nodeid] = true;
	}
	
	public int[] contractNodes() throws IOException {
		int totalEdgesOg = 0;
		for (int i = 0; i < outNodes.size(); i++) {
			totalEdgesOg += outNodes.get(i).size();
		}
		orderNodes();
		int counter = 0;
		while (!pq.isEmpty()) {
			int currentNode = pq.remove().nodeid;
			if (counter == 300000) {break;}
			//System.out.println(inNodes.get(currentNode).size() + " " + outNodes.get(currentNode).size());
			
			int newEdgeDiff = getEdgeDiff(currentNode);
			if (!pq.isEmpty() && newEdgeDiff > pq.peek().cost) {
				pq.add(new Node(currentNode, newEdgeDiff));
				continue;
			}
			contract(currentNode);
			priority[currentNode] = counter;
			counter++;
			System.out.println(counter);
		}
		int totalEdges = 0;
		for (int i = 0; i < outNodes.size(); i++) {
			totalEdges += outNodes.get(i).size();
		}
		System.out.println("Added shortcut count:" + (totalEdges - totalEdgesOg));
		CHFileWriter.createGraphFile(outNodes, priority);
		return priority;
	}

}
