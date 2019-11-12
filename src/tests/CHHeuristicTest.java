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

import ch.CHBidirection;
import ch.CHFileReader;
import ch.CHContainer;
import common.Coords;
import common.GraphReader;
import common.Node;
import common.SearchPair;
import star.AStar;

public class CHHeuristicTest {
	private static CHBidirection chb;
	private static CHContainer container;
	
	@Before
	public void setup() {
		
	}
	
	@Test
	public void testStar(){
		container = CHFileReader.readCsv("CHBeijingED.txt");
		chb = new CHBidirection(container.V);
		List<List<String>> txtData = new ArrayList<>();
		List<List<Integer>> nodes = new ArrayList<>();
		
		try (BufferedReader br = new BufferedReader(new FileReader("results/chSearchTimes.txt"))) {
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
		
		try (BufferedWriter writer = new BufferedWriter(new FileWriter("results/ch/ed.txt", true))) {
			writer.write("Node1 \tNode2 \tDistance\t Time\t Settled");
			writer.newLine();
		} catch (IOException e) {
			System.err.format("IOException: %s%n", e);
		}
		
		System.out.println("Starting CH Search...");
		for (int i = 0; i < nodes.size(); i++) {
			long startTime = System.currentTimeMillis();
			SearchPair result = chb.search(container.adj, container.adjR, container.priority, nodes.get(i).get(0), nodes.get(i).get(1));
			long endTime = System.currentTimeMillis();
			long time = endTime - startTime;
			try (BufferedWriter writer = new BufferedWriter(new FileWriter("results/ch/ed.txt", true))) {
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
