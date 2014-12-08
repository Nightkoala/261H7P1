/**
 * SpanTree.java
 * 
 * @author	Derek Brown <djb3718@rit.edu>
 *
 * Purpose:	Finds the minimum f-containing spanning tree for any given weighted
 * 			undirected graph.
 */

import java.util.Scanner;
import java.util.HashSet;
import java.util.Arrays;

public class SpanTree {

	// Attributes
	
	private int numVertices;
	private int numEdges;
	private HashSet<Edge> T;
	private Edge[] G;
	private int[] boss;
	private int[] size;
	private HashSet<Integer>[] set;
	private boolean[] seen;
	private int[][] adjList;
	private int[] degree;
	
	// Constructor
	
	/**
	 * Constructor for creating an object that holds important info for solving
	 * the SpanTree problem.
	 * 
	 * @param v	The number of vertices in the graph.
	 * @param e	The number of edges in the graph.
	 */
	public SpanTree( int v, int e ) {
		this.numVertices = v;
		this.numEdges = e;
		this.boss = new int[numVertices+1];
		this.size = new int[numVertices+1];
		this.set = new HashSet[numVertices+1];
		this.T = new HashSet<Edge>();
		this.seen = new boolean[numVertices+1];
		this.degree = new int[numEdges+1];
		this.adjList = new int[numVertices+1][numEdges];
		for( int i = 1 ; i <= numVertices ; i++ ) {
			set[i] = new HashSet<Integer>();
			seen[i] = false;
			degree[i] = 0;
			boss[i] = i;
			size[i] = 1;
			((HashSet<Integer>)set[i]).add(i);
		}//end for i
	}//end SpanTree constructor
	
	// Methods
	
	/**
	 * Sets the graph stored in the SpanTree object.
	 * 
	 * @param g	The graph.
	 */
	public void setG( Edge[] g ) {
		this.G = g;
	}//end setG
	
	/**
	 * Helper function for doing the union of two trees.
	 * 
	 * @param u	First vertex to consider.
	 * @param v	The second vertex to possibly union with the first.
	 */
	public void union( int u, int v ) {
		if( size[boss[u]] > size[boss[v]] ) {
			((HashSet<Integer>)set[boss[u]]).addAll(set[boss[v]]);
			size[boss[u]] += size[boss[v]];
			for( int z : set[boss[v]] ) {
				boss[z] = boss[u];
			}//end for z
		}//end if
		else {
			((HashSet<Integer>)set[boss[v]]).addAll(set[boss[u]]);
			size[boss[v]] += size[boss[u]];
			for( int z : set[boss[u]] ) {
				boss[z] = boss[v];
			}//end for z
		}//end else
	}//end union
	
	/**
	 * Main algorithm for creating the spanning tree
	 */
	public void Kruskal() {
		Arrays.sort( G );
		for( Edge e : G ) {
			if( boss[e.getStart()] != boss[e.getEnd()] ) {
				T.add( e );
				union( e.getStart(), e.getEnd() );
			}//end if
		}//end for e
	}//end Kruskal
	
	/**
	 * Finds the weight of the spanning tree
	 * 
	 * @return	The weight of the spanning tree.
	 */
	public int treeSize() {
		int size = 0;
		for( Edge e : T ) {
			size += e.getWeight();
		}//end for e
		return size;
	}//end treeSize
	
	/**
	 * Adds the edges that belong to F into the spanning tree.
	 */
	public void addFToTree() {
		for( Edge e : G ) {
			if( e.getInF() ) {
				T.add( e );
				union( e.getStart(), e.getEnd() );
			}//end if
		}//end for e
	}//end addFToTree
	
	/**
	 * Depth-First search helper function, used to determine if there is a 
	 * cycle in the spanning tree.
	 * 
	 * @param s		The vertex to consider.
	 * @param prev	The parent of the current vertex.
	 * @return		True, if spanning tree contains a cycle,
	 * 				False, otherwise.
	 */
	public boolean DFS( int s, int prev ) {
		System.out.printf("s=%d\tprev=%d\n", s, prev);
		seen[s] = true;
		boolean isCycle = false;
		for( int u = 0 ; u < degree[s] ; u++ ) {
			if( !seen[adjList[s][u]] ) {
				isCycle = DFS( adjList[s][u], s );
			}//end if
		}//end for u
		return isCycle;
	}//end DFS
	
	/**
	 * Helper method, constructs the adjacency list for the spanning tree to be
	 * used in DFS to determine if there exists a cycle.
	 */
	public void constructAdjList() {
		for( Edge e : T ) {
			adjList[e.getStart()][degree[e.getStart()]] = e.getEnd();
			degree[e.getStart()]++;
			adjList[e.getEnd()][degree[e.getEnd()]] = e.getStart();
			degree[e.getEnd()]++;
		}//end for e
	}//end constructAdjList
	
	/**
	 * The algorithm determining if the spanning tree returned by the Kruskal
	 * algorithm actually is a spanning tree, and doesn't contain a cycle.
	 * 
	 * @return	true, if it is a spanning tree,
	 * 			false, otherwise.
	 */
	public boolean isSpanningTree() {
		constructAdjList();
		return !DFS( 1, -1 );
	}//end isSpanningTree
	
	/**
	 * The main logic for the program, Reads input from the user, creates the
	 * SpanTree object to house the data and then feeds the data into the
	 * Kruskal algorithm to find the spanning tree, Then once Kruskal has
	 * completed execution, run a second algorithm to determine if the result
	 * is truly a spanning tree, ie does not contain a cycle,  The result is
	 * then displayed to the user.
	 * 
	 * @param args	Command line arguments, unused.
	 */
	public static void main(String[] args) {
		Scanner sc = new Scanner( System.in );
		String input = sc.next();
		int numVertices, numEdges;
		numVertices = Integer.parseInt( input );
		input = sc.next();
		numEdges = Integer.parseInt( input );
		int start, end, weight, f;
		boolean inF;
		Edge[] g = new Edge[numEdges];
		SpanTree ST = new SpanTree( numVertices, numEdges );
		for( int i = 0 ; i < numEdges ; i++ ) {
			input= sc.next();
			start = Integer.parseInt( input );
			input = sc.next();
			end = Integer.parseInt( input );
			input = sc.next();
			weight = Integer.parseInt( input );
			input = sc.next();
			f = Integer.parseInt( input );
			if( f == 1 ) {
				inF = true;
			}//end f
			else {
				inF = false;
			}//end else
			Edge e = ST.new Edge( start, end, weight, inF );
			g[i] = e;
		}//end for i
		sc.close();
		ST.setG( g );
		ST.addFToTree();
		ST.Kruskal();
		int size = ST.treeSize();
		System.out.println(size);
//		if( ST.isSpanningTree() ) {
//			System.out.println(size);
//		}//end if
//		else {
//			System.out.println(-1);
//		}//end else
//		ST.printAdjList();
	}//end main
	
	/**
	 * Edge class
	 * 
	 * @author	Derek Brown
	 * 
	 * Purpose:	Helper class, used to store information important for an edge,
	 * 			like the start, end, and weight.
	 */
	public class Edge implements Comparable<Edge> {
		
		// Attributes
		
		private int start;
		private int end;
		private int weight;
		private boolean inF;
		
		// Constructor
		
		/**
		 * Constructor for creating an instance of an Edge.
		 * 
		 * @param s	The start vertex.
		 * @param e	The end vertex
		 * @param w	The weight of the edge.
		 * @param f	true if edge is in F,
		 * 			false, otherwise.
		 */
		public Edge( int s, int e, int w, boolean f ) {
			this.start = s;
			this.end = e;
			this.weight = w;
			this.inF = f;
		}//end Edge constructor
		
		// Methods
		
		/**
		 * Getter for retrieving the edge's start vertex.
		 * 
		 * @return	The start vertex.
		 */
		public int getStart() {
			return this.start;
		}//end getStart
		
		/**
		 * Getter for retrieving the edge's end vertex.
		 * 
		 * @return	The end vertex.
		 */
		public int getEnd() {
			return this.end;
		}//end getEnd
		
		/**
		 * Getter for retrieving the edge's weight.
		 * 
		 * @return	The weight of the edge.
		 */
		public int getWeight() {
			return this.weight;
		}//end getWeight
		
		/**
		 * Getter for retrieving if the edge belongs to F.
		 * 
		 * @return	true if in F,
		 * 			false otherwise.
		 */
		public boolean getInF() {
			return this.inF;
		}//end getInF

		/**
		 * CompareTo function used for sorting the edges based on weight.
		 * 
		 * @param o	The edge that this edge is being compared to.
		 * 
		 * @return	-1 if this edge has less weight than o.weight,
		 * 			0 if both edges have the same weight,
		 * 			1 if this edge has greater weight than o.weight.
		 */
		@Override
		public int compareTo(Edge o) {
			if( this.weight < o.weight ) {
				return -1;
			}//end if
			else if( this.weight > o.weight ) {
				return 1;
			}//end else if
			return 0;
		}//end compareTo
	}//end Edge class
}//end SpanTree class
