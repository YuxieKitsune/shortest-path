package tests;

import java.util.*;
import org.junit.*;

import ch.CHBidirection;
import ch.CHContainer;
import ch.CHFileReader;

public class CHSearchTest {
	
	@Test
	public void testSearch(){
		CHContainer container = CHFileReader.readCsv("CHBeijing70s.txt");
		CHBidirection chb = new CHBidirection(container.V);
		System.out.println("Starting Bidirectional Search (CH)...");
		long startTime = System.currentTimeMillis();
		chb.search(container.adj, container.adjR, container.priority, 260, 2000);
		long endTime = System.currentTimeMillis();
		System.out.println("Process Time: " + (endTime - startTime) + " milliseconds");
	}
}
