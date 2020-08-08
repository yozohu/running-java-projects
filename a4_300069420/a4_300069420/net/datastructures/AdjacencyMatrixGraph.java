package net.datastructures;
import java.util.Iterator;
import java.util.*;

import net.datastructures.AdjacencyListGraph.MyEdge;
import net.datastructures.AdjacencyListGraph.MyVertex;

//package net.datastructures;

/**
 * 
 * @author Bob Smart
 * @param <V>
 * @param <E>
 * @param <T>
 */
public class AdjacencyMatrixGraph<V,E> implements Graph<V,E>{

	protected NodePositionList<Vertex<V>> vList;	// container for vertices
	protected NodePositionList<Edge<E>> eList;	// container for edges
	private ExpandableSquareMatrix<Edge<E>> esm;
	private Hashtable<Integer, Vertex<V>> my_dict;

	
	/** Default constructor that creates an empty graph */
	public AdjacencyMatrixGraph() {
		vList = new NodePositionList<Vertex<V>>();
		eList = new NodePositionList<Edge<E>>();	
		esm = new ExpandableSquareMatrix<Edge<E>>();;
		my_dict = new Hashtable<Integer, Vertex<V>>();
	}
	
	
	@Override
	public int numVertices() {
		return vList.size();
	}

	@Override
	public int numEdges() {
		return eList.size();
	}

	@Override
	public Iterable <Vertex<V>> vertices() {
		return vList;
	}

	@Override
	public Iterable <Edge <E>> edges() {
		return eList;
	}

	@Override
	public V replace(Vertex<V> p, V o) throws InvalidPositionException {
	    V temp = p.element();
	    MyVertex<V> vv = checkVertex(p);
	    vv.setElement(o);
	    return temp;
	}

	@Override
	public E replace(Edge<E> p, E o) throws InvalidPositionException {
	    E temp = p.element();
	    MyEdge<E> ee = checkEdge(p);
	    ee.setElement(o);
	    return temp;
	}

	@Override
	/** Return an iterator over the edges incident on a vertex */
	public Iterable<Edge<E>> incidentEdges(Vertex<V> v) throws InvalidPositionException {
	    MyVertex<V> vv = checkVertex(v);
	    return vv.incidentEdges();
	}

	@Override
	/** Return the endvertices of a edge in an array of length 2 */
	public Vertex<V>[] endVertices(Edge<E> e)throws InvalidPositionException {
		MyEdge<E> ee = checkEdge(e);
	    return ee.endVertices();
	}

	/** Return the other endvertex of an incident edge 
	 * TODO Complete implementation
	 */
	@Override
	public Vertex<V> opposite(Vertex<V> v, Edge<E> e)throws InvalidPositionException {
		checkVertex(v);
		MyEdge<E> ee = checkEdge(e);
		Vertex<V>[] endv = ee.endVertices();
    	if (v == endv[0])
      		return endv[1];
    	else if (v == endv[1])
      		return endv[0];
    	else
      		throw new InvalidPositionException("No such vertex exists");
	}

	/**
	 * TODO Complete implementation
	 */
	@Override
	public boolean areAdjacent(Vertex<V> u, Vertex<V> v) throws InvalidPositionException {		
		
		MyVertex<V> v1 = checkVertex(u);
		MyVertex<V> v2 = checkVertex(v);
		V value1 = (V) v;
		V value2 = (V) u;
		int counter1 = 0;
		int counter2 = 0;
		
		for (int i = 0; i < my_dict.size(); i++) {
			if (my_dict.get(i).equals(value1)) {
				counter1 = i;
			}
			
			if (my_dict.get(i).equals(value2)) {
				counter2 = i;
			}
			
		}		
		Edge<E> word = esm.get(counter1, counter2);
		if (word != null) {
			return true;
		}
		
		else {
			return false;
		}
	}

	/**
	 * TODO Complete implementation
	 */
		
	protected int vertexCounter = 0;
	
	private int dicValue = 0;
	
	@Override	
	public Vertex<V> insertVertex(V o) {
		
		esm.increaseOrder();
	
		MyVertex<V> vv =  new MyVertex<V>(o, vertexCounter);
		vertexCounter++;
	    vList.addLast(vv);
		my_dict.put(dicValue, vv);
		dicValue++;	
	    Position<Vertex<V>> p = vList.last();
	    vv.setLocation(p);
	    return vv;

	}

	/**
	 * TODO Complete implementation
	 */
	public Edge<E> insertEdge(Vertex<V> v, Vertex<V> w, E o) throws InvalidPositionException {  
		int counter1 = 0;
		int counter2 = 0;
	    MyVertex<V> vv = checkVertex(v);
	    MyVertex<V> ww = checkVertex(w);
	    V value1 = (V) v;
	    V value2 = (V) w;

	    for (int i = 0; i < my_dict.size(); i++) {
	    	if ((value1.equals(my_dict.get(i)))) {
	    		counter1 = i;
	    		break;
	    	}
	    }
	    
	    for (int i = 0; i < my_dict.size(); i++) {
	    	if ((value2.equals(my_dict.get(i)))){
	    		counter2 = i;
	    		break;
	    	}
	    }

	    MyEdge<E> ee = new MyEdge<E>(v, w, o);
	    eList.addLast(ee);
	    Position<Edge<E>> pe = eList.last();
	    ee.setLocation(pe);
	    
	    esm.set(counter1, counter2, ee);
	    esm.set(counter2, counter1, ee);    
	    return ee;
	}
	
	/**
	 * TODO Complete implementation
	 */
	@Override
	public V removeVertex(Vertex<V> v) throws InvalidPositionException {
	    MyVertex<V> vv = checkVertex(v);
	    V value1 = (V) v;
	    Iterator<Edge<E>> inc = incidentEdges(v).iterator();
	    
	    while (inc.hasNext()) {
	    
	      MyEdge<E> e = (MyEdge<E>) inc.next();
	      if (e.location() != null) { // if the edge has not been marked invalid
	    	  removeEdge(e);
	    	  }
	    }
	    
	    vList.remove(vv.location());
	    
	    for (int k = 0; k < my_dict.size(); k++) {
	    	
	    	if (value1.equals(my_dict.get(k))) {
	    		my_dict.remove(v, k);
	    		esm.removeColumnRow(k);
	    		break;
	    	}
	    }
	    
	    removeEdgeFromDictionary(v);
	    return v.element();
	}
	
	private void removeEdgeFromDictionary(Vertex<V> v) {		
	    for (int i = 0; i < my_dict.size(); i++) {
	    	if (v.equals(my_dict.get(i))) {
	    		my_dict.remove(i);
	    	}	    	
	    }
	}


	/**
	 * TODO Complete implementation
	 */
	@Override
	public E removeEdge(Edge<E> e) throws InvalidPositionException {
		
		MyEdge<E> ee = checkEdge(e);
		MyVertex<V>[] endv = ee.endVertices();	    
	     	    
	    int counter1 = 0;
	    int counter2 = 0;
	    
	    for (int i = 0; i < my_dict.size(); i++) {
	    	if ((endv[0]).equals(my_dict.get(i))) {
	    		counter1 = i;
	    		break;
	    	}
	    }
	    
	    for (int i = 0; i < my_dict.size(); i++) {
	    	if ((endv[1]).equals(my_dict.get(i))) {
	    		counter2 = i;
	    		break;
	    	}
	    }
	    
	    esm.set(counter1, counter2, null);
	    esm.set(counter2, counter1, null);
	   
	    eList.remove(ee.location());
	    ee.setLocation(null);	// invalidating this edge
	    return e.element();
	}
	
	/** Determines whether a given vertex is valid. */
	protected MyVertex<V> checkVertex(Vertex<V> v) throws InvalidPositionException {
		
		if (v == null || !(v instanceof MyVertex))
			throw new InvalidPositionException("Vertex is invalid");
		
		return (MyVertex<V>) v;
	}

	/** Determines whether a given edge is valid. */
	protected MyEdge<E> checkEdge(Edge<E> e) throws InvalidPositionException {
		
		//if (e == null || !(e instanceof MyEdge))
			//throw new InvalidPositionException("Edge is invalid");
		
		return (MyEdge<E>) e;
	}
	
	
	/** Implementation of a decorable position by means of a hash table*/
	protected static class MyPosition<T> 
			extends HashTableMap<Object,Object> implements DecorablePosition<T> {
		
		/** The element stored at this position. */
		protected T elem;
		
		/** Returns the element stored at this position. */
		public T element() {
			return elem;
		}
		
		/** Sets the element stored at this position. */
		public void setElement(T o) {
			elem = o;
		}
	}
	
	/** Implementation of a vertex for an undirected adjacency list
	* graph.  Each vertex stores its incidence container and position
	* in the vertex container of the graph. */
	protected  class MyVertex<V> 
		extends MyPosition<V> implements Vertex<V> {
		
		protected Position<Vertex<V>> loc;	
		protected int index;
		
		/** Constructs the vertex with the given element. */
		MyVertex(V o, int i) {
			index = i;
			elem = o;
		}
		
		/** Return the degree of a given vertex */
		public int degree() {
			return 0;
		}
		
		/** Returns the incident edges on this vertex. */
		public Iterable<Edge<E>> incidentEdges() {
			NodePositionList<Edge<E>> incidentEdges= new NodePositionList<Edge<E>>();
			for (int j = 0; j < esm.getOrder(); j++){
				Edge <E> e = esm.get(index,j);
				if (e != null){
					incidentEdges.addLast(esm.get(index,j));
				}
			}
			
			return incidentEdges;
		}
		
		
		public void setIndex(int i){
			index = i;
		}
	
		public int getIndex(){
			return index;
		}
		
		
		/** Returns the position of this vertex in the vertex container of
		* the graph. */
		public Position<Vertex<V>> location() {
			return loc;
		}
		
		/** Sets the position of this vertex in the vertex container of
		* the graph. */
		public void setLocation(Position<Vertex<V>> p) {
			loc = p;
		}
		
		/** Returns a string representation of the element stored at this
		* vertex. */
		public String toString() {
			return elem.toString();
		}
	}

	/** Implementation of an edge for an undirected adjacency list
	* graph.  Each edge stores its endpoints (end vertices), its
	* positions within the incidence containers of its endpoints, and
	* position in the edge container of the graph. */
	protected class MyEdge<E> extends MyPosition<E> implements Edge<E> {

		/** The end vertices of the edge. */
		protected MyVertex<V>[] endVertices;

		/** The position of the edge in the edge container of the
		* graph. */
		protected Position<Edge<E>> loc;

		/** Constructs an edge with the given endpoints and elements. */
		MyEdge (Vertex<V> v, Vertex<V> w, E o) {
			elem = o;
			endVertices = (MyVertex<V>[]) new MyVertex[2];
			endVertices[0] = (MyVertex<V>)v;
			endVertices[1] = (MyVertex<V>)w;
		}
		
		
		public MyVertex<V>[] endVertices(){
			return endVertices;
		}
		
		/** Returns the position of the edge in the edge container of the
		* graph. */
		public Position<Edge<E>> location() {
			return loc;
		}
		
		/** Sets the position of the edge in the edge container of the
		* graph. */
		public void setLocation(Position<Edge<E>> p) {
			loc = p;
		}
		
		/** Returns a string representation of the edge via a tuple of
		* vertices. */
		public String toString() {
			return "(" + endVertices[0].toString() + "," + endVertices[1].toString() + ")";
		}
	}

}
