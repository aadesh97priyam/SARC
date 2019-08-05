package com.philips.sarc;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.philips.sarc.CLI;
import com.philips.sarc.Configuration;
import com.philips.sarc.Reporter;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.text.StringSubstitutor;
import org.json.simple.JSONObject;

public class Processor implements CLI.Callable {

    File configFile;
    Configuration.Format format;

    private void identifyConfigFile(CommandLine cmdline) {
        if (!cmdline.hasOption("c"))  {
            configFile = new File("resources/dotfiles/.config");
            if (!configFile.exists() || !configFile.isFile()) {
                System.out.println("[WARN] Aborting as no .config file found in default location and neither is a path provided to one");
            }
        } else {
            String configFilePath = cmdline.getOptionValue("c");
            configFile = new File(configFilePath);
            if (!configFile.exists() || !configFile.isFile()) {
                System.out.println("[WARN] No file found at given location");
            }
        }
    }

    private void identifyConfigFormat(CommandLine cmdline) {
        if (cmdline.hasOption("f")) {
            try {
                format = Configuration.Format.valueOf(cmdline.getOptionValue("f").toUpperCase());
            } catch (Exception ex) {
                System.out.println("[WARN] Invalid format. Defaulting to JSON");
                format = Configuration.Format.JSON;
            }
        } else {
            format = Configuration.Format.JSON;
        }
    }

    private List<Script> getScripts(List<Script> tools) {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(new File("resources/dotfiles/.properties")));
        } catch (Exception e) {
            System.out.println("[WARN] No properties file found");
        }

        Map<String, String> valuesMap = new HashMap<>();
        properties.forEach((k,v)->valuesMap.put(k.toString(), v.toString()));
        StringSubstitutor sub = new StringSubstitutor(valuesMap);
        
        final ArrayList<Script> validTools = new ArrayList<>();
        for (Script tool : tools) {
            String command = tool.getCommand();
            tool.setCommand(sub.replace(command));

            if (tool.compile()) {
                validTools.add(tool);
            }
        }

        return validTools;
    }

    @Override
    public boolean onFlagEncountered(CommandLine cmdline) {
        identifyConfigFile(cmdline);
        identifyConfigFormat(cmdline);
        
        List<Script> tools = Configuration.parse(configFile, format, Script.class);

        List<Script> validTools = getScripts(tools);

        ArrayList<JSONObject> results = new ArrayList<>();
        for (Script script : validTools) {
            Object result = script.invoke();
            if (result != null) {
                JSONObject temp = (JSONObject) result;
                results.add(temp);
            }
        }

        Reporter.ReportFormat reportFormat = Reporter.ReportFormat.STDOUT;
        Reporter reporter = new Reporter();
        if (cmdline.hasOption("o")) {
            reportFormat = reporter.lookup(cmdline.getOptionValue("o"));
        }
        reporter.setReports(results);
        reporter.generateMergedReport(reportFormat);

        return false;
	}
    
}