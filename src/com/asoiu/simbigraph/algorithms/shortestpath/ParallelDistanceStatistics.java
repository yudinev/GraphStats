package com.asoiu.simbigraph.algorithms.shortestpath;

import edu.uci.ics.jung.graph.Hypergraph;

import java.util.Collection;
import java.util.function.Function;

/**
 * This is parallel version of
 * edu.uci.ics.jung.algorithms.shortestpath.DistanceStatistics class.
 *
 * @author Andrey Kurchanov
 * @see edu.uci.ics.jung.algorithms.shortestpath.DistanceStatistics
 */
public class ParallelDistanceStatistics<V, E> {
    private Hypergraph<V, E> graph;
    private Collection<V> vertices;
    private Object[] eccs;

    public ParallelDistanceStatistics(Hypergraph<V, E> graph) {
        this.graph = graph;
        calculateEccentricities(this.graph);
    }

    /**
     * Saves eccentricities of the <code>g</code> vertices into
     * <code>eccs</code> array. The eccentricity is defined to be the maximum,
     * over all pairs of vertices <code>u,v</code>, of the length of the
     * shortest path from <code>u</code> to <code>v</code>. If the graph is
     * disconnected (that is, some vertex is not reachable from another
     * vertices), the value associated with the vertex into <code>eccs</code>
     * array will be <code>Double.POSITIVE_INFINITY</code>.
     * <p>
     * This method uses Function and Parallel Stream features of JDK 1.8 for
     * parallel execution.
     */
    private void calculateEccentricities(Hypergraph<V, E> g) {
        ParallelUnweightedShortestPath<V, E> d = new ParallelUnweightedShortestPath<>(g);
        this.vertices = g.getVertices();
        Function<V, Double> f_inner = (v) -> {
            int di = d.getEccentricity(v);
            return (di == Integer.MAX_VALUE) ? Double.POSITIVE_INFINITY : (double)di;
        };
        this.eccs = this.vertices.parallelStream().map(f_inner).sorted().toArray();
    }

    /**
     * The getDiameter is defined to be the maximum, over all pairs of vertices
     * <code>u,v</code>, of the length of the shortest path from <code>u</code>
     * to <code>v</code>, or <code>Double.POSITIVE_INFINITY</code> if any of
     * these distances do not exist.
     *
     * @return the getDiameter of <code>g</code>, ignoring edge weights.
     */
    public double getDiameter() {
        return (Double)eccs[eccs.length - 1];
    }

    /**
     * The getRadius is defined to be the minimum, over all pairs of vertices <code>u,v</code>
     * , of the length of the shortest path from <code>u</code> to
     * <code>v</code>, or <code>Double.POSITIVE_INFINITY</code> if any of these
     * distances do not exist.
     *
     * @return the getRadius of <code>g</code>, ignoring edge weights.
     */
    public double getRadius() {
        return (Double)eccs[0];
    }
}