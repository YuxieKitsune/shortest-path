package hop2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import common.GraphReader;
import common.Node;
import dijkstras.BiDijkstra;

public class HopLabelTest {
	private static List<List<String>> data;
	private static List<List<Node>> adj;
	private static List<List<Node>> adjR;
	private static HopLabelNew hopLabel;
	
	@Before
	public void setup() {
		data = GraphReader.readCsv("beijing");
		int totalNodes = Integer.parseInt(data.get(0).get(0));
		
		adj = new ArrayList<List<Node>>();
		adjR = new ArrayList<List<Node>>();
		for (int i = 0; i < totalNodes; i++) {
			List<Node> item = new ArrayList<Node>();
			List<Node> itemR = new ArrayList<Node>();
			adj.add(item);
			adjR.add(itemR);
			adj.get(i).add(new Node(i, 0));
			adjR.get(i).add(new Node(i, 0));
		}
		
		for (int i = 0; i < totalNodes; i++) {
			int neighbourCount = Integer.parseInt(data.get(i + 1).get(6));
			for (int j = 0; j < neighbourCount; j++) {
				int neighbour = Integer.parseInt(data.get(i + 1).get(7 + (j * 2)));
				int dist = Integer.parseInt(data.get(i + 1).get(8 + (j * 2)));
				adj.get(i).add(new Node(neighbour, dist));
				adjR.get(neighbour).add(new Node(i, dist));
			}
		}
		try {
			hopLabel = new HopLabelNew(totalNodes, adj, adjR);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testDijkstras() throws IOException{
		System.out.println("Starting Label Creation...");
		long startTime = System.currentTimeMillis();
		//hopLabel.createLabels();
		long endTime = System.currentTimeMillis();
		System.out.println("Process Time: " + (endTime - startTime) + " milliseconds");
	}
}
