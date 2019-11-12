package ch;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import common.Node;

public class CHFileWriter {
	
	public static void createGraphFile(List<List<Node>> adj, int[] priority) throws IOException {
		File outFile = new File("CHBeijingE.txt");
		
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(outFile))) {
			writer.write(Integer.toString(adj.size()));
			writer.newLine();
			
			for (int i = 0; i < adj.size(); i++) {
				String thisLine = Integer.toString(i);
				thisLine += "\t" + Integer.toString(adj.get(i).size());
				for (int j = 0; j < adj.get(i).size(); j++) {
					Node current = adj.get(i).get(j);
					thisLine += "\t" + Integer.toString(current.nodeid);
					thisLine += "\t" + Double.toString(current.cost);
					thisLine += "\t" + Integer.toString(priority[current.nodeid]);
				}
				
				writer.write(thisLine);
				writer.newLine();
			}
			writer.close();			
		} catch (IOException e) {
			System.err.format("IOException: %s%n", e);
		}
//		
//		
//		try (FileWriter writer = new FileWriter("CHBeijing");
//				BufferedWriter bw = new BufferedWriter(writer);) {
//			
//			bw.write(Integer.toString(adj.size()));
//			
//			for (int i = 0; i < adj.size(); i++) {
//				String thisLine = Integer.toString(i);
//				thisLine += "\t" + Integer.toString(adj.get(i).size());
//				for (int j = 0; j < adj.get(i).size(); j++) {
//					Node current = adj.get(i).get(j);
//					thisLine += "\t" + Integer.toString(current.nodeid);
//					thisLine += "\t" + Integer.toString(current.cost);
//					thisLine += "\t" + Integer.toString(priorities[current.nodeid]);
//				}
//				
//				bw.write(thisLine);
//			}
//			
//		} catch (IOException e) {
//			System.err.format("IOException: %s%n", e);
//		}
	}
	
}
