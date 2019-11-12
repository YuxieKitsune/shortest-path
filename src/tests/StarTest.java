package tests;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.*;
import common.Coords;
import common.GraphReader;
import common.Node;
import star.AStar;

public class StarTest {
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
		System.out.println("Starting A* Search...");
		long startTime = System.currentTimeMillis();
		star.search(adj, 70, 2000);
		long endTime = System.currentTimeMillis();
		System.out.println("Process Time: " + (endTime - startTime));
	}
}
