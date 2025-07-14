// FileName: BuildGraph.java
// Date    : 8-10-2017

package com.wordpress.chapter10;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import edu.princeton.cs.algs4.Edge;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import edu.princeton.cs.algs4.EdgeWeightedGraph;
import edu.princeton.cs.algs4.Graph;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.StdOut;

/**
 * 
 * @author Duhai Alshukaili
 * 
 */
public class BuildGraph {
	
	private ST<String, Integer> nodeMap;      // course id -> node id
	private ST<Integer, String> revNodeMap;   // node id -> course id
	private ST<String, String> courseNameMap; // course id - courseName 
	
	private int count; // keep track of distinct courses 
	
	
	EdgeWeightedGraph clashes; // saves the weights
	Graph G; // no weights
	
	
	WelshPowell wp;
	
	/**
	 * 
	 * @param filePath
	 * @throws IOException 
	 */
	public BuildGraph(String filePath) throws IOException {
		nodeMap = new ST<String, Integer>();
		revNodeMap = new ST<Integer, String>();
		courseNameMap = new ST<String, String>();
		
		count = 0;
		

		
		FileReader in = new FileReader("clashdb_pruned.csv");
		
		pass1(in);
		
		in.reset();
		
		clashes = new EdgeWeightedGraph(count); // count is updated inside pass1
		G = new Graph(count);
		
		pass2(in);
		
		in.close();
		
		
		wp = new WelshPowell(G);
	}
	
	
	/**
	 * // First pass through clashdb file to collect the names and ids of the courses.
	 * 
	 * @param in
	 * @throws IOException
	 */
	private void pass1(FileReader in) throws IOException {
		
		Iterable<CSVRecord> records = 
				CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(in);
		
		for (CSVRecord record : records) {
			String mainCourse = record.get("Main Course No");
			String mainCourseName = record.get("Main course Name");
			String detailCourse = record.get("Detail Course No");
			String detailCourseName = record.get("Detail Course Name");
			// String studentNo = record.get("Total Students");

			// add mainCourse to nodeMap
			if (!nodeMap.contains(mainCourse)) {
				nodeMap.put(mainCourse, count);
				revNodeMap.put(count, mainCourse);
				count++;
			}

			// add detailCourse to nodeMap
			if (!nodeMap.contains(detailCourse)) {
				nodeMap.put(detailCourse, count);
				revNodeMap.put(count, detailCourse);
				count++;
			}

			if (!courseNameMap.contains(mainCourse))
				courseNameMap.put(mainCourse, mainCourseName);

			if (!courseNameMap.contains(detailCourse))
				courseNameMap.put(detailCourse, detailCourseName);
		}
	}
	
	
	private void pass2(FileReader in) throws IOException {
		Iterable<CSVRecord> records = 
				CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(in);

		for (CSVRecord record : records) {
			String mainCourse = record.get("Main Course No");
			String mainCourseName = record.get("Main course Name");
			String detailCourse = record.get("Detail Course No");
			String detailCourseName = record.get("Detail Course Name");
			Double studentNo = Double.parseDouble(record.get("Total Students"));

			clashes.addEdge(new Edge(nodeMap.get(mainCourse), nodeMap.get(detailCourse), studentNo));

			G.addEdge(nodeMap.get(mainCourse), nodeMap.get(detailCourse));

		}
	}

	public static void main(String args[]) {

		try {

			
			ST<String, Integer> nodeMap 
				= new ST<String, Integer>();     // course id -> node id
			
			ST<Integer, String> revNodeMap 
				= new ST<Integer, String>();     // node id -> course id
			
			ST<String, String> courseNameMap 
				= new ST<String, String>();		

			FileReader in = new FileReader("clashdb_pruned.csv");

			Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(in);
			/*
			 * //.withHeader( "Academic Year","Department Name","Main Course No",
			 * "Main course Name","Detail Course No", "Detail Course Name","Total Students"
			 * ).parse(in);
			 */

			// First pass through clashdb file to collect the names and ids of the courses.
			int count = 0; // count the distinct course names
			for (CSVRecord record : records) {
				String mainCourse = record.get("Main Course No");
				String mainCourseName = record.get("Main course Name");
				String detailCourse = record.get("Detail Course No");
				String detailCourseName = record.get("Detail Course Name");
				// String studentNo = record.get("Total Students");

				// add mainCourse to nodeMap
				if (!nodeMap.contains(mainCourse)) {
					nodeMap.put(mainCourse, count);
					revNodeMap.put(count, mainCourse);
					count++;
				}

				// add detailCourse to nodeMap
				if (!nodeMap.contains(detailCourse)) {
					nodeMap.put(detailCourse, count);
					revNodeMap.put(count, detailCourse);
					count++;
				}

				if (!courseNameMap.contains(mainCourse))
					courseNameMap.put(mainCourse, mainCourseName);

				if (!courseNameMap.contains(detailCourse))
					courseNameMap.put(detailCourse, detailCourseName);

			}

			in.close();
			
			// graph structure 
			EdgeWeightedGraph clashes = new EdgeWeightedGraph(count); // saves the weights
			Graph G = new Graph(count); // no weights

			// 2nd pass
			in = new FileReader("clashdb_pruned.csv");
			records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(in);

			for (CSVRecord record : records) {
				String mainCourse = record.get("Main Course No");
				String mainCourseName = record.get("Main course Name");
				String detailCourse = record.get("Detail Course No");
				String detailCourseName = record.get("Detail Course Name");
				Double studentNo = Double.parseDouble(record.get("Total Students"));

				clashes.addEdge(new Edge(nodeMap.get(mainCourse), nodeMap.get(detailCourse), studentNo));

				G.addEdge(nodeMap.get(mainCourse), nodeMap.get(detailCourse));

			}

			in.close();

			/*
			 * int queryNode = nodeMap.get("ITBS2203");
			 * 
			 * System.out.printf("Course Clashing with %s: \n", "ITBS2203");
			 * 
			 * for (Edge edge: clashes.adj(queryNode)) {
			 * 
			 * int other = edge.other(queryNode); String otherName = revNodeMap.get(other);
			 * 
			 * System.out.printf("%9s%4d\n", otherName, (int)edge.weight()); }
			 */

			/*
			int queryNode = nodeMap.get("ITBS2203");

			System.out.printf("\n\nCourse Clashing with %s (with counts): \n", "ITBS2203");

			for (Edge edge : clashes.adj(queryNode)) {

				int other = edge.other(queryNode);
				String otherName = revNodeMap.get(other);

				System.out.printf("%9s%4d\n", otherName, (int) edge.weight());
			}
			
			System.out.printf("\n\nCourse Clashing with %s (no   counts): \n", "ITBS2203");

			for (Integer other : G.adj(queryNode)) {

				String otherName = revNodeMap.get(other);

				System.out.printf("%9s\n", otherName);
			}
			*/

			WelshPowell wp = new WelshPowell(G);
			System.out.println("done");
			
			for (int i = 0; i < count; i++) {
				StdOut.printf("%s %s, %d\n", revNodeMap.get(i), 
						                  courseNameMap.get(revNodeMap.get(i)), 
						                  wp.getColor(i));
			}
			
			
			

		} catch (IOException ex) {
			ex.printStackTrace(System.err);
		}

	}

}
