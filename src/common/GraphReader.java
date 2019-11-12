package common;

import java.io.*;
import java.util.*;

public class GraphReader {

	public static List<List<String>> readCsv(String fileName) {
		List<List<String>> data = new ArrayList<>();
		
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] values = line.split("\t");
				data.add(Arrays.asList(values));
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return data;
	}
	
}
