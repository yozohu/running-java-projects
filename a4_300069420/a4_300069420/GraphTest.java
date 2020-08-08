import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.io.IOException;


import net.datastructures.AdjacencyMatrixGraph;
import net.datastructures.Graph;
import net.datastructures.Vertex;
import net.datastructures.Edge;
import net.datastructures.AdjacencyListGraph;
import net.datastructures.Map;
import net.datastructures.HashTableMap;
import net.datastructures.InvalidPositionException;

/**
 * 
 * @(based on Jochen Lang implementation of SimpleGraph)
 *
 */
public class GraphTest {
	Graph<String,String> graphTest;  


	/** 
	* Create a TestGraph from a file
	*/
	public GraphTest( String fileName ) throws InvalidPositionException, IOException {
		graphTest = new AdjacencyMatrixGraph<String,String>();
		read( fileName );
	}


	/**
	* Read a list of edges from file
	*/
	protected void read( String fileName ) throws InvalidPositionException, IOException {
		BufferedReader graphFile = new BufferedReader( new FileReader(fileName));
		
		// Create a hash map to store all the vertices read
		Map<String, Vertex<String>> vertices = new HashTableMap<String, Vertex<String>>();
		
		// Read the edges and insert
		String line;
		while( ( line = graphFile.readLine( ) ) != null ) {
			
			StringTokenizer st = new StringTokenizer( line );
			
			if( st.countTokens() != 2 ) 
				throw new IOException("Incorrect input file at line " + line );
			
			String source = st.nextToken( );
			String dest = st.nextToken( );
			
			Vertex<String> sv = vertices.get( source );
			if ( sv == null ) {
				// Source vertex not in graph -- insert
				sv = graphTest.insertVertex(source); 
				vertices.put( source, sv );
			} 
			
			Vertex<String> dv = vertices.get( dest );
			if ( dv == null ) {
				// Destination vertex not in graph -- insert
				dv = graphTest.insertVertex(dest); 
				vertices.put( dest, dv );
			}
			
			// check if edge is already in graph
			if ( !graphTest.areAdjacent( sv, dv )) {
				// edge not in graph -- add 
				graphTest.insertEdge(sv, dv, source + " to " + dest ); 
			}
		}
	}

	
	/**
	 * Insert Vertex
	 */
	protected void insertVertex( String vert){
		// check if the vertex already exists
		Vertex <String> v = getVertex(vert);
		
		if (v == null){
			// Vertex does not exist, and therefore we can add it! 
			graphTest.insertVertex(vert);
		}
	}
	
	/**
	 * Remove Vertex
	 */
	protected void removeVertex( String vert){
		// check if the vertex already exists
		Vertex <String> v = getVertex(vert);
		
		if (v != null){
			// Vertex does exist, and therefore we can remove it! 
			graphTest.removeVertex(v);
		}
	}
	
	
	/**
	 * Insert Edge
	 */
	protected void insertEdge( String vert1, String vert2){
		// check if the vertices exists
		Vertex <String> v1 = getVertex(vert1);
		Vertex <String> v2 = getVertex(vert2);
		
		if (v1 != null && v2 !=null){
			// Vertex does not exist, and therefore we can add it! 
			graphTest.insertEdge(v1, v2, v1 + " to " + v2);
		}
	}
	
	/**
	 * Remove Edge
	 */
	protected void removeEdge( String vert1, String vert2){
		// check if the vertices exists
		Vertex <String> v1 = getVertex(vert1);
		Vertex <String> v2 = getVertex(vert2);
		
		if (v1 != null && v2 !=null){
			// Both of the supplied vertices do exist, 
			// let's look for the edge we need to remove

			// First possibility for the edge element: 
			String p1 = v1+" to "+v2;
			
			// Second possibility for the edge element: 
			String p2 = v2+" to "+v1;
			
			for (Edge<String> e : graphTest.edges()){
				if (e.element().equals(p1) || e.element().equals(p2)){
					graphTest.removeEdge(e);
				}
			}
			
		}
	}
	
	/**
	* Helper routine to get a Vertex (Position) from a string naming
	* the vertex
	*/
	protected Vertex<String> getVertex( String vert ) {
		// Go through vertex list to find vertex -- why is this not a map
		for( Vertex<String> vs : graphTest.vertices() ) {
			if ( vs.element().equals( vert )) {
				return vs;
			}
		}
		return null;
	}
	
	
	/**
	* Printing all the vertices in the list, followed by printing all
	* the edges
	*/
	void print() {	
		System.out.println( "This graph has "+graphTest.numVertices()+" vertices:"); 
		for( Vertex<String> vs : graphTest.vertices() ) {
			System.out.println( vs.element() );
		}
		
		System.out.println( "It also has "+graphTest.numEdges()+" edges:"); 
		for( Edge<String> es : graphTest.edges() ) {
			System.out.println( es.element() );
		}
		
		return;
	}
	
	/**
	 * Get user commands, parse them and apply them to the graph
	 * @throws IOException
	 */
	protected void getUserCommands() throws IOException{
		while (true){
			String line="";
	
			line = readInput();

			
			StringTokenizer st = new StringTokenizer( line );
			
			if( st.countTokens() < 3 ) {
				commandErrorMsg(line);
			}
			else {
				String command = st.nextToken( );
				String arg1 = st.nextToken( );
				String arg2 = st.nextToken( );
				if (command.equalsIgnoreCase("insert")){
					
					if (arg1.equalsIgnoreCase("vertex")){
						insertVertex(arg2);
						print();
					}
					else if (arg1.equalsIgnoreCase("edge")){
						if (st.hasMoreTokens()){
							String arg3 = st.nextToken( );
							
							insertEdge(arg2,arg3);
						}
						print();
					}
					else {
						commandErrorMsg(line);
					}
					
					
					
				}
				else if (command.equalsIgnoreCase("remove")){
					
					if (arg1.equalsIgnoreCase("vertex")){
						removeVertex(arg2);
						print();
					}
					else if (arg1.equalsIgnoreCase("edge")){
						if (st.hasMoreTokens()){
							String arg3 = st.nextToken( );
							
							removeEdge(arg2,arg3);
						}
						print();
					}
					else {
						commandErrorMsg(line);
					}
					
					
					
				}
				else {
					commandErrorMsg(line);
				}
			}
			
			
		}
	}
	
	
	private void commandErrorMsg(String line){
		System.out.println("Invalid command: " + line );
	}
	
	private String readInput() throws IOException {
		System.out.print( "Enter command: " );
		BufferedReader reader = 
				new BufferedReader(new InputStreamReader ( System.in ));
		return reader.readLine();
	}
	
	
	
	
	/**
	* Generate a Graph from File and prints the vertices visited
	* by a DepthFirstSearch
	*/
	public static void main( String[] argv ) {
		if ( argv.length < 1 ) {
			System.err.println( "Usage: java TestGraph fileName" );
			System.exit(-1);
		}
		try {
			GraphTest graphTest = new GraphTest( argv[0] );
			graphTest.print();
		
			graphTest.getUserCommands();
		}
		catch ( Exception except ) {
			except.printStackTrace();
		}
	}
}
