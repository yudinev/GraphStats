import com.asoiu.simbigraph.algorithms.shortestpath.ParallelDistanceStatistics;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Hypergraph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.io.PajekNetReader;
import org.apache.commons.collections15.Factory;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author Andrey Kurchanov
 */
public class Main {
    /**
     * @param args
     */
    public static void main(String[] args) throws URISyntaxException {
        String  path;
        Hypergraph<Integer, Integer> graph;
        ParallelDistanceStatistics<Integer, Integer> pds;

        // Input graph file (Pajek format) should be specified as first input argument
        if (args.length != 1) {
            throw new IllegalArgumentException("Can't find graph file parameter");
        } else {
            path = args[0];
        }

        long start = System.currentTimeMillis();

        graph = loadGraph(path);
        if (graph == null) {
            return;
        }

        System.out.println("Vertices = " + graph.getVertexCount());
        System.out.println("Arcs = " + graph.getEdgeCount());
        pds  = new ParallelDistanceStatistics<Integer, Integer>(graph);
        System.out.println("Diameter = " + pds.getDiameter());
        System.out.println("Radius = " + pds.getRadius());
        long elapsedTime = System.currentTimeMillis() - start;
        System.out.println("Elapsed time = " + elapsedTime / 1000.0 + " s");
    }

    private static Hypergraph<Integer, Integer> loadGraph(String path) {
        Hypergraph<Integer, Integer> graph = null;
        PajekNetReader<Graph<Integer, Integer>, Integer, Integer> pnr = new PajekNetReader<>(
            createFactory(), createFactory()
        );

        System.out.println("Loading graph from the " + path + " file");
        try {
            graph = pnr.load(path, new SparseMultigraph<Integer, Integer>());
        } catch (IOException e) {
            System.out.println("Failed to load graph");
            e.printStackTrace();
        }
        System.out.println("Graph successfully loaded.");
        return graph;
    }

    private static Factory<Integer> createFactory() {
        return new Factory<Integer>() {
            private int n = 0;

            @Override
            public Integer create() {
                return n++;
            }
        };

    }
}