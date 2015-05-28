package com.asoiu.simbigraph.algorithms;

import com.asoiu.simbigraph.exception.GraphStatsException;

/**
 * The interface specifies methods that each operation working with a graph
 * must implement.
 * 
 * @author Andrey Kurchanov
 */
public interface GraphStatsOperation {
	
	/**
	 * @author Andrey Kurchanov
	 */
	void execute() throws GraphStatsException;

}