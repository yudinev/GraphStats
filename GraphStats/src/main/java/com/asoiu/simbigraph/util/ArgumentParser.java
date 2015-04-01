package com.asoiu.simbigraph.util;

import org.apache.commons.cli.*;

/**
 * @author Stanislav Semochkin
 * @version 1.0 April 01, 2015
 */
public class ArgumentParser {

    private final Options options;

    private final Option graphPath;

    private final Option threadCount;

    private final HelpFormatter help;

    public ArgumentParser() {
        graphPath = OptionBuilder
            .withType(String.class)
            .isRequired(true)
            .hasArg(true)
            .withLongOpt("file")
            .withDescription("aaaaaa")
            .create("f")
        ;
        threadCount = OptionBuilder
            .withType(Integer.class)
            .isRequired(true)
            .hasArg(true)
            .withLongOpt("thread")
            .withDescription("aaaaaa")
            .create("t")
        ;
        options = new Options()
            .addOption(graphPath)
            .addOption(threadCount)
        ;
        help = new HelpFormatter(); //"Usage: java -jar GraphStats.jar <PathToGraphFile> <NumberOfThreads>"
    }

    public ProgramParameters parseCmdParameters(final String[] args) throws ParseException {
        ProgramParameters parameters = new ProgramParameters();
        try {
            final CommandLineParser parser = new PosixParser();
            CommandLine cmd = parser.parse(options, args);
            if (cmd.hasOption(graphPath.getOpt())) {
               parameters.setGraphFile(cmd.getOptionValue(graphPath.getOpt()));
            }
            if (cmd.hasOption(graphPath.getOpt())) {
                parameters.setThreadCount(Integer.parseInt(cmd.getOptionValue(threadCount.getOpt())));
            }
        } catch (ParseException | NumberFormatException e) {
            help.printHelp("GraphStats", options);
        }
        return parameters;
    }
}
