package tests;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import ch.CHBidirection;
import ch.CHContainer;
import ch.CHFileReader;
import common.Coords;
import common.GraphReader;
import common.Node;
import common.SearchPair;
import dijkstras.BiDijkstra;
import dijkstras.Dijkstras;
import star.AStar;

public class AllTest {
	private static List<List<String>> data;
	private static List<List<Node>> adj;
	private static List<List<Node>> adjR;
	private static List<Coords> coords;
	private static Dijkstras dijkstra;
	private static BiDijkstra bidijkstra;
	private static AStar star;
	private static CHBidirection chb;
	private static CHContainer container;
	
	@Before
	public void setup() {
		data = GraphReader.readCsv("beijing");
		int totalNodes = Integer.parseInt(data.get(0).get(0));
		bidijkstra = new BiDijkstra(totalNodes, 1);
		dijkstra = new Dijkstras(totalNodes);
		
		adj = new ArrayList<List<Node>>();
		adjR = new ArrayList<List<Node>>();
		coords = new ArrayList<Coords>();
		for (int i = 0; i < totalNodes; i++) {
			List<Node> item = new ArrayList<Node>();
			List<Node> itemR = new ArrayList<Node>();
			adj.add(item);
			adjR.add(itemR);
			adj.get(i).add(new Node(i, 0));
			adjR.get(i).add(new Node(i, 0));
		}
		
		for (int i = 0; i < totalNodes; i++) {
			double lon = Double.parseDouble(data.get(i).get(3));
			double lat = Double.parseDouble(data.get(i).get(2));
			coords.add(new Coords(lon, lat));
			
			int neighbourCount = Integer.parseInt(data.get(i + 1).get(6));
			for (int j = 0; j < neighbourCount; j++) {
				int neighbour = Integer.parseInt(data.get(i + 1).get(7 + (j * 2)));
				int dist = Integer.parseInt(data.get(i + 1).get(8 + (j * 2)));
				adj.get(i).add(new Node(neighbour, dist));
				adjR.get(neighbour).add(new Node(i, dist));
			}
		}
		
		star = new AStar(totalNodes, coords);
		container = CHFileReader.readCsv("CHBeijing70s.txt");
		chb = new CHBidirection(container.V);
	}
	
	@Test
	public void testAll() throws IOException {
		Random random = new Random();
		setupFiles();
		for (int i = 0; i < 1000; i++) {
			int node1 = random.nextInt(312350);
			int node2 = random.nextInt(312350);
			if (node1 == node2) {
				i--;
				continue;
			}
			long startTime = System.currentTimeMillis();
			SearchPair chResult = chb.search(container.adj, container.adjR, container.priority, node1, node2);
//			if (chResult.dist == Integer.MAX_VALUE && i < 500) {
//				System.out.println("ch" + i);
//				i--;
//				continue;
//			}
			long endTime = System.currentTimeMillis();
			long elapsedCH = (endTime - startTime);
			
			startTime = System.currentTimeMillis();
			SearchPair dijkResult = dijkstra.dijkstra(adj, node1, node2);
			if (dijkResult.dist == Integer.MAX_VALUE) {
				System.out.println("Di");
				i--;
				continue;
			}
			endTime = System.currentTimeMillis();
			long elapsedDijk = (endTime - startTime);
			
			startTime = System.currentTimeMillis();
			SearchPair bidiResult = bidijkstra.bidijkstra(adj, adjR, node1, node2);
			if (bidiResult.dist == Integer.MAX_VALUE) {
				System.out.println("Bi");
				i--;
				continue;
			}
			endTime = System.currentTimeMillis();
			long elapsedBi = (endTime - startTime);
			
			startTime = System.currentTimeMillis();
			SearchPair starResult = star.search(adj, node1, node2);
			if (starResult.dist == Integer.MAX_VALUE) {
				System.out.println("Star");
				i--;
				continue;
			}
			endTime = System.currentTimeMillis();
			long elapsedA = (endTime - startTime);
			
			writeFile(node1, node2, elapsedCH, elapsedDijk, elapsedBi, elapsedA, chResult, dijkResult, bidiResult, starResult);
			System.out.println(i);
		}	
	}
	
	public void writeFile(int node1, int node2, long elapsedCH, long elapsedDijk, long elapsedBi, long elapsedA, 
			SearchPair chResult, SearchPair dijkResult, SearchPair bidiResult, SearchPair starResult) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter("results/dijkstraTimes.txt", true))) {
			writer.write(node1 + "\t" + node2 + "\t" + dijkResult.dist + "\t" + elapsedDijk + "\t" + dijkResult.settled);
			writer.newLine();
		} catch (IOException e) {
			System.err.format("IOException: %s%n", e);
		}
		
		try (BufferedWriter writer = new BufferedWriter(new FileWriter("results/bidijkstraTimes.txt", true))) {
			writer.write(node1 + "\t" + node2 + "\t" + bidiResult.dist + "\t" + elapsedBi + "\t" + bidiResult.settled);
			writer.newLine();
		} catch (IOException e) {
			System.err.format("IOException: %s%n", e);
		}
		
		try (BufferedWriter writer = new BufferedWriter(new FileWriter("results/aStarTimes.txt", true))) {
			writer.write(node1 + "\t" + node2 + "\t" + starResult.dist + "\t" + elapsedA + "\t" + starResult.settled);
			writer.newLine();
		} catch (IOException e) {
			System.err.format("IOException: %s%n", e);
		}
		
		try (BufferedWriter writer = new BufferedWriter(new FileWriter("results/chSearchTimes.txt", true))) {
			int dist;
			if (chResult.dist != Integer.MAX_VALUE) {
				dist = bidiResult.dist;
			} else {
				dist = Integer.MAX_VALUE;
			}
			writer.write(node1 + "\t" + node2 + "\t" + dist + "\t" + elapsedCH + "\t" + chResult.settled);
			writer.newLine();
		} catch (IOException e) {
			System.err.format("IOException: %s%n", e);
		}
	}
	
	public void setupFiles() {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter("results/dijkstraTimes.txt", true))) {
			writer.write("Node1 \tNode2 \tDistance\t Time\t Settled");
			writer.newLine();
		} catch (IOException e) {
			System.err.format("IOException: %s%n", e);
		}
		try (BufferedWriter writer = new BufferedWriter(new FileWriter("results/bidijkstraTimes.txt", true))) {
			writer.write("Node1 \tNode2 \tDistance\t Time\t Settled");
			writer.newLine();
		} catch (IOException e) {
			System.err.format("IOException: %s%n", e);
		}
		try (BufferedWriter writer = new BufferedWriter(new FileWriter("results/aStarTimes.txt", true))) {
			writer.write("Node1 \tNode2 \tDistance\t Time\t Settled");
			writer.newLine();
		} catch (IOException e) {
			System.err.format("IOException: %s%n", e);
		}
		try (BufferedWriter writer = new BufferedWriter(new FileWriter("results/chSearchTimes.txt", true))) {
			writer.write("Node1 \tNode2 \tDistance\t Time\t Settled");
			writer.newLine();
		} catch (IOException e) {
			System.err.format("IOException: %s%n", e);
		}
	}
}
