package com.asoiu.simbigraph.util;

/**
 * @author Stanislav Semochkin
 * @version 1.0 April 01, 2015
 */
public class ProgramParameters {

    private String graphFile;

    private int threadCount;

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
