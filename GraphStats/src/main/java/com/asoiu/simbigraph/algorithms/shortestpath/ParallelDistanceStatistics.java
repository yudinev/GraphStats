package com.asoiu.simbigraph.algorithms.shortestpath;

import edu.uci.ics.jung.graph.Hypergraph;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

/**
 * This is parallel version of
 * edu.uci.ics.jung.algorithms.shortestpath.DistanceStatistics class.
 *
 * @author Andrey Kurchanov
 * @see edu.uci.ics.jung.algorithms.shortestpath.DistanceStatistics
 */
public class ParallelDistanceStatistics<V, E> {

    private static final Logger LOG = LogManager.getLogger(ParallelDistanceStatistics.class); //todo use async logger

    private Hypergraph<V, E> graph;

    private Collection<V> vertices;

    private Object[] eccs;

    private int numberOfThreads;

    /**
     * Constructs and initializes class.
     * Also this method run <code>calculateEccentricities()</code> method.
     *
     * @param graph the graph
     * @param numberOfThreads number of parallel threads
     * @see com.asoiu.simbigraph.algorithms.shortestpath.ParallelDistanceStatistics#calculateEccentricities()
     */
    public ParallelDistanceStatistics(Hypergraph<V, E> graph, int numberOfThreads) {
        this.graph = graph;
        this.numberOfThreads = numberOfThreads;
    }

    /**
     * Saves eccentricities of the <code>graph</code> vertices into
     * <code>eccs</code> array. The eccentricity is defined to be the maximum,
     * over all pairs of vertices <code>u,v</code>, of the length of the
     * shortest path from <code>u</code> to <code>v</code>. If the graph is
     * disconnected (that is, some vertex is not reachable from another
     * vertices), the value associated with the vertex into <code>eccs</code>
     * array will be 0.
     * <p>
     * This method uses Function and Parallel Stream features of JDK 1.8 and
     * custom ForkJoinPool for parallel execution.
     */
    public void calculateEccentricities() {
    	UnweightedShortestPath<V, E> d = new UnweightedShortestPath<>(graph);
    	vertices = graph.getVertices();
    	ForkJoinPool forkJoinPool = new ForkJoinPool(numberOfThreads);
		try {
            eccs = forkJoinPool.submit(() -> vertices.stream().parallel().map(vertex -> d.getEccentricity(vertex)).sorted().toArray()).get();
        } catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    /**
     * The getDiameter is defined to be the maximum, over all pairs of vertices <code>u,v</code>
     * , of the length of the shortest path from <code>u</code> to
     * <code>v</code>.
     *
     * @return the diameter of <code>graph</code>, ignoring edge weights.
     */
    public int getDiameter() {
		return (int) eccs[eccs.length - 1];
    }

    /**
     * The getRadius is defined to be the minimum, over all pairs of vertices <code>u,v</code>
     * , of the length of the shortest path from <code>u</code> to
     * <code>v</code>.
     *
     * @return the radius of <code>graph</code>, ignoring edge weights.
     */
    public int getRadius() {
    	return (int) eccs[0];
    }
}