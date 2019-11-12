package tests;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import org.junit.*;
import common.GraphReader;
import common.Node;
import dijkstras.BiDijkstra;

public class BidirectionalSearchTest {
	private static List<List<String>> data;
	private static BiDijkstra dpq;
	private static List<List<Node>> adj;
	private static List<List<Node>> adjR;
	
	@Before
	public void setup() {
		data = GraphReader.readCsv("beijing");
		int totalNodes = Integer.parseInt(data.get(0).get(0));
		dpq = new BiDijkstra(totalNodes, 1);
		
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
	}
	
	@Test (timeout = 1000 * 10)
	public void testDijkstras(){
		System.out.println("Starting Bidirectional Dijkstra's...");
		long startTime = System.currentTimeMillis();
		dpq.bidijkstra(adj, adjR, 70, 2000);
		long endTime = System.currentTimeMillis();
		System.out.println("Process Time: " + (endTime - startTime) + " milliseconds");
	}
	
//	@Test
//	public void testDijkstrasRandom() throws IOException {
//		Random random = new Random();
//		try (BufferedWriter writer = new BufferedWriter(new FileWriter("results/bidijkstraTimes.txt", true))) {
//			writer.write("Node1 \tNode2 \tDistance\t Time");
//			writer.newLine();
//		} catch (IOException e) {
//			System.err.format("IOException: %s%n", e);
//		}
//		for (int i = 0; i < 1000; i++) {
//			System.out.println(i);
//			int node1 = random.nextInt(312350);
//			int node2 = random.nextInt(312350);
//			if (node1 == node2) {
//				i--;
//				continue;
//			}
//			long startTime = System.currentTimeMillis();
//			//TODO
////			int dist = dpq.bidijkstra(adj, adjR, node1, node2);
////			if (dist == Integer.MAX_VALUE) {
////				i--;
////				continue;
////			}
////			long endTime = System.currentTimeMillis();
////			long elapsed = (endTime - startTime);
////			try (BufferedWriter writer = new BufferedWriter(new FileWriter("results/bidijkstraTimes.txt", true))) {
////				writer.write(node1 + "\t" + node2 + "\t" + dist + "\t" + elapsed);
////				writer.newLine();
////			} catch (IOException e) {
////				System.err.format("IOException: %s%n", e);
////			}
//		}
//	}
}
