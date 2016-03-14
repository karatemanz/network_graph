/*************************************************************************
 *  Compilation:  javac DirectedEdge.java
 *  Execution:    java DirectedEdge
 *
 *  Immutable weighted directed edge.
 *
 *************************************************************************/
import Helpers.*;

/**
 *  The <tt>DirectedEdge</tt> class represents a weighted edge in an directed graph.
 *  <p>
 *  For additional documentation, see <a href="http://algs4.cs.princeton.edu/44sp">Section 4.4</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 */

public class DirectedEdge{
	
    private final int v;
    private final int w;
    private double weight;
    private boolean status;

   /**
     * Create a directed edge from v to w with given weight.
     */
    public DirectedEdge(int v, int w, double weight) {
    	
        this.v = v;
        this.w = w;
        this.weight = weight;
        this.status = true;
        
    }

   /**
     * Return the vertex where this edge begins.
     */
    public int from() {
        return v;
    }

   /**
     * Return the vertex where this edge ends.
     */
    public int to() {
        return w;
    }

    /**
     * Return either endpoint of this edge.
     */
    public int either() {
        return v;
    }
    
    /**
     * Return the endpoint of this edge that is different from the given vertex
     * (unless a self-loop).
     */
    public int other(int vertex) {
        if      (vertex == v) return w;
        else if (vertex == w) return v;
        else throw new RuntimeException("Illegal endpoint");
    }
    
   /**
     * Return the weight of this edge.
     */
    public double weight() { return weight; }

    
    public void changeWeight(double newWeight){ this.weight = newWeight; }
    
    // status of the edge
    public boolean status(){ return status; }
    
   
    // change edge status to true, if already true output will be false
    public boolean up(){ 
    	
    	boolean out = true;
    	
    	if(this.status == true){
    		out = false;
    	}

    	this.status = true;
    	
    	return out;
    	
    }
    
    // change edge status to false, if already false output will be false
    public boolean down(){ 
    	
    	boolean out = true;
    	
    	if(this.status == false){
    		out = false;
    	}
    			
    	this.status = false; 
    	
    	return out;
    
    }
    
   /**
     * Return a string representation of this edge.
     */
    public String toString() {
        return v + "->" + w + " " + String.format("%5.2f", weight);
    }

   /**
     * Test client.
     */
    public static void main(String[] args) {
        DirectedEdge e = new DirectedEdge(12, 23, 3.14);
        StdOut.println(e);
    }
}
