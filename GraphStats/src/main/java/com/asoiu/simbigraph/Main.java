package com.asoiu.simbigraph;

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

/**
 * @author Andrey Kurchanov
 */
public class Main {

    private static final Logger LOG = LogManager.getLogger(Main.class); //todo config file appender

    private static ProgramParameters parameters;

    /**
     * @param args
     */
    public static void main(String[] args) {
        long startTime;
        ParallelDistanceStatistics<Integer, Integer> pds;
        Hypergraph<Integer, Integer> graph;

        parseCmd(args);
        graph = initGraph();

        if (graph.getVertexCount() == 0) {
            LOG.info("Graph is empty.");
            System.exit(1);
        }

        pds = new ParallelDistanceStatistics<>(graph, parameters.getThreadCount());

        LOG.info("Vertices = {}", graph.getVertexCount());
        LOG.info("{} = {}", graph.getDefaultEdgeType() == EdgeType.DIRECTED ? "Arcs" : "Edges", graph.getEdgeCount());

        startTime = System.nanoTime();
        pds.calculateEccentricities();
        LOG.info("Diameter = {}", pds.getDiameter());
        LOG.info("Radius = {}", pds.getRadius());
        LOG.info("Elapsed time = {}", FormatUtils.durationToHMS(System.nanoTime() - startTime));
    }

    private static void parseCmd(String[] args) {
        ArgumentParser parser = new ArgumentParser();
        try {
            parameters = parser.parseCmdParameters(args);
        } catch (ParseException | NumberFormatException e) {
            LOG.debug("Can't parse cmd parameters");
            System.exit(1);
        }
    }

    private static Hypergraph<Integer, Integer> initGraph() {
        long startTime;
        Hypergraph<Integer, Integer> graph = null;

        LOG.info("Loading graph from {}", parameters.getGraphFile());
        startTime = System.nanoTime();
        try {
            graph = loadGraph(parameters.getGraphFile());
            LOG.info("Graph successfully loaded in {} ", FormatUtils.durationToHMS(System.nanoTime() - startTime));
        } catch (IOException e) {
            LOG.debug("Failed to load graph. {}", e);
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