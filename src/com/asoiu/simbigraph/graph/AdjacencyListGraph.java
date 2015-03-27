package com.asoiu.simbigraph.graph;

import java.util.ArrayList;
import java.util.Collection;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import edu.uci.ics.jung.graph.AbstractGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;

import javafx.util.Pair;

/**
 * An implementation of <code>Graph</code> which represents the graph as an adjacency list and
 * permits directed or undirected edges only.
 * 
 * @author Andrey Kurchanov
 */
public class AdjacencyListGraph<V,E>
	extends AbstractGraph<V,E>
	implements Graph<V,E> {	
	private ArrayList<Pair<V, ArrayList<V>>> mGraph;	
	private EdgeType edgeType;
	
	public AdjacencyListGraph() {
		mGraph = new ArrayList<Pair<V, ArrayList<V>>>();
	}
	
	@Override
	public Collection<E> getEdges() throws NotImplementedException {
		throw new NotImplementedException();
	}

	@Override
	public Collection<V> getVertices() {
		Collection<V> vertices = new ArrayList<V>();
		for (Pair<V, ArrayList<V>> pair : mGraph) {
			vertices.add(pair.getKey());
		}
		return vertices;
	}

	@Override
	public boolean containsVertex(V vertex) {
		for (Pair<V, ArrayList<V>> pair : mGraph) {
			if (pair.getKey().equals(vertex))
				return true;
		}
		return false;
	}

	@Override
	public boolean containsEdge(E edge) throws NotImplementedException {
		throw new NotImplementedException();
	}

	@Override
	public int getEdgeCount() {
		int edgeCount = 0;
		for (Pair<V, ArrayList<V>> pair : mGraph) {
			edgeCount += pair.getValue().size();
		}
		if (edgeType.name() == "UNDIRECTED")
			return edgeCount / 2;
		else
			return edgeCount;
	}

	@Override
	public int getVertexCount() {
		return mGraph.size();
	}

	@Override
	public Collection<V> getNeighbors(V vertex) {
		Collection<V> neighbors = new ArrayList<>();
		neighbors.addAll(getSuccessors(vertex));
		if (edgeType.name() == "DIRECTED")
			neighbors.addAll(getPredecessors(vertex));
		return neighbors;
	}

	@Override
	public Collection<E> getIncidentEdges(V vertex) throws NotImplementedException {
		throw new NotImplementedException();
	}

	@Override
	public boolean addVertex(V vertex) {
		// Add the vertex with an empty list of outgoing edges.
        mGraph.add(new Pair<V, ArrayList<V>>(vertex, new ArrayList<>()));
        return true;
	}

	@Override
	public boolean removeVertex(V vertex) throws NotImplementedException {
		throw new NotImplementedException();
	}

	@Override
	public boolean removeEdge(E edge) throws NotImplementedException {
		throw new NotImplementedException();
	}

	@Override
	public EdgeType getEdgeType(E edge) {
		return edgeType;
	}

	@Override
	public EdgeType getDefaultEdgeType() {
		return edgeType;
	}

	@Override
	public Collection<E> getEdges(EdgeType edge_type) throws NotImplementedException {
		throw new NotImplementedException();
	}

	@Override
	public int getEdgeCount(EdgeType edge_type) {
		return getEdgeCount();
	}

	@Override
	public Collection<E> getInEdges(V vertex) throws NotImplementedException {
		throw new NotImplementedException();
	}

	@Override
	public Collection<E> getOutEdges(V vertex) throws NotImplementedException {
		throw new NotImplementedException();
	}

	@Override
	public Collection<V> getPredecessors(V vertex) {
		Collection<V> predecessors = new ArrayList<>();
		for (Pair<V, ArrayList<V>> pair : mGraph) {
			if (pair.getValue().contains(vertex))
				predecessors.add(pair.getKey());
		}
		return predecessors;
	}

	@Override
	public Collection<V> getSuccessors(V vertex) {
		return mGraph.get((Integer) vertex).getValue();
	}

	@Override
	public V getSource(E directed_edge) throws NotImplementedException {
		throw new NotImplementedException();
	}

	@Override
	public V getDest(E directed_edge) throws NotImplementedException {
		throw new NotImplementedException();
	}

	@Override
	public boolean isSource(V vertex, E edge) throws NotImplementedException {
		throw new NotImplementedException();
	}

	@Override
	public boolean isDest(V vertex, E edge) throws NotImplementedException {
		throw new NotImplementedException();
	}

	@Override
	public edu.uci.ics.jung.graph.util.Pair<V> getEndpoints(E edge) throws NotImplementedException {
		throw new NotImplementedException();
	}

	@Override
	public boolean addEdge(E edge, edu.uci.ics.jung.graph.util.Pair<? extends V> endpoints, EdgeType edgeType) {
		this.edgeType = edgeType;

        // Add the edge.
		if (edgeType.name() == "UNDIRECTED") {
			mGraph.get((Integer) endpoints.getFirst()).getValue().add(endpoints.getSecond());
			mGraph.get((Integer) endpoints.getSecond()).getValue().add(endpoints.getFirst());
		} else {
			mGraph.get((Integer) endpoints.getFirst()).getValue().add(endpoints.getSecond());
		}
		return true;
	}
}