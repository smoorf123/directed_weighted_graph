package dwGraph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/*
 *  The DWGraph class (DWGraph standing for directed Weighted Graph) is a Graph
 *  Data structure where each edge has an associated weight and each vertex has
 *  an associated direction. The way this code implements this structure is by
 *  storing the vertices in a HashMap as keys and values being an ArrayList of
 *  Edges. The Edges are objects of the Edge class which stores two things
 *  namely the weight of the edge and the nextVertex which is the direction the
 *  edge points to. Whenever a new object of DWGraph is initiated, a new said
 *  HashMap called adjacencyList is created. The naming of this HashMap is after
 *  the type of implementation of this graph. There are 3 helper methods used in
 *  this code to aid the functionality of methods in the class. This class
 *  performs basic functions such as adding vertices, adding edges, removing
 *  both as well as getting collections of either. This class also has an
 *  overridden toString() method for debugging purposes. This class also is
 *  declared with generic type V meaning it is able to store data for a variety of data types.
 */
public class DWGraph<V extends Comparable<V>> {

	// Main implementation of Graph, through Adjacency List where keys are of
	// generic V type and values are ArrayLists of Edge type
	private HashMap<V, ArrayList<Edge>> adjacencyList;

	// Public Constructor to initialize HashMap
	public DWGraph() {
		adjacencyList = new HashMap<V, ArrayList<Edge>>();
	}

	// Private inner class Edge with a public constructor to create objects of
	// edge type. Edge objects contain two forms of data namely the weight of
	// the edge and the vertex the edge points to. This fulfills the Weighted
	// Directed aspect of the DWGraph class.
	private class Edge {
		private int weight;
		private V nextVertex;

		// Public Constructor to initialize fields
		public Edge(int weightIn, V nextVertexIn) {
			weight = weightIn;
			nextVertex = nextVertexIn;
		}
	}

	// Helper method used by every method in this class with some form of
	// parameters. This method throws an IllegalArgumentException if the
	// parameter of a method contains a null value.
	private void nullCheck(Object o) {
		// if object o is null, IllegalArgumentException is thrown
		if (o == null) {
			throw new IllegalArgumentException(
					"Input argument to this method is null");
		}
	}

	// Overridden toString() method for debugging purposes. This method prints
	// an object of the DWGraph by indicating vertex then mentioning the weight
	// and then the next vertex. Vertices (as keys of HashMap) are separated by
	// a "|". The final format is like "Vertex1: Vertex1 -(Weight1)->
	// nextVertex1, Vertex1 -(Weight2)-> nextVertex2,| Vertex2: Vertex2
	// -(Weight1)-> nextVertex1, Vertex2 -(Weight2)-> nextVertex2,|"
	@Override
	public String toString() {
		String retVal = "";

		// Iterates through each vertex (keys of adjacencyList) and adds to
		// retVal
		for (V values : adjacencyList.keySet()) {
			retVal += values.toString() + ": ";
			// Iterates through each edge and adds the weight and nextVertex in
			// the aforementioned format
			for (Edge edge : adjacencyList.get(values)) {
				retVal += values.toString() + " -(" + edge.weight + ")-> "
						+ edge.nextVertex.toString() + ",";
			}

			// Adds separator for each individual vertex (key in adjacencyList)
			retVal += "| ";
		}

		// Returns final associated string
		return retVal;
	}

	// This method creates a vertex by adding the argument passed as the
	// parameter as a key in the adjacencyList HashMap. The associated value is
	// set to a new ArrayList of type Edge that stores all the edges with
	// weights and vertices pointed to.
	public boolean createVertex(V dataForVertex) {
		// check for null using helper method
		nullCheck(dataForVertex);
		// Checks if vertex already previously exists
		boolean flag = !isVertex(dataForVertex);

		// If vertex does not exist then adds the vertex as a key and
		// instantiates the associated ArrayList of type Edge for values
		if (flag) {
			adjacencyList.put(dataForVertex, new ArrayList<Edge>());
		}

		// returns true if vertex was added, false if not
		return flag;
	}

	// This method checks if the argument passed in the parameter is present in
	// the current Graph. This is done by checking if a pre-existing key is
	// present in the adjacencyList HashMap. returns true if it does, false if
	// not
	public boolean isVertex(V dataForVertex) {
		nullCheck(dataForVertex);

		if (adjacencyList.containsKey(dataForVertex)) {
			return true;
		} else {
			return false;
		}
	}

	// This method returns the vertices present in a graph by simply returning a
	// set of all the keys from the adjacencyList HashMap
	public Collection<V> getVertices() {
		return (adjacencyList.keySet());
	}

	// This method is used to create an edge consisting of a weight a starting
	// Vertex and a vertex which is pointed to. This is done by creating an Edge
	// object and setting the weight and nextVertex to the values passed as
	// arguments. Once this object is created, it is added to the value
	// associated with the key associated with the initial vertex. If the
	// initial or final vertex dont exist then they are created and the method
	// calls itself to make sure the edge is still added. The only case where
	// edge isnt created is when the weight is less than 0 in which case the
	// method results false.
	public boolean createEdge(V initialVertex, V finalVertex, int weight) {
		// Null checks for each argument passed
		nullCheck(initialVertex);
		nullCheck(finalVertex);
		nullCheck(weight);

		// Check for weight being greater than 0 or equal to
		if (weight >= 0) {
			// For the case where both initial and final vertex are present
			if (isVertex(initialVertex) && isVertex(finalVertex)) {
				// Creates new Edge object with argument values
				Edge edge = new Edge(weight, finalVertex);
				ArrayList<Edge> edges = adjacencyList.get(initialVertex);
				// uses the helper method isEdge to check if the current path
				// already exists in which case it changes the value of the
				// weight associated with the edge
				if (isEdge(initialVertex, finalVertex)) {
					for (Edge currEdge : adjacencyList.get(initialVertex)) {
						if (currEdge.nextVertex.equals(finalVertex)) {
							edge = currEdge;
						}
					}
					edge.weight = weight;
					// if the path doesnt exist, the edge is added to the
					// associated initialVertex(key)
				} else {
					edges.add(edge);
				}

				// If the initialVertex and finalVertex dont exist, they are
				// both created and then the method calls itself to add these
				// methods
			} else {
				createVertex(initialVertex);
				createVertex(finalVertex);
				createEdge(initialVertex, finalVertex, weight);
			}
			// returns true on successful adding
			return true;
		} else {
			// if weight less than 0, returns false for unsuccessful adding
			return false;
		}
	}

	// helper method isEdge that sees if an edge already exists. Returns true if
	// path exists and false otherwise
	private boolean isEdge(V initialVertex, V finalVertex) {
		// goes through each edge from specified initialVertex and sees if
		// finalVertex is present
		for (Edge edge : adjacencyList.get(initialVertex)) {
			if (edge.nextVertex.equals(finalVertex)) {
				return true;
			}
		}
		return false;
	}

	// This method calculates the weight associated between two vertices if an
	// edge exists between them. In case an edge doesnt exist or either of the
	// vertices dont exist, -1 is returned
	public int edgeCost(V initialVertex, V finalVertex) {
		// Null check of arguments passed as parameters
		nullCheck(initialVertex);
		nullCheck(finalVertex);

		int currWeight = -1;
		// Don't need to check edge case if finalVertex exists as the way
		// createEdge is structured, a finalVertex will automatically be created
		// if it exists as a destination and not as a vertex as per earlier
		// requirements hence if it exists as a destination, it also exists as a
		// vertex in the adjacencyList
		if (isVertex(initialVertex) && isEdge(initialVertex, finalVertex)) {
			for (Edge currEdge : adjacencyList.get(initialVertex)) {
				if (currEdge.nextVertex.equals(finalVertex)) {
					// assigns currWeight if matching finalVertex found hence
					// indicating a path exists between intiialVertex and
					// finalVertex
					currWeight = currEdge.weight;
				}
			}
		}

		// returns currWeight (-1 is base case if path isnt found)
		return currWeight;
	}

	// This method removes a given edge using the parameters of initialVertex to
	// finalVertex. The method first finds associated path between the vertices
	// and if the path is found the edge is removed. True is returned if the
	// edge is successfully removed and false if not
	public boolean removeEdge(V initialVertex, V finalVertex) {
		nullCheck(initialVertex);
		nullCheck(finalVertex);

		// For case where initial Vertex exists and final vertex is a viable
		// path using isEdge helper method
		if (isVertex(initialVertex) && isEdge(initialVertex, finalVertex)) {
			ArrayList<Edge> edges = adjacencyList.get(initialVertex);
			// Goes through edges to find finalVertex
			for (int i = 0; i < edges.size(); i++) {
				if (edges.get(i).nextVertex.equals(finalVertex)) {
					// Removes edge from the ArrayList value of the key
					// (initialVertex)
					edges.remove(edges.get(i));
				}
			}
			// returns true on successful removing
			return true;
		} else {
			// returns false on unsuccessful removing
			return false;
		}
	}

	// This method removes a given vertex from the adjacencyList HashMap. Apart
	// from jsut removing the vertex as a key in the adjacencyList HashMap it
	// also removes the vertex as a finalVertex from any other vertices by going
	// through each vertex and seeing if a path exists between that vertex and
	// the vertex to be removed. If found, that edge is removed from that
	// vertex.
	public boolean removeVertex(V dataForVertex) {
		// Null check for argument passed in parameter
		nullCheck(dataForVertex);

		// If the Vertex doesn't exist in adjacencyList it also wont exist as a
		// destination through an edge due to how createEdge is structured
		if (isVertex(dataForVertex)) {
			// removing the vertex (key) in the adjacencyList HashMap
			adjacencyList.remove(dataForVertex);

			// Going through every other vertex to check if the removed vertex
			// is a final vertex of any other vertex in which case the edge is
			// removed using the removeEdge() method
			for (V vertex : getVertices()) {
				ArrayList<Edge> edges = adjacencyList.get(vertex);
				for (int i = 0; i < edges.size(); i++) {
					if (edges.get(i).nextVertex.equals(dataForVertex)) {

						// Edge removed if vertex to be removed is found as
						// finalVertex in other vertices
						removeEdge(vertex, dataForVertex);
					}
				}
			}

			// returns true on successful removing
			return true;
		} else {
			// returns false on unsuccessful removing
			return false;
		}
	}

	// This method returns an ArrayList of neighboring vertices i.e. vertices
	// that the vertex passed as an argument points to through its various
	// edges. This is done by creating a new ArrayList of type V (retVal) and
	// returning it. Null is returned if no vertex exists in this graph matching
	// that passed as an argument
	public Collection<V> adjacentVertices(V dataForVertex) {
		// Null check for argument passed in parameter
		nullCheck(dataForVertex);
		// ArrayList to be returned as a set of neighboring vertices
		ArrayList<V> retVal = null;

		// Checking if vertex exists
		if (isVertex(dataForVertex)) {
			retVal = new ArrayList<V>();
			// Iterating through each edge of vertex and adding neighbors to
			// retVal
			for (Edge currEdge : adjacencyList.get(dataForVertex)) {
				retVal.add(currEdge.nextVertex);
			}
		}
		// returning all neighbors added in retVal
		return retVal;
	}

	// This method returns an ArrayList of preceding neighboring vertices i.e.
	// all of the vertices that point to the vertex passed as an argument. This
	// is done by iterating over each vertex and its edges to fin which
	// vertices point to the argument vertex. When vertices with said criteria
	// are found they are added to a ArrayList of type V called retVal which is
	// returned to show all vertices pointing to argument vertex. Null is
	// returned if no vertex exists in this graph matching that passed as an
	// argument
	public Collection<V> predecessorsOfVertex(V destVertex) {
		// Null check for argument passed in parameter
		nullCheck(destVertex);
		boolean flag = false;
		// ArrayList to be returned as a set of neighboring vertices
		ArrayList<V> retVal = null;

		// Finds if any vertices exist that have matching paths where final
		// vertex is same as that passed in argument.
		for (V vertex : getVertices()) {
			if (isEdge(vertex, destVertex)) {
				flag = true;
			}
		}

		// If there exists such a path, each vertex is iterated over and
		// vertices with edges pointing to the argument vertex are stored in
		// retVal
		if (flag) {
			retVal = new ArrayList<V>();
			for (V vertex : getVertices()) {
				ArrayList<Edge> edges = adjacencyList.get(vertex);
				// Iterating over each edge to check for nextVertex to be vertex
				// passed as argument
				for (Edge currEdge : edges) {
					if (currEdge.nextVertex.equals(destVertex)) {
						// If suitable edge is found, initial vertex is added to
						// retVal
						retVal.add(vertex);
					}
				}
			}
		}
		// returning all preceding neighbors added in retVal
		return retVal;
	}

	// This method returns a new graph on the basis of the argument
	// verticesForNewGraph which is a collection of vertices to be removed from
	// the current graph object and returned as a separate graph. This is
	// achieved by instantiated a new DWGraph object and adding all matching
	// vertices from the collection found in the current object to the new
	// DWGraph object called retGraph. To be added, edges connecting
	// initialVertex and finalVertex must have both these vertices present in
	// The collection. If either is missing, the edge does not exist in either
	// current Graph object nor the returned new retGraph. If neither
	// initialVertex nor finalVertex exist then the edge is retained however if
	// both exist they are removed from the current graph and only present in
	// the new returned retGraph.
	public DWGraph<V> divideGraph(Collection<V> verticesForNewGraph) {
		// Null check for argument passed in parameter
		nullCheck(verticesForNewGraph);
		int tempCounter = 0;
		DWGraph<V> retGraph = new DWGraph<V>();
		// Array list of vertices to be removed after they have been moved to
		// the new graph consisting of the vertices in the argument passed
		ArrayList<V> vertsToRem = new ArrayList<V>();

		// Goes through each vertex in collection in argument and sees if
		// matching vertex exists in the current graph object.
		for (V vertex : verticesForNewGraph) {
			if (isVertex(vertex)) {
				// If vertex exists, new vertex is added to retGraph
				retGraph.createVertex(vertex);
				// Checks if finalVertex of each edge is present in vertices
				// collection in argument, if it is, it is added in the retGraph
				// otherwise skipped
				for (Edge edge : adjacencyList.get(vertex)) {
					if (verticesForNewGraph.contains(edge.nextVertex)) {
						tempCounter++;
						retGraph.createEdge(vertex, edge.nextVertex,
								edge.weight);
					}
				}
			}

			// tempcounter is used for two edge cases namely, if neither
			// initial vertex nor final vertex are found in the vertices in
			// collection in argument in which case the new vertex added in
			// retGraph is deleted and the edge is maintained in current object
			// or if any pairs are added to retGraph that means any remaining
			// edges have either initial vertex or final vertex missing from
			// vertices collection in which case the vertex shouldn't exist in
			// either graph so it is removed form current object as well
			if (tempCounter == 0) {
				retGraph.removeVertex(vertex);
			} else {
				vertsToRem.add(vertex);
			}

			// resetting temp counter for next case of vertices
			tempCounter = 0;
		}

		// Removing each vertex from the current object graph as specified by
		// vertsToRem array as these vertices now exist in the retGraph
		for (V vertex : vertsToRem) {
			removeVertex(vertsToRem.get(vertsToRem.indexOf(vertex)));
		}

		// returning new retGraph with vertices from collection in argument
		return retGraph;
	}

}
