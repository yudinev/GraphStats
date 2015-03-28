package com.asoiu.simbigraph.algorithms.shortestpath;

import java.util.Comparator;
import java.util.PriorityQueue;

import edu.uci.ics.jung.graph.Hypergraph;

import javafx.util.Pair;

/**
 * This is parallel version of
 * edu.uci.ics.jung.algorithms.shortestpath.UnweightedShortestPath class.
 *
 * @author Andrey Kurchanov
 * @see edu.uci.ics.jung.algorithms.shortestpath.UnweightedShortestPath
 */
public class ParallelUnweightedShortestPath<V, E> {
    private Hypergraph<V, E> graph;

    /**
     * Constructs and initializes algorithm
     *
     * @param graph the graph
     */
    public ParallelUnweightedShortestPath(Hypergraph<V, E> graph) {
        this.graph = graph;
    }

    /**
     * Computes the shortest path distances from a given vertex to all other
     * vertices.
     *
     * @param source the source vertex
     */
    public int getEccentricity(V source) {
    	int ecc = 0;
    	int[] distances = new int[graph.getVertexCount()];
        for (int i = 0; i < distances.length; i++) {
        	distances[i] = Integer.MAX_VALUE;
        }	
        distances[(Integer) source] = 0;
        PriorityQueue<Pair<V, Integer>> q = new PriorityQueue<Pair<V, Integer>>(
            100, new Comparator<Pair<V, Integer>>() {
            @Override
            public int compare(Pair<V, Integer> o1, Pair<V, Integer> o2) {
                return (o1.getValue() > o2.getValue() ? 1 : -1);
            }
        }
        );
        q.add(new Pair<V, Integer>(source, 0));
        Pair<V, Integer> tempPair;
        V v;
        int cur_d;
        while (!q.isEmpty()) {
        	tempPair = q.poll();
        	v = tempPair.getKey();
        	cur_d = tempPair.getValue();
            if (cur_d > distances[(Integer) v]) {
                continue;
            }
            for (V neighbor : graph.getSuccessors(v)) {
                if (distances[(Integer) v] + 1 < distances[(Integer) neighbor]) {
                    distances[(Integer) neighbor] = distances[(Integer) v] + 1;
                    q.add(new Pair<V, Integer>(neighbor, distances[(Integer) neighbor]));
                }
            }
            if (distances[(Integer) v] > ecc)
                ecc = distances[(Integer) v];
        }
        return ecc;
    }
}