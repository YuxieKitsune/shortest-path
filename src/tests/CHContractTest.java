package tests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.*;

import ch.CHBidirection;
import ch.NewContract;
import common.GraphReader;
import common.Node;

public class CHContractTest {
	private static List<List<String>> data;
	private static NewContract ch;
	private static List<List<Node>> adj;
	private static List<List<Node>> adjR;
	private CHBidirection chb;
	
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
				adj.get(i).add(new Node(neighbour, dist, Integer.MAX_VALUE));
				adjR.get(neighbour).add(new Node(i, dist, Integer.MAX_VALUE));
			}
		}
		ch = new NewContract(adj, adjR);
	}
	
	@Test
	public void testContraction() throws IOException {
		System.out.println("Starting Contraction of Nodes...");
		long startTime = System.currentTimeMillis();
		int[] priority = ch.contractNodes();
		long endTime = System.currentTimeMillis();
		System.out.println("Process Time: " + ((endTime - startTime) / 1000) + " seconds");
	}
}
