package com.wordpress.chapter10;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*  Input: Graph data stored in a txt file with the following format e.g data.txt
 *  13 vertices, 13 edges
 *  0: 6 2 1 5 
 *  1: 0 
 *  2: 0 
 *  3: 5 4 
 *  4: 5 6 3 
 *  5: 3 4 0 
 *  6: 0 4 
 *  7: 8 
 *  8: 7 
 *  9: 11 10 12 
 *  10: 9 
 *  11: 9 12 
 *  12: 11 9 
*/
public class WelshPowellNW {

    private List<Vertex> vertices;
    
    public WelshPowellNW(String _path){
        vertices = new ArrayList<Vertex>();
        ArrayList<String> lines = (ArrayList<String>) readGraphData(_path);
        
        if (lines != null){
            for (int i = 1; i < lines.size(); i++){
                String node = lines.get(i).split(":")[0];
                String[] adj = lines.get(i).split(":")[1].split(" ");
                
                List<String> neighbors = new ArrayList<String>();
                for (int j = 1; j < adj.length; j++){
                    neighbors.add(adj[j]);
                }
                vertices.add(new Vertex(node, new ArrayList<String>(neighbors)));
            }   
        }
        
    }
    
    @Override
    public String toString() {
        String result = "";
        for (Vertex v: vertices){
            result += v.node + ":" + v.neighbors.toString() + "\n";
        }
        return result;
    }
    /*
     * Implementation of the Welsh-Powell Algorithm
     */
    public void colourVertices(){
        Collections.sort(vertices, new VertexComparator()); // arrange vertices in order of descending valence
        Map<String, String> vertex_color_index = new HashMap<String, String>(); //create Map<Vertex, Color>
        for (int i = 0; i < vertices.size(); i++){
            if ((vertex_color_index.containsKey(vertices.get(i).node))){  
            	System.out.println("vertex " + i + " has been colored");
                continue;
            }
            else{
            	System.out.println("now coloring vertex " + i);
            	
                vertex_color_index.put(vertices.get(i).node, "Colour " + i); //color first vertex in list with color 1
                
                for (int j = i+1; j < vertices.size(); j++){
                    if (!(vertices.get(i).neighbors.contains(vertices.get(j).node)) && !(vertex_color_index.containsKey(vertices.get(j).node))){
                        vertex_color_index.put(vertices.get(j).node, "Colour " + i);
                    }
                    else{
                        continue;
                    }
                }
            }   
        }
        System.out.println(vertex_color_index);
        
    }
    
    private List<String> readGraphData(String _path){
        Path path = FileSystems.getDefault().getPath(_path, "");
        try {
            return Files.readAllLines(path, Charset.defaultCharset());
        } catch (IOException e) {
            System.err.println("I/O Error");
            return null;
        }
    }
    
    
    
    private static class Vertex{
        private String node;
        private List<String> neighbors;
        
        public Vertex(String node, ArrayList<String> neighbors){
            this.node = node;
            this.neighbors = neighbors;
        }
        
    }
    
    class VertexComparator implements Comparator<Vertex>{

        public int compare(Vertex a, Vertex b) {
            return a.neighbors.size() < b.neighbors.size() ? 1 : a.neighbors.size() == b.neighbors.size() ? 0 : -1;
        }
        
    }
    
    public static void main(String[] args){
        WelshPowellNW graph = new WelshPowellNW("data/graph1.txt");
        System.out.println(graph.toString());
        graph.colourVertices();
    }
}