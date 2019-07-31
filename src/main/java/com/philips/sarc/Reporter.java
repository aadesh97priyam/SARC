package com.philips.sarc;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.text.StringSubstitutor;
import org.json.simple.JSONObject;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

@SuppressWarnings("unchecked")
public class Reporter {
    private List<JSONObject> reports;
    private JSONObject merged;

    public void setReports(List<JSONObject> reports) {
        this.reports = reports;
    }

    public List<JSONObject> getReports() {
        return reports;
    }

    public void generateMergedReport(ReportFormat rformat) {
        if (reports == null || reports.size() == 0) {
            System.out.println("[WARN] Skipping report generation as no data provided");
            return;
        }

        switch (rformat) {
            case STDOUT:
            generateConsoleReport();
            break;

            case PLAIN_TEXT:
            generatePlainTextReport();
            break;
            
            case HTML:
            generateHtmlReport();
            break;

            default:
            System.out.println("[WARN] Invalid format option. Defaulting to STDOUT");
            generateConsoleReport();
        }
    }

    private void merge() {
        merged = new JSONObject();
        Set<String> keySet = new HashSet<>();
        for (JSONObject report : reports) {
            keySet.addAll(report.keySet());
        }
        keySet.remove("tool");

        for (String key : keySet) {
            JSONObject collection = new JSONObject();
            for (JSONObject report : reports) {
                if (report.containsKey(key)) {
                    collection.put(report.get("tool"), report.get(key));
                }
            }
            merged.put(key, collection);
        }
    }

    private void generateConsoleReport() {
        merge();
        Set<String> files = merged.keySet();
        for (String file : files) {
            System.out.println("File: " + file);
            JSONObject fileData = (JSONObject) merged.get(file);
            Set<String> tools = fileData.keySet();
            for (String tool : tools) {
                JSONObject toolData = new JSONObject((ScriptObjectMirror) fileData.get(tool));
                System.out.println(tool + " Report");
                Set<String> errors = toolData.keySet();

                for (String error : errors) {
                    JSONObject errorData = new JSONObject((ScriptObjectMirror)toolData.get(error));
                    System.out.println("Line " + errorData.get("line") + " : " + errorData.get("comment").toString().trim());
                }
                System.out.println();
            }
        }
    }

    private void generatePlainTextReport() {
        merge();
        StringBuilder contents = new StringBuilder();

        Set<String> files = merged.keySet();
        for (String file : files) {
            contents.append("File: " + file + "\n");
            JSONObject fileData = (JSONObject) merged.get(file);
            Set<String> tools = fileData.keySet();
            for (String tool : tools) {
                JSONObject toolData = new JSONObject((ScriptObjectMirror)fileData.get(tool));
                contents.append(tool + " Report" + "\n");
                Set<String> errors = toolData.keySet();

                for (String error : errors) {
                    JSONObject errorData = new JSONObject((ScriptObjectMirror)toolData.get(error));
                    contents.append("Line " + errorData.get("line") + " : " + errorData.get("comment").toString().trim() + "\n");
                }
                contents.append("\n");
            }
        }
        File reportFile = new File("report.txt");
        Utils.writeFileContents(reportFile, contents.toString());
        System.out.println("[INFO] Text report generated in file: report.txt");
    }

    private void generateHtmlReport() {
        merge();
        File htmlReportDirectory = new File("htmlreport");
        if (htmlReportDirectory.exists()) {
            try {
            Utils.deleteDirectoryRecursion(htmlReportDirectory.toPath());
            } catch (IOException ioe) {
                System.out.println("[ERROR] Couldn't delete html report directory");
                System.out.println("[INFO]  Skipping report creation");
                return;
            }
        }
        if (htmlReportDirectory.mkdir()) {
            generateIndexFile();
            generateReportFiles();
            System.out.println("[INFO] Html report generated in folder: htmlreport");
        } else {
            System.out.println("[ERROR] Unable to create directory");
            System.out.println("[INFO]  Skipping report creation");
            return;
        }
    }

    private void generateIndexFile() {
        StringSubstitutor substitutor;
        StringBuilder listContents = new StringBuilder();
        String listHtml = "<li><a href='${filename}.html'>${filename}</a></li>";
        Set<String> files = merged.keySet();
        HashMap<String, String> replacementValues;
        for (String file : files) {
            replacementValues = new HashMap<>();
            String fName = file.substring(file.lastIndexOf("\\")+1);
            replacementValues.put("filename", fName);
            substitutor = new StringSubstitutor(replacementValues);
            String listHtmlClone = listHtml.substring(0);
            listContents.append(substitutor.replace(listHtmlClone));
        }

        File indexFileTemplate = new File("resources/templates/indextemplate.txt");
        String indexFileContents = Utils.getFileContents(indexFileTemplate);
        replacementValues = new HashMap<>();
        replacementValues.put("list", listContents.toString());
        substitutor = new StringSubstitutor(replacementValues);
        Utils.writeFileContents(new File("htmlreport/index.html"), substitutor.replace(indexFileContents));
    }

    private void generateReportFiles() {

        StringBuilder report = new StringBuilder();
        Set<String> files = merged.keySet();
        for (String file : files) {
            report.delete(0, report.length());
            String filename = file.substring(file.lastIndexOf("\\")+1);

            JSONObject fileData = (JSONObject) merged.get(file);
            Set<String> tools = fileData.keySet();
            for (String tool : tools) {
                JSONObject toolData = new JSONObject((ScriptObjectMirror)fileData.get(tool));
                report.append("<h4>" + tool + " Report" + "</h4>");
                Set<String> errors = toolData.keySet();
                
                report.append("<p>");
                for (String error : errors) {
                    JSONObject errorData = new JSONObject((ScriptObjectMirror)toolData.get(error));
                    report.append("Line " + errorData.get("line") + " : " + errorData.get("comment").toString().trim() + "<br>");
                }
                report.append("</p><hr>");
            }

            HashMap<String, String> map = new HashMap<>();
            map.put("report", report.toString());
            map.put("file", filename);
            StringSubstitutor substitutor = new StringSubstitutor(map);
            Utils.writeFileContents(new File("htmlreport/" + filename + ".html"), substitutor.replace(Utils.getFileContents(new File("resources/templates/reporttemplate.txt"))));
        }
    }

    public ReportFormat lookup(String value) {

        if (value == null) return ReportFormat.STDOUT;

        switch (value.toUpperCase()) {
            case "STDOUT": return ReportFormat.STDOUT;
            case "PLAIN_TEXT": return ReportFormat.PLAIN_TEXT;
            case "HTML": return ReportFormat.HTML;
        }

        return ReportFormat.STDOUT;
    }
 
    public static enum ReportFormat {
        STDOUT, PLAIN_TEXT, HTML;
    }

}