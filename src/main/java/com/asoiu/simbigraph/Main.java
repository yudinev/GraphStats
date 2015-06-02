package com.asoiu.simbigraph;

import com.asoiu.simbigraph.algorithms.GraphStatsOperation;
import com.asoiu.simbigraph.algorithms.shortestpath.ParallelDistanceStatistics;
import com.asoiu.simbigraph.algorithms.subgraph.ParallelFourSizeSubgraphsCounterFullEnumeration;
import com.asoiu.simbigraph.algorithms.subgraph.ParallelFourSizeSubgraphsCounterSampling;
import com.asoiu.simbigraph.algorithms.subgraph.ParallelThreeSizeSubgraphsCounterFullEnumeration;
import com.asoiu.simbigraph.algorithms.subgraph.ParallelThreeSizeSubgraphsCounterSampling;
import com.asoiu.simbigraph.exception.GraphStatsException;
import com.asoiu.simbigraph.graph.AdjacencyListGraph;
import com.asoiu.simbigraph.util.ArgumentParser;
import com.asoiu.simbigraph.util.FormatUtils;
import com.asoiu.simbigraph.util.ProgramParameters;

import edu.uci.ics.jung.graph.Hypergraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.io.PajekNetReader;

import org.apache.commons.cli.ParseException;
import org.apache.commons.collections15.Factory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrey Kurchanov
 */
public class Main {

	private static final Logger LOG = LogManager.getLogger(Main.class.getName());

    private static ProgramParameters parameters;

    /**
     * This is entry point in the program.
     * 
     * @author Andrey Kurchanov
     * @param args input arguments
     */
    public static void main(String[] args) {
        long startTime;
        Hypergraph<Integer, Integer> graph;

        ProgramParameters parameters = parseCmd(args);
        graph = initGraph();

        if (graph.getVertexCount() == 0) {
            LOG.error("Graph is empty.");
            System.exit(1);
        }
        LOG.info("Vertices = {}.", graph.getVertexCount());
        LOG.info("{} = {}.", graph.getDefaultEdgeType() == EdgeType.DIRECTED ? "Arcs" : "Edges", graph.getEdgeCount());
        
        List<GraphStatsOperation> requestedOperation = new ArrayList<GraphStatsOperation>();
        if (parameters.getIsDiameterRadiusRequestedFlag()) {
        	requestedOperation.add(new ParallelDistanceStatistics<Integer, Integer>(graph, parameters.getNumberOfThreads()));
        }
        if (parameters.getIsThreeSizeSubgraphsCountFullEnumerationRequestedFlag()) {
        	requestedOperation.add(new ParallelThreeSizeSubgraphsCounterFullEnumeration<Integer, Integer>(graph, parameters.getNumberOfThreads()));
        }
        if (parameters.getIsThreeSizeSubgraphsCountSamplingRequestedFlag()) {
        	requestedOperation.add(new ParallelThreeSizeSubgraphsCounterSampling<Integer, Integer>(graph, parameters.getNumberOfRuns(), parameters.getNumberOfThreads()));
		}
        if (parameters.getIsFourSizeSubgraphsCountFullEnumerationRequestedFlag()) {
        	requestedOperation.add(new ParallelFourSizeSubgraphsCounterFullEnumeration<Integer, Integer>(graph, parameters.getNumberOfThreads()));
		}
        if (parameters.getIsFourSizeSubgraphsCountSamplingRequestedFlag()) {
        	requestedOperation.add(new ParallelFourSizeSubgraphsCounterSampling<Integer, Integer>(graph, parameters.getNumberOfRuns(), parameters.getNumberOfThreads()));
		}
        if (requestedOperation.isEmpty()) {
        	LOG.warn("No one of available operations has been requested.");
        } else {
	        for (GraphStatsOperation graphStatsOperation : requestedOperation) {
	        	startTime = System.nanoTime();
	        	try {
					graphStatsOperation.execute();
					LOG.info(graphStatsOperation);
				} catch (GraphStatsException e) {
					LOG.error(e.getMessage());
					LOG.debug(e);
				}
	        	LOG.info("Elapsed time = {}.", FormatUtils.durationToHMS(System.nanoTime() - startTime));
			}
        }
    }
    
	/**
	 * This is a wrapper for <code>parseCmdParameters(String[])</code> method of
	 * the <code>com.asoiu.simbigraph.util.ArgumentParser</code> instance.
	 * 
	 * @author Andrey Kurchanov
	 * @param args input arguments
	 * @see com.asoiu.simbigraph.util.ArgumentParser#parseCmdParameters(String[])
	 * @return the instance of <code>com.asoiu.simbigraph.util.ProgramParameters</code> if
	 *         input arguments were parsed successfully, otherwise thrown
	 *         exception is logged and the program shuts down
	 */
    private static ProgramParameters parseCmd(String[] args) {
        ArgumentParser parser = new ArgumentParser();
        try {
            parameters = parser.parseCmdParameters(args);
        } catch (ParseException | NumberFormatException e) {
            LOG.error("Can't parse cmd parameters.");
            LOG.debug("Can't parse cmd parameters.", e);
            System.exit(1);
        }
        return parameters;
    }

	/**
	 * This is a wrapper for <code>loadGraph(String)</code> method.
	 * 
	 * @author Andrey Kurchanov
	 * @see com.asoiu.simbigraph.Main#loadGraph(String)
	 * @return the instance of a class which implements
	 *         <code>edu.uci.ics.jung.graph.Hypergraph</code> interface if
	 *         specific graph was loaded successfully, otherwise thrown
	 *         exception is logged and the program shuts down
	 */
    private static Hypergraph<Integer, Integer> initGraph() {
        long startTime;
        Hypergraph<Integer, Integer> graph = null;

        LOG.info("Loading graph from {} file.", parameters.getGraphFile());
        startTime = System.nanoTime();
        try {
            graph = loadGraph(parameters.getGraphFile());
            LOG.info("Graph successfully loaded in {}.", FormatUtils.durationToHMS(System.nanoTime() - startTime));
        } catch (IOException e) {
        	LOG.error("Failed to load graph from {} file.", parameters.getGraphFile());
            LOG.debug("Failed to load graph from {} file.", parameters.getGraphFile(), e);
            System.exit(1);
        }
        return graph;
    }

	/**
	 * Loads information about specific graph by using
	 * <code>load(String, edu.uci.ics.jung.graph.Graph)</code> method of
	 * <code>edu.uci.ics.jung.io.PajekNetReader</code> instance.
	 * 
	 * @author Andrey Kurchanov
	 * @param path a string representation of the path to the graph file
	 * @throws IOException
	 * @see edu.uci.ics.jung.io.PajekNetReader#load(String, edu.uci.ics.jung.graph.Graph)
	 * @return the instance of a class which implements
	 *         <code>edu.uci.ics.jung.graph.Hypergraph</code> interface if
	 *         specific graph was loaded successfully, otherwise
	 *         <code>IOException</code> is thrown
	 */
    private static Hypergraph<Integer, Integer> loadGraph(String path) throws IOException {
        return new PajekNetReader<>(createIntegerFactory(), createIntegerFactory()).load(path, new AdjacencyListGraph<>());
    }

    /**
     * @author Andrey Kurchanov
     * @return the factory object
     */
    private static Factory<Integer> createIntegerFactory() {
        return new Factory<Integer>() {
            private int n = 0;

            @Override
            public Integer create() {
                return n++;
            }
        };
    }
    
}