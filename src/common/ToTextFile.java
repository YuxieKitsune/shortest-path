package common;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

public class ToTextFile {
	private static List<List<String>> data;
	private static List<Coords> coords;
	
	@Test
    public void textFile() {
    	data = GraphReader.readCsv("beijing");
		int totalNodes = Integer.parseInt(data.get(0).get(0));
		
		coords = new ArrayList<Coords>();
		for (int i = 0; i < totalNodes; i++) {
			double lon = Double.parseDouble(data.get(i).get(3));
			double lat = Double.parseDouble(data.get(i).get(2));
			coords.add(new Coords(lon, lat));
		}
		
		try (BufferedWriter writer = new BufferedWriter(new FileWriter("beijingNodesOnly.txt"))) {
			writer.write("Node\tLogitude\tLatitude");
			writer.newLine();
			
			for (int i = 0; i < totalNodes; i++) {			
				writer.write(i + "\t" + coords.get(i).longitude + "\t" + coords.get(i).latitude);
				writer.newLine();
			}
			writer.close();			
		} catch (IOException e) {
			System.err.format("IOException: %s%n", e);
		}
    }
}
