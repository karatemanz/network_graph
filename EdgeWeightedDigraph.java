/*************************************************************************
 *  Compilation:  javac EdgeWeightedDigraph.java
 *  Execution:    java EdgeWeightedDigraph V E
 *  Dependencies: Bag.java DirectedEdge.java
 *
 *  An edge-weighted digraph, implemented using adjacency lists.
 *
 *************************************************************************/
import java.util.*;
import Helpers.*;

/**
 *  The <tt>EdgeWeightedDigraph</tt> class represents an directed graph of vertices
 *  named 0 through V-1, where each edge has a real-valued weight.
 *  It supports the following operations: add an edge to the graph,
 *  iterate over all of edges leaving a vertex.
 *  Parallel edges and self-loops are permitted.
 *  <p>
 *  For additional documentation, see <a href="http://algs4.cs.princeton.edu/44sp">Section 4.4</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 */

public class EdgeWeightedDigraph{
    private final int V;
    private int E;
    //private Bag<DirectedEdge>[] adj;
    private ArrayList<DirectedEdge>[] adj;
    private ArrayList<Vertex> vertice;
    
    /**
     * Create an empty edge-weighted digraph with V vertices.
     */
    public EdgeWeightedDigraph(int V) {
        if (V < 0) throw new RuntimeException("Number of vertices must be nonnegative");
        this.V = V;
        this.E = 0;
        //adj = (Bag<DirectedEdge>[]) new Bag[V];
        adj = (ArrayList<DirectedEdge>[]) new ArrayList[V];
        vertice = new ArrayList<Vertex>();
        
        for (int v = 0; v < V; v++){ 
        	//adj[v] = new Bag<DirectedEdge>();
        	adj[v] = new ArrayList<DirectedEdge>();
        	vertice.add(new Vertex(v));
        	
        }
    }

   /**
     * Create a edge-weighted digraph with V vertices and E edges.
     */
    public EdgeWeightedDigraph(int V, int E) {
        this(V);
        if (E < 0) throw new RuntimeException("Number of edges must be nonnegative");
        for (int i = 0; i < E; i++) {
            int v = (int) (Math.random() * V);
            int w = (int) (Math.random() * V);
            double weight = Math.round(100 * Math.random()) / 100.0;
            DirectedEdge e = new DirectedEdge(v, w, weight);
            addEdge(e);
        }
    }

    /**
     * Create an edge-weighted digraph from input stream.
     */
    public EdgeWeightedDigraph(In in) {
        this(in.readInt());
        int E = in.readInt();
        for (int i = 0; i < E; i++) {
            int v = in.readInt();
            int w = in.readInt();
            double weight = in.readDouble();
            addEdge(new DirectedEdge(v, w, weight));
            // bidirectional edge added
            addEdge(new DirectedEdge(w, v, weight));
        }
    }


   /**
     * Return the number of vertices in this digraph.
     */
    public int V() {
        return V;
    }

   /**
     * Return the number of edges in this digraph.
     */
    public int E() {
        return E;
    }

    public Vertex getVertex(int index){
    	
    	return vertice.get(index);
    	
    }
    
   /**
     * Add the edge e to this digraph.
     */
    public void addEdge(DirectedEdge e) {
        int v = e.from();
        adj[v].add(e);
        E++;
    }

    // remove edge from digraph
    public void removeEdge(DirectedEdge e) {  	
    	
    	int v = e.from();
    	adj[v].remove(e);
    	E--;
    	
    }
    
    // changes the given DirectedEdges weight
    public void changeEdgeWeight(DirectedEdge oldEdge, double newWeight){
    	
    	oldEdge.changeWeight(newWeight);
    	
    }
    
    // checks for existance of an edge in the graph
    public boolean hasEdge(DirectedEdge e){
    	
    	for (int v = 0; v < V; v++) {
            for (DirectedEdge match : adj(v)) {

            	if(match.to() == e.to() && match.from() == match.from()){ 
            		return true;  
            	}
            	
            }
        }
    	
    	return false;
    	
    }
    
    // gets edge that matches to and from
    public DirectedEdge getMatchEdge(DirectedEdge e){
    	
    	for (int v = 0; v < V; v++) {
            for (DirectedEdge match : adj(v)) {

            	if(match.to() == e.to() && match.from() == match.from()){ 
            		return match;  
            	}
            	
            }
        }
    	
    	return null;
    	
    }
    
    public DirectedEdge getComplement(DirectedEdge e){
    	
    	for (int v = 0; v < V; v++) {
            for (DirectedEdge comp : adj(v)) {

            	if(comp.to() == e.from() && comp.from() == e.to()){ 
            		return comp;  
            	}
            	
            }
        }
 
    	return null;
    }
    
    // returns up vertices
    public Iterable<Vertex> upordown(){
    	
    	return vertice;
    }
    
   /**
     * Return the edges leaving vertex v as an Iterable.
     * To iterate over the edges leaving vertex v, use foreach notation:
     * <tt>for (DirectedEdge e : graph.adj(v))</tt>.
     */
    public Iterable<DirectedEdge> adj(int v) {
        return adj[v];
    }

   /**
     * Return all edges in this graph as an Iterable.
     * To iterate over the edges, use foreach notation:
     * <tt>for (DirectedEdge e : graph.edges())</tt>.
     */
    public Iterable<DirectedEdge> edges() {
        Bag<DirectedEdge> list = new Bag<DirectedEdge>();
        for (int v = 0; v < V; v++) {
            for (DirectedEdge e : adj(v)) {
                list.add(e);
            }
        }
        return list;
    } 
      
   /**
     * Return number of edges leaving v.
     */
    public int outdegree(int v) {
        return adj[v].size();
    }

    // return the adjacency list of v
    public ArrayList<DirectedEdge> list(int v){
    	
    	return adj[v];
    }
    

   /**
     * Return a string representation of this graph.
     */
    public String toString() {
        String NEWLINE = System.getProperty("line.separator");
        StringBuilder s = new StringBuilder();
        for (int v = 0; v < V; v++) {
        	if(this.getVertex(v).status()){
            s.append(v + ": ");
        	}
            for (DirectedEdge e : adj[v]) {
            	if(e.status()){
                s.append(e + "  ");
            	}
            }
            s.append(NEWLINE);
        }
        return s.toString();
    }

    /**
     * Test client.
     */
    public static void main(String[] args) {
        int V = Integer.parseInt(args[0]);
        int E = Integer.parseInt(args[1]);
        EdgeWeightedDigraph G = new EdgeWeightedDigraph(V, E);
        StdOut.println(G);
    }

}
