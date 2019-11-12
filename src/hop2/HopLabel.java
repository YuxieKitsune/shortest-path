package hop2;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.google.gson.*;

import common.Node;

public class HopLabel {
	private int totalNodes;
	private List<List<Node>> outNodes;
	private List<List<Node>> inNodes;
	private List<List<Label>> outLabels;
	private List<List<Label>> inLabels;
	private Set<Integer> labelled;
	private Gson gsonFile;
	private HashMap<String, HashMap<Integer, List<Label>>> inOutLabels;
	private HopDijkstra dijkstra;
	
	@SuppressWarnings("unchecked")
	public HopLabel(int totalNodes, List<List<Node>> outNodes, List<List<Node>> inNodes) throws IOException {
		this.totalNodes = totalNodes;
		this.outNodes = outNodes;
		this.inNodes = inNodes;
		outLabels = new ArrayList<List<Label>>();
		inLabels = new ArrayList<List<Label>>();
		labelled = new HashSet<Integer>();
		dijkstra = new HopDijkstra(totalNodes, outNodes, inNodes, labelled);
		gsonFile = new GsonBuilder().setPrettyPrinting().create();
		inOutLabels = new HashMap<String, HashMap<Integer, List<Label>>>();
		HashMap<Integer, List<Label>> in = new HashMap<Integer, List<Label>>();
		HashMap<Integer, List<Label>> out = new HashMap<Integer, List<Label>>();
		inOutLabels.put("in", in);
		inOutLabels.put("out", out);
		FileWriter file = new FileWriter("HopBeijing.json");
		for (int i = 0; i < totalNodes; i++) {
			List<Label> outList = new ArrayList<Label>();
			outLabels.add(outList);
			List<Label> inList = new ArrayList<Label>();
			inLabels.add(inList);
			in.put(i, inList);
			out.put(i, outList);
		}
		gsonFile.toJson(in, file);
		gsonFile.toJson(out, file);
		file.flush();
	}
	
	
	public void addInLabels(List<Label> labelList, int src) {
		for (Label label : labelList) {
			if (label.nodeid == src)
				continue;
			inLabels.get(label.nodeid).add(new Label(src, label.dist));
		}
	}
	
	public void addOutLabels(List<Label> labelList, int src) {
		for (Label label : labelList) {
			if (label.nodeid == src)
				continue;
			outLabels.get(label.nodeid).add(new Label(src, label.dist));
		}
	}
	
	public void createLabels() {
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