package Helpers;

public class Vertex {

	private final int id;
	private boolean status;
	private int count;
	
	public Vertex(int index){
			
		id = index;
		status = true;
		count++;
		
	}
	
	// status is changed to up/true
	public void up(){ this.status = true; }
	
	// status is changed to down/false
	public void down(){ this.status = false; }
	
	// return the status
	public boolean status(){ return status; }
	
	public String toString(){
		
		return "" + id;
		
	}
	
	
}
