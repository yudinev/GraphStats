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
     * array will be <code>Integer.MAX_VALUE</code>.
     * <p>
     * This method uses Function and Parallel Stream features of JDK 1.8 for
     * parallel execution.
     */
    private void calculateEccentricities(Hypergraph<V, E> g) {
        ParallelUnweightedShortestPath<V, E> d = new ParallelUnweightedShortestPath<V, E>(g);
        vertices = g.getVertices();
        Function<V, Integer> f_inner = (v) -> {
            int di = d.getEccentricity(v);
            return di;
        };
        //eccs = vertices.stream().parallel().map(f_inner).sorted().toArray();
        eccs = vertices.stream().map(f_inner).sorted().toArray();
    }

    /**
     * The getDiameter is defined to be the maximum, over all pairs of vertices
     * <code>u,v</code>, of the length of the shortest path from <code>u</code>
     * to <code>v</code>, or <code>Double.POSITIVE_INFINITY</code> if any of
     * these distances do not exist.
     *
     * @return the getDiameter of <code>g</code>, ignoring edge weights.
     */
    public Integer getDiameter() {
        return (Integer) eccs[eccs.length - 1];
    }

    /**
     * The getRadius is defined to be the minimum, over all pairs of vertices <code>u,v</code>
     * , of the length of the shortest path from <code>u</code> to
     * <code>v</code>, or <code>Double.POSITIVE_INFINITY</code> if any of these
     * distances do not exist.
     *
     * @return the getRadius of <code>g</code>, ignoring edge weights.
     */
    public Integer getRadius() {
        return (Integer) eccs[0];
    }
}