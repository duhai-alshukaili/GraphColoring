package com.wordpress.chapter10;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import edu.princeton.cs.algs4.Graph;
import edu.princeton.cs.algs4.In;

/**
 * This class implements the WelshPowell algorithm for graph coloring
 * (<a href="http://bit.ly/2loBN7a">Vertex Coloring: Welsh-Powell Algorithm</a>
 * for details).
 * 
 * Graph Coloring is the assignment of labels, called colors, to the vertices 
 * in graph such that no two adjacent vertices share the same color. 
 * 
 * The chromatic number of a graph is the minimal number of colors for which such
 * and assignment is possible. 
 * 
 * One of the applications of graph coloring is timetabling. In a university exams
 * timetabling problem, you want to assign exams to slots/days where student 
 * does not have two exams in the same slot. 
 * 
 * In such a graph, nodes are subjects and edges are clashes between subjects. 
 * A clash between two subjects exists if at least one students take 
 * both subjects. 
 * 
 * @author Duhai Alshukaili
 *
 */
public class WelshPowell {
	
	
	// Map<Vertex, Color>
	private HashMap<Integer, Integer> vertexColors;
	
	// the current color
	private int color;
	
	
	// the graph to be process
	private Graph G;
	
	/**
	 * Color the vertices in the graph {@code G} using the WelshPowell algorithm<br>
	 * see <a href="http://bit.ly/2loBN7a">Vertex Coloring: Welsh-Powell Algorithm</a>
	 * for details.
	 * 
	 * @param G the graph
	 */
	public WelshPowell(Graph G) {
		
		this.vertexColors = new HashMap<Integer, Integer>();
		
		this.color = -1;
		
		this.G = G;
		
		colorVertices();
	}
	
	
	/**
	 * Run the Welsh-Powell algorithm of the graph {@code G}
	 */
	private void colorVertices() {
		
		/*
		System.out.println("Before Sorting: ");
		for (int v = 0; v < G.V(); v++)
			System.out.printf("%4d%4d\n", v, G.degree(v));
		*/
		
		// add all vertices to an list
		ArrayList<Integer> vertices = new ArrayList<Integer>();
		for (int v = 0; v < G.V(); v++)
			vertices.add(v);
		
		
		// Step 1 in Welsh-Powell: 
		// Sort the vertices in order of descending valence 
		Collections.sort(vertices, new VertexComparator(G));
		
		/*
			System.out.println("\n\nAfter Sorting: ");
			for (int v: vertices) {
				System.out.printf("%4d%4d\n", v, G.degree(v));
			}
		*/
		
		
		// Setp 2:
		// now we run the the coloring loop
		for (int i = 0; i < vertices.size(); i++) {
			int vi = vertices.get(i);
			
			
			if (!vertexColors.containsKey(vi)) { // if vi is not colored
				
				int c = nextColor();
				
				// color vi with c
				vertexColors.put(vi, c);
				System.out.println("Coloring " + vi + " with " + c);
				
				// go down the list of the remaining vertices 
				for (int j = i + 1; j < vertices.size(); j++) {
					
					int vj = vertices.get(j);
					
					if (!vertexColors.containsKey(vj)  
							&&
						!inNeighbourhoodOf(vj,c) ) {
						
						// color vj with c
						vertexColors.put(vj, c);
						System.out.println("Coloring " + vj + " with " + c);
						
					} // end if vj
						
					
				} // end for j
				
			} // end if vi
			
		} // end for  i
	}
	
	
	/**
	 * This method returns {@code true} of the {@code vertex} is in connected to some other 
	 * vertex in the graph {@code G} where the other vertex is colored with {@code color}
	 *  
	 * @param vertex the vertex to check
	 * @param color the color of the neighbourhood vertices to be checked
	 * @return {@code true} of one of the neighbourhood vertices is colored wiht {@code color}
	 */
	private boolean inNeighbourhoodOf(int vertex, int color) {
		
		
		for (int v: G.adj(vertex)) {
			if (vertexColors.containsKey(v) 
					&& vertexColors.get(v) == color)
				return true;
		}
		
		return false;
	}
	
	
	/**
	 * Pick the next available unsed color. In this implementation 
	 * simple increment the value of {@code color}
	 * @return
	 */
	private int nextColor() {
		color++;
		
		return color;
	}
	
	
	/**
	 * Returns the number of colors needed to color the vertices of {@code G}
	 * @return the chromatic number
	 */
	public int chromaticNumber() {
		return color;
	}
	
	/**
	 * Return the color of assigned to {@code vertex}
	 * 
	 * @param vertex a vertex in the graph {@code G}
	 * @return the color of {@code vertex}
	 */
	public int getColor(int vertex) {
		
		if (vertex < 0 || vertex >= G.V())
			throw new IndexOutOfBoundsException("vertex " + vertex + 
					" is not between 0 and " + (G.V()-1));
		
		return vertexColors.get(vertex);
	}
	
	
	/**
	 * Test this class
	 * @param args
	 */
	public static void main(String args[]) {
		
		Graph G = new Graph(new In("data/tinyG.txt"));
		
		System.out.println(G.toString());
		
		WelshPowell wp = new WelshPowell(G);
		
		System.out.println("done");
	}
	
	/**
	 * 
	 * @author Duhai Alshukaili
	 *
	 */
	private class VertexComparator implements Comparator<Integer> {
		
		private Graph G;
		
		public VertexComparator(Graph G) {
			this.G = G;
		}
		
		public int compare(Integer v1, Integer v2) {
			return G.degree(v1) < G.degree(v2) ? 
					1 : G.degree(v1) == G.degree(v2) ? 0 : -1;
		}
		
	}

}
