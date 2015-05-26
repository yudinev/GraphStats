package com.asoiu.simbigraph.algorithms.shortestpath;

import edu.uci.ics.jung.graph.Hypergraph;

import com.asoiu.simbigraph.algorithms.GraphStatsOperation;

import java.util.Collection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

/**
 * This is parallel version of the algorithm finding the radius and the diameter
 * of the graph.
 *
 * @author Andrey Kurchanov
 */
public class ParallelDistanceStatistics<V, E> implements GraphStatsOperation {
	
    private Hypergraph<V, E> graph;

    private Collection<V> vertices;

    private Object[] eccs;

    private int numberOfThreads;

    /**
     * Constructs and initializes the class.
     *
     * @author Andrey Kurchanov
     * @param graph the graph
     * @param numberOfThreads number of parallel threads
     */
    public ParallelDistanceStatistics(Hypergraph<V, E> graph, int numberOfThreads) {
        this.graph = graph;
        this.numberOfThreads = numberOfThreads;
    }

	/**
	 * Saves eccentricities of the <code>graph</code> vertices into
	 * <code>eccs</code> array.<br>
	 * The eccentricity is defined to be the maximum, over all pairs of vertices
	 * <code>u,v</code>, of the length of the shortest path from <code>u</code>
	 * to <code>v</code>.<br>
	 * If the graph is disconnected (that is, some vertex is not reachable from
	 * another vertices), the value associated with the vertex into
	 * <code>eccs</code> array will be 0.
	 * <p>
	 * The method uses Function and Parallel Stream features of Java 1.8 and
	 * custom ForkJoinPool for parallel execution.
	 * 
	 * @author Andrey Kurchanov
	 */
    @Override
    public void execute() {
    	UnweightedShortestPath<V, E> d = new UnweightedShortestPath<>(graph);
    	vertices = graph.getVertices();
    	ForkJoinPool forkJoinPool = new ForkJoinPool(numberOfThreads);
		try {
            eccs = forkJoinPool.submit(() -> vertices.stream().parallel().map(vertex -> d.getEccentricity(vertex)).sorted().toArray()).get();
        } catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
    }

	/**
	 * The getDiameter is defined to be the maximum, over all pairs of vertices
	 * <code>u,v</code>, of the length of the shortest path from <code>u</code>
	 * to <code>v</code>.
	 *
	 * @author Andrey Kurchanov
	 * @return the diameter of the <code>graph</code>, ignoring edge weights.
	 */
    private int getDiameter() {
		return (int) eccs[eccs.length - 1];
    }

	/**
	 * The getRadius is defined to be the minimum, over all pairs of vertices
	 * <code>u,v</code>, of the length of the shortest path from <code>u</code>
	 * to <code>v</code>.
	 *
	 * @author Andrey Kurchanov
	 * @return the radius of the <code>graph</code>, ignoring edge weights.
	 */
    private int getRadius() {
    	return (int) eccs[0];
    }
    
    /**
     * @author Andrey Kurchanov
     */
    @Override
    public String toString() {
    	return "Diameter = " + getDiameter() + ". Radius = " + getRadius() + ".";
    }
    
}