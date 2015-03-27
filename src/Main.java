import java.io.IOException;

import org.apache.commons.collections15.Factory;

import com.asoiu.simbigraph.algorithms.shortestpath.ParallelDistanceStatistics;
import com.asoiu.simbigraph.graph.AdjacencyListGraph;

import edu.uci.ics.jung.graph.Hypergraph;
import edu.uci.ics.jung.io.PajekNetReader;

/**
 * @author Andrey Kurchanov
 */
public class Main {
    /**
     * @param args
     */
    public static void main(String[] args) {
        String  path;
        Hypergraph<Integer, Integer> graph = null;
        ParallelDistanceStatistics<Integer, Integer> pds;

        if (args.length != 1) {
            throw new IllegalArgumentException("Usage: java -jar GraphStats.jar <PathToGraphFile>");
        } else {
            path = args[0];
        }

        System.out.println("Loading graph from the " + path + " file");
        try {
			graph = loadGraph(path);
		} catch (IOException e) {
			System.out.println("Failed to load graph");
		}
        System.out.println("Graph successfully loaded.");
        
        System.out.println("Vertices = " + graph.getVertexCount());
        if (graph.getDefaultEdgeType().name() == "DIRECTED")
        	System.out.println("Arcs = " + graph.getEdgeCount());
        else if (graph.getDefaultEdgeType().name() == "UNDIRECTED")
        	System.out.println("Edges = " + graph.getEdgeCount());
        
        long startTime = System.currentTimeMillis();
        pds  = new ParallelDistanceStatistics<Integer, Integer>(graph);
        System.out.println("Diameter = " + pds.getDiameter());
        System.out.println("Radius = " + pds.getRadius());
        long elapsedTime = System.currentTimeMillis() - startTime;
        System.out.println("Elapsed time = " + elapsedTime / 1000.0 + " s");
    }

    private static Hypergraph<Integer, Integer> loadGraph(String path) throws IOException {
    	Hypergraph<Integer, Integer> graph = new PajekNetReader<>(createIntegerFactory(), createIntegerFactory()).load(path, new AdjacencyListGraph<Integer, Integer>());
        return graph;
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