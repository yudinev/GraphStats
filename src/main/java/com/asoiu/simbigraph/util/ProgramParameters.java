package com.asoiu.simbigraph.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Andrey Kurchanov
 */
public class ProgramParameters {

    private Map<String, Boolean> operations;
    
    private String graphFile;

    private int threadCount;
    
    public ProgramParameters() {
    	operations = new HashMap<String, Boolean>();
    	operations.put("isDiameterRadiusRequested", false);
	}
   
    public boolean getIsDiameterRadiusRequestedFlag() {
    	return (boolean) operations.get("isDiameterRadiusRequested");
    }
    
    public void setIsDiameterRadiusRequested() {
    	operations.put("isDiameterRadiusRequested", true);
    }
	
	public String getGraphFile() {
        return graphFile;
    }

    public void setGraphFile(String graphFile) {
        this.graphFile = graphFile;
    }

    public int getThreadCount() {
        return threadCount;
    }

    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }
    
}