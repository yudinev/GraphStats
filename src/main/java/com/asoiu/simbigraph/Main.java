package com.asoiu.simbigraph;

import com.asoiu.simbigraph.algorithms.GraphStatsOperation;
import com.asoiu.simbigraph.algorithms.shortestpath.ParallelDistanceStatistics;
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
     * @param args
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
        	requestedOperation.add(new ParallelDistanceStatistics<Integer, Integer>(graph, parameters.getThreadCount()));
        }
        if (requestedOperation.isEmpty()) {
        	LOG.warn("No one of available operations has been requested.");
        } else {
	        for (GraphStatsOperation graphStatsOperation : requestedOperation) {
	        	startTime = System.nanoTime();
	        	graphStatsOperation.execute();
	        	LOG.info(graphStatsOperation);
	        	LOG.info("Elapsed time = {}.", FormatUtils.durationToHMS(System.nanoTime() - startTime));
			}
        }
    }

    private static ProgramParameters parseCmd(String[] args) {
        ArgumentParser parser = new ArgumentParser();
        try {
            parameters = parser.parseCmdParameters(args);
        } catch (ParseException | NumberFormatException e) {
            LOG.error("Can't parse cmd parameters.");
            LOG.debug("Can't parse cmd parameters. {}", e);
            System.exit(1);
        }
        return parameters;
    }

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
            LOG.debug("Failed to load graph from {} file. {}", parameters.getGraphFile(), e);
            System.exit(1);
        }
        return graph;
    }


    private static Hypergraph<Integer, Integer> loadGraph(String path) throws IOException {
        return new PajekNetReader<>(createIntegerFactory(), createIntegerFactory()).load(path, new AdjacencyListGraph<>());
    }

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