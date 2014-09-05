import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.commons.collections15.Factory;

import com.asoiu.simbigraph.algorithms.shortestpath.ParallelDistanceStatistics;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.io.PajekNetReader;

/**
 * @author Andrey Kurchanov
 */
public class Main {
	/**
	 * @param args
	 */
	public static void main(String[] args) throws URISyntaxException {
		Graph<Integer, Integer> graph = new SparseMultigraph<Integer, Integer>();
		File file = new File(args[0]); // Input graph file (Pajek format) should
										// be specified as first input argument
		Factory<Integer> vertexFactory = new Factory<Integer>() {
			int n = 0;

			@Override
			public Integer create() {
				return n++;
			}
		};
		Factory<Integer> edgeFactory = new Factory<Integer>() {
			int n = 0;

			@Override
			public Integer create() {
				return n++;
			}
		};
		PajekNetReader<Graph<Integer, Integer>, Integer, Integer> pnr = new PajekNetReader<Graph<Integer, Integer>, Integer, Integer>(
				vertexFactory, edgeFactory);
		long before = System.currentTimeMillis();
		try {
			System.out.println("Loading graph from the "
					+ file.getAbsolutePath() + " file");
			graph = pnr.load(file.getAbsolutePath(), graph);
			System.out.println("SUCCESS");
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		System.out.println("Vertices = " + graph.getVertexCount());
		System.out.println("Arcs = " + graph.getEdgeCount());
		ParallelDistanceStatistics<Integer, Integer> pds = new ParallelDistanceStatistics<>();
		System.out.println("Diameter = " + pds.diameter(graph));
		System.out.println("Radius = " + pds.radius(graph));
		long after = System.currentTimeMillis();
		long diff = after - before;
		System.out.println("Elapsed time = " + diff / 1000.0 + " s");
	}
}