package hop2;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.google.gson.*;

import common.Node;

public class HopLabelNew {
	private int totalNodes;
	private List<List<Node>> outNodes;
	private List<List<Node>> inNodes;
	private List<List<Label>> outLabels;
	private List<List<Label>> inLabels;
	private List<BufferedWriter> writerIn;
	private List<BufferedWriter> writerOut;
	private Set<Integer> labelled;
	private HopDijkstra dijkstra;
	
	@SuppressWarnings("unchecked")
	public HopLabelNew(int totalNodes, List<List<Node>> outNodes, List<List<Node>> inNodes) throws IOException {
		this.totalNodes = totalNodes;
		this.outNodes = outNodes;
		this.inNodes = inNodes;
		outLabels = new ArrayList<List<Label>>();
		inLabels = new ArrayList<List<Label>>();
		labelled = new HashSet<Integer>();
		dijkstra = new HopDijkstra(totalNodes, outNodes, inNodes, labelled);
		int[][] testIn = new int[totalNodes][totalNodes];
		for (int i = 0; i < totalNodes; i++) {
			List<Label> outList = new ArrayList<Label>();
			outLabels.add(outList);
			List<Label> inList = new ArrayList<Label>();
			inLabels.add(inList);
			System.out.println(i);
		}
	}
	
	
	public void addInLabels(List<Label> labelList, int src) throws IOException {
		for (Label label : labelList) {
			if (label.nodeid == src)
				continue;
		}
	}
	
	public void addOutLabels(List<Label> labelList, int src) throws IOException {
		for (Label label : labelList) {
			if (label.nodeid == src)
				continue;
		}
	}
	
	public void createLabels() throws IOException {
		for (int i = 0; i < totalNodes; i++) {
			System.out.println("Node: " + i);
			//In Labels: Normal Dijkstra's from specific node
			addInLabels(dijkstra.inLabelSearch(i), i);
			//Out Labels: Reverse Dijkstra's from specific node
			addOutLabels(dijkstra.outLabelSearch(i), i);
			labelled.add(i);
		}
	}
	
}