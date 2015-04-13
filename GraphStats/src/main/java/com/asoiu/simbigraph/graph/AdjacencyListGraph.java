package com.asoiu.simbigraph.graph;

import edu.uci.ics.jung.graph.AbstractGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;

/**
 * An implementation of <code>Graph</code> which represents the graph as an adjacency list and
 * permits directed or undirected edges only.
 *
 * @author Andrey Kurchanov
 */
public class AdjacencyListGraph<V,E> extends AbstractGraph<V,E> {

    private Map<V, List<V>> mGraph;

    private EdgeType edgeType;

    public AdjacencyListGraph() {
        mGraph = new HashMap<>();
    }

    @Override
    public Collection<V> getVertices() {
        return new ArrayList<>(mGraph.keySet());
    }

    @Override
    public boolean containsVertex(V vertex) {
        return mGraph.containsKey(vertex);
    }

    @Override
    public int getEdgeCount() {
        int edgeCount = 0;
        for (List<V> list : mGraph.values()) {
            edgeCount += list.size();
        }
        return edgeType == EdgeType.UNDIRECTED ? edgeCount / 2 : edgeCount;
    }

    @Override
    public int getVertexCount() {
        return mGraph.size();
    }

    @Override
    public Collection<V> getNeighbors(V vertex) {
        Collection<V> neighbors = new ArrayList<>(getSuccessors(vertex));
        if (edgeType == EdgeType.DIRECTED) {
            neighbors.addAll(getPredecessors(vertex));
        }
        return neighbors;
    }

    @Override
    public boolean addVertex(V vertex) {
        // Add the vertex with an empty list of outgoing edges.
    	if (!containsVertex(vertex)) {
    		mGraph.put(vertex, new ArrayList<>());
    	}
        return true;
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
    public int getEdgeCount(EdgeType edge_type) {
        return getEdgeCount();
    }

    @Override
    public Collection<V> getPredecessors(V vertex) {
        List<V> predecessors = new ArrayList<>(mGraph.size() / 2);
        for (Map.Entry<V, List<V>> pair : mGraph.entrySet()) {
            if (pair.getValue().contains(vertex)) {
                predecessors.add(pair.getKey());
            }
        }
        return predecessors.size() == 0 ? new ArrayList<>() : predecessors;
    }

    @Override
    public Collection<V> getSuccessors(V vertex) {
        return mGraph.get(vertex);
    }

    @Override
    public boolean addEdge(E edge, edu.uci.ics.jung.graph.util.Pair<? extends V> endpoints, EdgeType edgeType) {
        this.edgeType = edgeType;

        if (mGraph.get(endpoints.getFirst()).contains(endpoints.getSecond()) == false) {
        	// Add the edge.
        	if (edgeType == EdgeType.UNDIRECTED) {
        		mGraph.get(endpoints.getFirst()).add(endpoints.getSecond());
        		mGraph.get(endpoints.getSecond()).add(endpoints.getFirst());
        	} else {
        		mGraph.get(endpoints.getFirst()).add(endpoints.getSecond());
        	}
        }
        return true;
    }

    // NOT IMPLEMENTED

    @Override
    public Collection<E> getEdges() throws NotImplementedException {
        throw new NotImplementedException();
    }

    @Override
    public boolean containsEdge(E edge) throws NotImplementedException {
        throw new NotImplementedException();
    }

    @Override
    public Collection<E> getIncidentEdges(V vertex) throws NotImplementedException {
        throw new NotImplementedException();
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
    public Collection<E> getEdges(EdgeType edge_type) throws NotImplementedException {
        throw new NotImplementedException();
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
    
}