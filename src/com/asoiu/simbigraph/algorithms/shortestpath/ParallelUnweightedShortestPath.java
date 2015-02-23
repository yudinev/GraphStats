package com.asoiu.simbigraph.algorithms.shortestpath;

import edu.uci.ics.jung.graph.Hypergraph;
import javafx.util.Pair;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * This is parallel version of
 * edu.uci.ics.jung.algorithms.shortestpath.UnweightedShortestPath class.
 *
 * @author Andrey Kurchanov
 * @see edu.uci.ics.jung.algorithms.shortestpath.UnweightedShortestPath
 */
public class ParallelUnweightedShortestPath<V, E> {
    private Hypergraph<V, E> mGraph;

    /**
     * Constructs and initializes algorithm
     *
     * @param g the graph
     */
    public ParallelUnweightedShortestPath(Hypergraph<V, E> g) {
        this.mGraph = g;
    }

    /**
     * Computes the shortest path distances from a given node to all other
     * nodes.
     *
     * @param source the source node
     */
    public int getEccentricity(V source) {
        int ecc = 0;
        Map<V, Integer> distances = new HashMap<V, Integer>();
        for (V v : mGraph.getVertices()) {
            distances.put(v, Integer.MAX_VALUE);
        }
        distances.replace(source, 0);
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
        Integer cur_d;
        while (!q.isEmpty()) {
        	tempPair = q.poll();
        	v = tempPair.getKey();
        	cur_d = tempPair.getValue();
            if (cur_d > distances.get(v)) {
                continue;
            }
            for (V neighbor : mGraph.getNeighbors(v)) {
                if (distances.get(v) + 1 < distances.get(neighbor)) {
                    distances.replace(neighbor, distances.get(v) + 1);
                    q.add(new Pair<V, Integer>(neighbor, distances.get(v) + 1));
                }
            }
            if (distances.get(v) > ecc) {
                ecc = distances.get(v);
            }
        }
        return ecc;
    }
}