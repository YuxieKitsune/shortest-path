package tests;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.*;
import org.junit.*;
import common.GraphReader;
import common.Node;
import dijkstras.Dijkstras;

public class DijkstrasSearchTest {
	private static List<List<String>> data;
	private static Dijkstras dpq;
	private static List<List<Node>> adj;
	
	@Before
	public void setup() {
		data = GraphReader.readCsv("beijing");
		int totalNodes = Integer.parseInt(data.get(0).get(0));
		dpq = new Dijkstras(totalNodes);
		
		adj = new ArrayList<List<Node>>();
		for (int i = 0; i < totalNodes; i++) {
			List<Node> item = new ArrayList<Node>();
			adj.add(item);
			adj.get(i).add(new Node(i, 0));
		}
		
		for (int i = 0; i < totalNodes; i++) {
			int neighbourCount = Integer.parseInt(data.get(i + 1).get(6));
			for (int j = 0; j < neighbourCount; j++) {
				int neighbour = Integer.parseInt(data.get(i + 1).get(7 + (j * 2)));
				int dist = Integer.parseInt(data.get(i + 1).get(8 + (j * 2)));
				adj.get(i).add(new Node(neighbour, dist));
			}
		}
	}
	
	@Test
	public void testDijkstras() throws FileNotFoundException, UnsupportedEncodingException{
		System.out.println("Starting Dijkstra's...");
		long startTime = System.currentTimeMillis();
		dpq.dijkstra(adj, 70, 2000);
		long endTime = System.currentTimeMillis();
		System.out.println("Process Time: " + (endTime - startTime) + " milliseconds");
	}
	
//	@Test
//	public void testDijkstrasRandom() throws IOException {
//		Random random = new Random();
//		try (BufferedWriter writer = new BufferedWriter(new FileWriter("results/dijkstraTimes.txt", true))) {
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
//			//TODO
////			long startTime = System.currentTimeMillis();
////			int dist = dpq.dijkstra(adj, node1, node2);
////			if (dist == Integer.MAX_VALUE) {
////				i--;
////				continue;
////			}
////			long endTime = System.currentTimeMillis();
////			long elapsed = (endTime - startTime);
////			try (BufferedWriter writer = new BufferedWriter(new FileWriter("results/dijkstraTimes.txt", true))) {
////				writer.write(node1 + "\t" + node2 + "\t" + dist + "\t" + elapsed);
////				writer.newLine();
////			} catch (IOException e) {
////				System.err.format("IOException: %s%n", e);
////			}
//		}
//	}
}
