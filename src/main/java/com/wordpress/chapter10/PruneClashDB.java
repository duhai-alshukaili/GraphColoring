// FileName: BuildGraph.java
// Date    : 8-10-2017

package com.wordpress.chapter10;

import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import edu.princeton.cs.algs4.Edge;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import edu.princeton.cs.algs4.EdgeWeightedGraph;
import edu.princeton.cs.algs4.Graph;
import edu.princeton.cs.algs4.ST;

/**
 * 
 * @author Duhai Alshukaili
 * 
 */
public class PruneClashDB {

	public static void main(String args[]) {

		try {
	
			
			
			
			ST<String, Integer> nodeMap = new ST<String, Integer>();
			ST<Integer, String> revNodeMap = new ST<Integer, String>();
			ST<String, String> courseNameMap = new ST<String,String>();
			
			
			FileReader in = new FileReader("clashdb.csv");


			Iterable<CSVRecord> records = 
					CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
			       /*
			       //.withHeader(
					"Academic Year","Department Name","Main Course No",
					"Main course Name","Detail Course No",
					"Detail Course Name","Total Students"
					).parse(in);*/
			
			PrintWriter out = new PrintWriter("clashdb_pruned.csv");
			
			CSVPrinter printer = CSVFormat.RFC4180.withHeader(
					"Academic Year","Department Name","Main Course No",
					"Main course Name","Detail Course No",
					"Detail Course Name","Total Students"
					).print(out);
			
			ArrayList<String> clashes = new ArrayList<String>();
			
			int count = 0; // count the distinct course names
			for (CSVRecord record : records) {
				String mainCourse = record.get("Main Course No");
				String detailCourse = record.get("Detail Course No");
				
				StringBuilder b1 = new StringBuilder();
				StringBuilder b2 = new StringBuilder();
				
				b1.append(mainCourse).append("-").append(detailCourse);
				b2.append(detailCourse).append("-").append(mainCourse);
				
				if (clashes.contains(b1.toString()) || 
					clashes.contains(b2.toString()))
					continue;
				
				clashes.add(b1.toString());
				
				printer.printRecord(record);
								
			}
			
			in.close();
			out.close();

			
			
		} catch (IOException ex) {
			ex.printStackTrace(System.err);
		}

	}

}
