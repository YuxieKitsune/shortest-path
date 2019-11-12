package ch;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import common.Node;

public class CHFileReader {

	public static CHContainer readCsv(String fileName) {
		List<List<String>> data = new ArrayList<>();
		List<List<Node>> adj = new ArrayList<List<Node>>();
		List<List<Node>> adjR = new ArrayList<List<Node>>();
		
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] values = line.split("\t");
				data.add(Arrays.asList(values));
//				List<Node> nodeList = new ArrayList<Node>();
//				int totalNodes = Integer.parseInt(values[1]);
//				for (int i = 0; i < totalNodes; i++) {
//					Node currentNeighbour = new Node();
//				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int totalNodes = Integer.parseInt(data.get(0).get(0));
		for (int i = 0; i < totalNodes; i++) {
			List<Node> item = new ArrayList<Node>();
			List<Node> itemR = new ArrayList<Node>();
			adj.add(item);
			adjR.add(itemR);
		}
		
		int[] priority = new int[totalNodes];
		for (int i = 1; i < data.size(); i++) {
			int neighbourCount = Integer.parseInt(data.get(i).get(1));
			for (int j = 0; j < neighbourCount; j++) {
				int neighbour = Integer.parseInt(data.get(i).get(2 + (j * 3)));
				int dist = (int) Double.parseDouble(data.get(i).get(3 + (j * 3)));
				priority[neighbour] = Integer.parseInt(data.get(i).get(4 + (j * 3)));
				adj.get(i - 1).add(new Node(neighbour, dist));
				adjR.get(neighbour).add(new Node(i - 1, dist));
			}
		}
		
		return new CHContainer(totalNodes, adj, adjR, priority);
	}
	
}
