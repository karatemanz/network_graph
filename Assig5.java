
import java.io.*;
import java.util.*;
import java.util.Stack;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.text.DefaultCaret;

import Helpers.*;

/*************************************************************************
 *  Compilation:  javac Assig5.java
 *  Execution:    java Assig5 filename.txt
 *  Dependencies: EdgeWeightedDigraph.java IndexMinPQ.java Stack.java DirectedEdge.java
 *	Package: Helpers
 *	JAR: org.jgraph.jar org.jgrapht.jar
 *
 *  Graphing user interface that relays information based on execution of
 *  commands:
 *  
 *  	R 		-> Report (connection, node up&down, and current connected
 *  						components)
 *  	M 		-> Display Minimum Spanning Tree Of Graph
 *  	S i j 	-> Display Shortest Path From i To j Of Graph
 *  	P i j x -> Display Each Distinct Path Of The Graph
 *  	D i		-> Node Down (disconnects node from graph and edges down) 
 *  	U i		-> Node Up (connects node to graph and edges up)
 *  	C i j x -> Change/Create Edge
 *  	Q		-> Quit Program
 *
 *************************************************************************/


public class Assig5 {

	private Scanner read;
	private EdgeWeightedDigraph G;
	private int numVerts;
	private int edges;
	private PrimMSTTrace mst;
	private DijkstraSP sp, dp, tempDP;
	private ArrayList<DirectedEdge> adjStart, temp;
	private double dpWeight;
	
	// -- GUI Components -- //
	JFrame frame, helpFrame;
	JPanel pane1, pane2, helpPane;
	JLabel prompt, oaStatus;
	JMenuBar menu;
	JMenu help;
	JTextArea outputArea, helpArea;
	JScrollPane scroll, helpScroll;
	JButton R, M, S, P, D, U, C, Q;
	ActionListener listener;

	public Assig5(String file) {

		try { 
	    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		    e.printStackTrace();
		}
		
		// used to read the file
		try {
			read = new Scanner(new File(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		numVerts = Integer.parseInt(read.nextLine());
		edges = read.nextInt();
			//System.out.println(numVerts);
			//System.out.println(edges);
		
		read.nextLine();
		
		G = new EdgeWeightedDigraph(numVerts);
		
		while(read.hasNextLine()){
			
			String line = read.nextLine();
			//System.out.println(line);
			String [] edgeRep = line.split(" ");
			
			// grab the data for each edge by file line
			int v = Integer.parseInt(edgeRep[0]);
			int w = Integer.parseInt(edgeRep[1]); 
			double weight = (double)Integer.parseInt(edgeRep[2]);
			
			// initial edge representation
			G.addEdge(new DirectedEdge(v, w, weight));
			// bidirectional edge representation
			G.addEdge(new DirectedEdge(w, v, weight));
						
		}
		
		// -- GUI Creation -- //
		
		// -- button listener segment -- //
		listener = new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	            if(e.getSource() == R){
		            outputArea.append("Report\n");
		            outputArea.append("------\n");
		            
	            		Report();
	            	
	            	outputArea.append("\n");
	            	
	            }else if(e.getSource() == M){
	            	outputArea.append("Minimum Spanning Tree\n");
		            outputArea.append("---------------------\n");
		            
	            		MST();
	            	
	            	outputArea.append("\n");
	            	
	            }else if(e.getSource() == S){
	            	outputArea.append("Shortest Path\n");
		            outputArea.append("-------------\n");
		            
	            	String start = JOptionPane.showInputDialog("Insert Start Vertex:(vertex#)");
	            	String end = JOptionPane.showInputDialog("End Vertex To Path To: (vertex#)");
	  
	            	if(start == null || end == null){ outputArea.append("No Valid Input! Please Try Again!\n"); return;  }
	            	
		            	ShortPath(Integer.parseInt(start), 
		            			  Integer.parseInt(end)
		            			  );
	            	
	            	outputArea.append("\n");
	            	
	            }else if(e.getSource() == P){
	            	outputArea.append("Distinct Path's\n");
		            outputArea.append("---------------\n");
	            	
		            String start = JOptionPane.showInputDialog("Insert Start Vertex:(vertex#)");
		            String end = JOptionPane.showInputDialog("End Vertex To Establish Paths To:(vertex#)");
		            String weight = JOptionPane.showInputDialog("Weight Restriction:");
		            
		            if(start == null || end == null || weight == null){ outputArea.append("No Valid Input! Please Try Again!\n"); return;  }
		            
			            DistinctPaths(Integer.parseInt(start), 
			            			  Integer.parseInt(end), 
			            			  Double.parseDouble(weight)
			            			  );
	            	
		            outputArea.append("\n");
		            
	            }else if(e.getSource() == D){
	            	outputArea.append("Down Vertex\n");
		            outputArea.append("-----------\n");
	            	
		            String vert = JOptionPane.showInputDialog("Insert Vertex To Down:(vertex#)");
		            
		            if(vert == null){ outputArea.append("No Valid Input! Please Try Again!\n"); return;  }
		            
		            	Down(Integer.parseInt(vert));
		            
		            outputArea.append("\n");
		            
	            }else if(e.getSource() == U){
	            	outputArea.append("Up Vertex\n");
		            outputArea.append("---------\n");
	            	
		            String vert = JOptionPane.showInputDialog("Insert Vertex To Up:(vertex#)");
		            
		            if(vert == null){ outputArea.append("No Valid Input! Please Try Again!\n"); return; }
		            
		            	Up(Integer.parseInt(vert));
		            
		            outputArea.append("\n");
		            
	            }else if(e.getSource() == C){
	            	outputArea.append("Change/Create Edge\n");
		            outputArea.append("------------------\n");
	            	
		            String start = JOptionPane.showInputDialog("Insert Start of Edge:(vertex#)");
		            String end = JOptionPane.showInputDialog("End of Edge:(vertex#)");
		            String weight = JOptionPane.showInputDialog("Insert Weight To Change Edge To:");
		            
		            if(start == null || end == null || weight == null){ outputArea.append("No Valid Input! Please Try Again!\n"); return;  }
		            
		            Change(Integer.parseInt(start), 
	            			  Integer.parseInt(end), 
	            			  Double.parseDouble(weight)
	            			  );
		            
		            outputArea.append("");
		            
	            }else if(e.getSource() == Q){
	            	Quit();
	            	
	            }else if(e.getSource() == help){
	            	helpFrame.setVisible(true);
	            }
	            
	        }
	    };
		// -- end of listener -- //
		
	    // -- main frame -- //
		frame = new JFrame("Network Graph");
		menu = new JMenuBar();
		help = new JMenu("Options");
		
		help.setToolTipText("Clarifies Each Buttons Functionality");
		help.addActionListener(listener);
		
		help.addMenuListener(new MenuListener() {

		      public void menuSelected(MenuEvent e) {
		        
		    	  helpFrame.setVisible(true);
		    	  
		      }

		      public void menuDeselected(MenuEvent e) {
		      }

		      public void menuCanceled(MenuEvent e) {
		      }
		    });
		
		
		menu.add(help);
		
		frame.setJMenuBar(menu);
		
		// -- display panel -- //
		pane1 = new JPanel();
		pane1.setBorder(new EmptyBorder(10, 10, 10, 10) );
		pane1.setLayout(new BoxLayout(pane1, BoxLayout.Y_AXIS));
		oaStatus = new JLabel("Output For Commands Displayed Below: ");
		outputArea = new JTextArea(30, 30); 
		outputArea.setEditable(false);
        scroll = new JScrollPane(outputArea);
        
        	// -- display panel init. -- //
        	pane1.add(oaStatus, BorderLayout.NORTH);
        	pane1.add(scroll);
        
        	// for scroll-bar auto-scrolling with text output
			DefaultCaret caret = (DefaultCaret) outputArea.getCaret();
			caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
			// -- button pane -- //
			pane2 = new JPanel();
			pane2.setBorder(new EmptyBorder(50, 10, 50, 50) );
			pane2.setLayout(new BoxLayout(pane2, BoxLayout.Y_AXIS));
			prompt = new JLabel("Please Select A Command: ");
			prompt.setAlignmentX(Component.CENTER_ALIGNMENT);
		    pane2.add(prompt);
			
			// -- button init., alignment, and add to panel -- //
			R = new JButton("Report");	
		    M = new JButton("Min Span Tree");
		    S = new JButton("ShortestPath");
		    P = new JButton("Distinct Paths");
		    D = new JButton("Down A Vertex");
		    U = new JButton("Up A Vertex");
		    C = new JButton("Change A Vertex");
		    Q = new JButton("Quit");		
		    JButton [] button = {R, M, S, P, D, U, C, Q};

		    for(JButton j : button){
		    	j.setAlignmentX(Component.CENTER_ALIGNMENT);
		    	j.addActionListener(listener);
		    	pane2.add(j);	
		    }	
		    
		 frame.add(pane1, BorderLayout.WEST);
		 frame.add(pane2, BorderLayout.EAST);
		 

		 frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 frame.pack();
		 frame.setSize(600, 500);
		 frame.setVisible(true);	 
		 // -- end main frame -- //
	
		 // -- help frame -- //
		 helpFrame = new JFrame();
		 helpPane = new JPanel();
		 
		 DefaultCaret caret2 = (DefaultCaret) outputArea.getCaret();
		 caret2.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		 
		 helpArea = new JTextArea(30, 30); 
		 helpArea.setEditable(false);
	     helpScroll = new JScrollPane(helpArea);
		 
		 helpPane.add(helpScroll);
		 
		 helpArea.append("Help:\n\n");
		 helpArea.append("This will hopefully clarify the use of each of\n"
		 		+ "following commands that are apart of this program\n");
		 
		 helpArea.append("----------------------------------------------------\n\n");
		 helpArea.append("Report -> display the current active network show the \n"
		 		+ "status of the network show the connected components of the \n"
		 		+ "network\n\n");
		 
		 helpArea.append("MST -> show the vertices and edges in the current \n"
		 		+ "minimum spanning tree of the network. \n\n");
		 
		 helpArea.append("ShortestPath -> display the shortest path from vertex \n"
		 		+ "i to vertex j in the graph.\n"
		 		+ "\t Input 1: (i)-> starting vertex of path\n"
		 		+ "\t Input 2: (j)-> end vertex of the path\n\n");
		 
		 helpArea.append("DistinctPaths -> display each of the distinct paths \n"
		 		+ "from vertex i to vertex j with total weight less than or \n"
		 		+ "equal to x.\n"
		 		+ "\t Input 1: (i)-> starting vertex of pathing\n"
		 		+ "\t Input 2: (j)-> ending vertex of paths\n"
		 		+ "\t Input 3: (w)-> weight restriction for paths\n\n");
		 
		 helpArea.append("Up/Down -> veretx i will go up or down depending on the\n"
		 		+ "command. All incident edges also go down.\n"
		 		+ "\t Input: (i)-> vertex to put up or down state\n\n");
		 
		 helpArea.append("Change -> change the weight of edge (i, j) in the graph \n"
		 		+ "to value x. If x is <= 0 the edge(s) should be removed from \n"
		 		+ "the graph. If x > 0 and edge (i, j) previously did not exist, \n"
		 		+ "create it. \n"
		 		+ "\t Input 1: (i)-> starting vertex of edge to change/create\n"
		 		+ "\t Input 2: (j)-> end vertex of edge to change/create\n"
		 		+ "\t Input 3: (x)-> weight to change/create edge\n\n");
		 
		 helpArea.append("Quit -> end the program execution\n\n");
		 
		 helpFrame.add(helpScroll);
		 
		 helpFrame.pack();
		 helpFrame.setSize(600, 500);
		 helpFrame.setVisible(false);
		 // -- end help frame -- //
		 
		  
	}

	public void Report(){
		
		// check graph connection status
		if(G.V() == numVerts){ 
			outputArea.append("Network is connected!\n");
		}else{  
			outputArea.append("Network is not connected!\n");
		}
	
		// check nodes up
		outputArea.append("Following nodes are currently up:\n");
		
		for(Vertex v : G.upordown()){
			if(v.status()){ outputArea.append(v.toString() + " "); }	
		}
		
		outputArea.append("\n");
		
		// check nodes down
		outputArea.append("Following nodes are currently down:\n");
		for(Vertex v : G.upordown()){
			if(!v.status()){ outputArea.append(v.toString() + " "); }
		}
		
		outputArea.append("\n");
		
		// display current network
		outputArea.append("Connected Components:\n");
		outputArea.append(G.toString() + "\n");
		
		
	}
	
	public void MST(){
		
		// check for subgraph's
		
		// use PrimMST Eager
		mst = new PrimMSTTrace(G);
		
		outputArea.append("Edges in MST: \n");
		for (DirectedEdge e : mst.edges()){
			outputArea.append("" + e + "\n");
	    }
		
		// mst for graph/subgraph's + display
		
	}
	
	public void ShortPath(int start, int finish){
			
			// shortest path from start node to finish node
			outputArea.append("Shortest Path From ("+start+") -> ("+finish+"):\n");
			
			sp = new DijkstraSP(G, start);
			double pathWeight = 0;
			
			if(sp.hasPathTo(finish) && G.getVertex(start).status() && G.getVertex(finish).status()){
				for (DirectedEdge e : sp.pathTo(finish)) {
					if(!e.status()){
						outputArea.append("No Path Detected!\n");
						return;
					}
	                pathWeight += e.weight();
	            }
				
				outputArea.append("Min Path Weight: [" + pathWeight + "]\n");
				outputArea.append("" + sp.pathTo(finish) + "\n");
			}else{
				if(G.getVertex(start).status() && G.getVertex(finish).status()){
					outputArea.append("Start or End points are invalid!\n");
				}
				outputArea.append("No Path Detected!\n");
			}
			
			outputArea.append("\n");
				
	}
	
	// -- distinct path from start to finish -- //
	public void DistinctPaths(int start, int finish, double restrict){
		
		// all distinct paths from start to finish with a restricted weight
		dp = new DijkstraSP(G, start);
		
		if(!dp.hasPathTo(finish) && G.getVertex(start).status() && G.getVertex(finish).status()){
			if(G.getVertex(start).status() && G.getVertex(finish).status()){
				outputArea.append("Start or End points are invalid!\n");
			}
			outputArea.append("No Path's Found\n");
			return;
		}else{
		
			outputArea.append("Weight Restriction: ["+ restrict +"]\n");
			adjStart = G.list(start);
			dp(start, finish, restrict);
			
		}

	}
	
	// recursive dp call
	public void dp(int s, int f, double r){
		
			int count = 1;
		
			for(DirectedEdge e : adjStart){
				
				tempDP = new DijkstraSP(G, e.to());
				
				double pathTotal = tempDP.distTo(f) + e.weight();
				if(e.status() && pathTotal <= r){
					
					outputArea.append(count + ".) " + s + "->" + e.to() + " " 
								+ e.weight() + " " + tempDP.pathTo(f) 
								+ "["+ pathTotal +"]" + "\n");
					count++;
				}
				if(count == 1){
					outputArea.append("No Valid Path's!");
				}
				
				
			}

	}
	
	// -- connect node and incident nodes -- //
	public void Up(int vertex){
		
		G.getVertex(vertex).up();
		for (DirectedEdge e : G.list(vertex)){ 
			
			if(e.up() && G.getComplement(e).up()){  
				numVerts++; 
				outputArea.append("["+e.toString()+"] edge is now up!\n");
			}else{
				outputArea.append("<"+e.toString()+"> is already up!\n");	
			} 
	
		}
	}
	
	// -- disconnect node and incident node -- //
	public void Down(int vertex){
		
		G.getVertex(vertex).down();
		for (DirectedEdge e : G.list(vertex)){ 
			
			if(e.down() && G.getComplement(e).down()){ 
				numVerts--;  
				outputArea.append("["+e.toString()+"] edge is now down!\n");
			}else{
				outputArea.append("<"+e.toString()+"> is already down!\n");	
			}
			
		
		}
		
	}
	
	// -- change/create edge u, v with new weight -- //
	// --> for 0 or - input edge will be deleted permanently
	public void Change(int u, int v, double weight){
		
		DirectedEdge newEdge = new DirectedEdge(u, v, weight);
		DirectedEdge oldEdge = G.getMatchEdge(newEdge);
		
		// remove edge if weight is negative or 0
		if(weight <= 0){ 
			
			outputArea.append("Null weight received, current edge deleted!\n");
			G.removeEdge(oldEdge); 
			G.removeEdge(G.getComplement(oldEdge));
			outputArea.append("\n");
			return; 
			
		}
		
		// if edge exists change weight if not create new edge
		if(G.hasEdge(oldEdge)){
			
			outputArea.append("Old Weight: " + oldEdge.weight() + "\n");
			G.changeEdgeWeight(oldEdge, weight);
			G.changeEdgeWeight(G.getComplement(oldEdge), weight);
			outputArea.append("New Weight: " + newEdge.weight() + "\n");
			
		}else{
			
			G.addEdge(newEdge);
			G.addEdge(G.getComplement(newEdge));
			outputArea.append("New Edge Made: " + newEdge.toString() + "\n");
			
		}
		
		outputArea.append("\n");
		
	}
	
	public void Quit(){
		
		// ends the program + prompts user of exit
		System.exit(0);
		
	}
	
	public static void main (String[] args){
		
		new Assig5(args[0]);
		
	}
	
	
}
