package com.philips.sarc;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;

public class CLI 
{
    private Options options;
    private Map<String, Callable> callbackMap;
    private Set<String> mandatoryList;

    public CLI (File optionFile) {
        String fileContents = Utils.getFileContents(optionFile);
        if (fileContents != null && isContentLegal(fileContents)) {
            this.options = constructOptions(fileContents);
        } else {
            throw new RuntimeException("ERROR: Illegal file contents!");
        }

        callbackMap = new HashMap<>();
        mandatoryList = new HashSet<>();
    }

    private Options constructOptions(String content) {

        Options options = new Options();

        String[] lines = content.split("\n");
        for (int i=0; i < lines.length; i++) {
            String[] columns = lines[i].split(",");
            options.addOption(columns[0], columns[1], Boolean.parseBoolean(columns[2]), columns[3]);    
        }

        return options;
    }

    private boolean isContentLegal(String content) {
        boolean flag = true;
        String[] allLines = content.split("\n");
        for (String line : allLines) {
            String[] cols = line.split(",");
            if (cols.length < 4) {
                flag = false;
                break;
            }
        }
        return flag;
    }

    public void parse(String[] args) throws ParseException {
        CommandLineParser commandLineParser = new DefaultParser();
        CommandLine commandLine = null;
        try{
            commandLine = commandLineParser.parse(options, args);
        } catch (UnrecognizedOptionException uoe) { return; }

        for (String key : callbackMap.keySet()) {
            if (commandLine != null && commandLine.hasOption(key) || mandatoryList.contains(key)) {
                if (callbackMap.get(key).onFlagEncountered(commandLine)) break;
            }
        }
    }

    public void registerCallback(String option, Callable callback) {
        if (option != null && callback != null) {
            callbackMap.put(option, callback);
        }
    }

    public void registerMandatoryCallback(String option, Callable callback) {
        if (option != null && callback != null) {
            callbackMap.put(option, callback);
            mandatoryList.add(option);
        }
    }

    public void printHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("SARC", options);
    }

    public interface Callable {
        boolean onFlagEncountered(CommandLine cmdline);
    }
}