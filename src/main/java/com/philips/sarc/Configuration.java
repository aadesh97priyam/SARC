package com.philips.sarc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Configuration<E> {

    private Configuration () {

    }

    public static <E> List<E> parse(File configFile, Format fileFormat, Class<E> classtype) {
        if (configFile == null || fileFormat == null || classtype == null) return null;
        ArrayList<E> configNodes = new ArrayList<>();
        
        switch (fileFormat) {
            case JSON: {
                JSONArray configurationNodes;

                try {
                    String contents = Utils.getFileContents(configFile);
                    if (contents == null) throw new RuntimeException("Couldn't retrieve file contents");
                    configurationNodes = (JSONArray) new JSONParser().parse(contents);
                } catch(Exception ex) {
                    System.out.println("[ERROR] " + ex.getMessage());
                    configurationNodes = new JSONArray();
                }
                
                ObjectMapper mapper = new ObjectMapper();
                for (int index = 0; index < configurationNodes.size(); index++) {
                    JSONObject cNode = (JSONObject) configurationNodes.get(index);
                    try {
                        E object = mapper.readValue(cNode.toJSONString(), classtype);
                        configNodes.add(object);
                    } catch (Exception ex) {
                        System.out.println("[WARN] Skipping one configuration node due to type incompatibility");
                        continue;
                    }
                }

                break;
            }
        }

        return configNodes;
    }

    public enum Format {
        JSON;
    }
}