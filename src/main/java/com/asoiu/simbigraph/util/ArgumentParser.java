package com.asoiu.simbigraph.util;

import org.apache.commons.cli.*;

/**
 * @author Andrey Kurchanov
 */
public class ArgumentParser {

	private final Option graphPath;
    
    private final Option operations;

    private final Option threadCount;
    
    private final Options options;    

    private final HelpFormatter help;

    public ArgumentParser() {
    	graphPath = OptionBuilder
    		.withType(String.class)
    		.isRequired(true)
    		.hasArg(true)
    		.withLongOpt("file")
    		.withDescription("This parameter sets path to the graph file.")
    		.create("f")
    	;
    	operations = OptionBuilder
    		.withType(String.class)
    		.isRequired(true)
    		.hasArg(true)
    		.withLongOpt("operations")
    		.withDescription("List of available operations:\ndr - get diameter and radius of the graph\nseparated by comma.")
    		.create("op")
    	;
    	threadCount = OptionBuilder
    		.withType(Integer.class)
    		.isRequired(true)
    		.hasArg(true)
    		.withLongOpt("thread")
    		.withDescription("This parameter sets count of parallel threads.")
    		.create("t")
    	;
    	options = new Options()
    		.addOption(graphPath)
    		.addOption(operations)
    		.addOption(threadCount)
    	;
    	help = new HelpFormatter();
    }

    public ProgramParameters parseCmdParameters(final String[] args) throws ParseException {
    	ProgramParameters parameters = new ProgramParameters();
        try {
            final CommandLineParser parser = new PosixParser();
            CommandLine cmd = parser.parse(options, args);
            if (cmd.hasOption(graphPath.getOpt())) {
               parameters.setGraphFile(cmd.getOptionValue(graphPath.getOpt()));
            }
            if (cmd.hasOption(operations.getOpt())) {
            	for (String string : cmd.getOptionValue(operations.getOpt()).split(",")) {
					switch (string) {
					case "dr":
						parameters.setIsDiameterRadiusRequested();
						break;
					default:
						break;
					}
				}
            }
            if (cmd.hasOption(graphPath.getOpt())) {
                parameters.setThreadCount(Integer.parseInt(cmd.getOptionValue(threadCount.getOpt())));
            }
        } catch (ParseException | NumberFormatException e) {
            help.printHelp("GraphStats", options);
            System.exit(1);
        }
        return parameters;
    }
    
}