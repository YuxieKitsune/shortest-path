package tests;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import common.Coords;
import common.GraphReader;
import common.Node;
import common.SearchPair;
import star.AStar;

public class EuclideanComparison {
	private static List<List<String>> data;
	private static AStar star;
	private static List<List<Node>> adj;
	private static List<Coords> coords;
	
	@Before
	public void setup() {
		data = GraphReader.readCsv("beijing");
		int totalNodes = Integer.parseInt(data.get(0).get(0));
		
		adj = new ArrayList<List<Node>>();
		for (int i = 0; i < totalNodes; i++) {
			List<Node> item = new ArrayList<Node>();
			adj.add(item);
		}
		
		coords = new ArrayList<Coords>();
		for (int i = 0; i < totalNodes; i++) {
			double lon = Double.parseDouble(data.get(i).get(3));
			double lat = Double.parseDouble(data.get(i).get(2));
			coords.add(new Coords(lon, lat));
			
			int neighbourCount = Integer.parseInt(data.get(i + 1).get(6));
			for (int j = 0; j < neighbourCount; j++) {
				int neighbour = Integer.parseInt(data.get(i + 1).get(7 + (j * 2)));
				int dist = Integer.parseInt(data.get(i + 1).get(8 + (j * 2)));
				adj.get(i).add(new Node(neighbour, dist));
			}
		}
		star = new AStar(totalNodes, coords);
	}
	
	@Test
	public void testStar(){
		List<List<String>> txtData = new ArrayList<>();
		List<List<Integer>> nodes = new ArrayList<>();
		
		try (BufferedReader br = new BufferedReader(new FileReader("results/aStarTimes.txt"))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] values = line.split("\t");
				txtData.add(Arrays.asList(values));
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for (int i = 1; i < txtData.size(); i++) {
			List<Integer> list = new ArrayList<Integer>();
			int node1 = Integer.parseInt(txtData.get(i).get(0));
			int node2 = Integer.parseInt(txtData.get(i).get(1));
			list.add(node1);
			list.add(node2);
			nodes.add(list);
		}
		
		try (BufferedWriter writer = new BufferedWriter(new FileWriter("results/star/euclidean.txt", true))) {
			writer.write("Node1 \tNode2 \tDistance\t Time\t Settled");
			writer.newLine();
		} catch (IOException e) {
			System.err.format("IOException: %s%n", e);
		}
		
		System.out.println("Starting A* Search...");
		for (int i = 0; i < nodes.size(); i++) {
			long startTime = System.currentTimeMillis();
			SearchPair result = star.search(adj, nodes.get(i).get(0), nodes.get(i).get(1));
			long endTime = System.currentTimeMillis();
			long time = endTime - startTime;
			try (BufferedWriter writer = new BufferedWriter(new FileWriter("results/star/euclidean.txt", true))) {
				writer.write(nodes.get(i).get(0) + "\t" + nodes.get(i).get(1) + "\t" + result.dist + "\t" + time + "\t" + result.settled);
				writer.newLine();
			} catch (IOException e) {
				System.err.format("IOException: %s%n", e);
			}
			System.out.println(i);
		}

		//System.out.println("Process Time: " + (endTime - startTime));
	}
}
