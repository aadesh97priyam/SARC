package com.philips.sarc;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.ArrayList;

import com.philips.sarc.Reporter.ReportFormat;

import org.json.simple.JSONObject;
import org.junit.Test;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

public class ReporterTest {
    // Test cases generateMergedReport
    @Test
    public void setNullreportAndMerge() {
        new File("report.txt").delete();
        Reporter r = new Reporter();
        r.generateMergedReport(Reporter.ReportFormat.PLAIN_TEXT);
        File report = new File("report.txt");
        assertEquals(false, report.exists());
        new File("report.txt").delete();
    }

    @Test
    public void nullArgToMergerReport() {
        Reporter r = new Reporter();
        r.setReports(new ArrayList<JSONObject>());
        r.generateMergedReport(null);
    }

    @Test 
    public void setEmptyReportAndMerge() {
        new File("report.txt").delete();
        ArrayList<JSONObject> reports = new ArrayList<>();
        Reporter r = new Reporter();
        r.setReports(reports);
        r.generateMergedReport(Reporter.ReportFormat.PLAIN_TEXT);
        assertEquals(false, new File("report.txt").exists());
        new File("report.txt").delete();
    }
    
    // Test cases for getReports
    @Test
    public void getReportsOnNewReporterObject() {
        Reporter r = new Reporter();
        assertEquals(null, r.getReports());
    }

    @Test
    public void getReportsAfterSettingReports() {
        Reporter r = new Reporter();
        r.setReports(null);
        assertEquals(null, r.getReports());
        ArrayList<JSONObject> al = new ArrayList<>();
        r.setReports(al);
        assertEquals(al, r.getReports());
    }

    // Test cases for lookup
    @Test
    public void nullLookp() {
        Reporter reporter = new Reporter();
        ReportFormat rf =  reporter.lookup(null);
        assertEquals(ReportFormat.STDOUT, rf);
    }

    @Test
    public void emptyStringLookup() {
        Reporter reporter = new Reporter();
        ReportFormat rf =  reporter.lookup("");
        assertEquals(ReportFormat.STDOUT, rf);
    }

    @Test
    public void randomStringLookup() {
        Reporter reporter = new Reporter();
        ReportFormat rf =  reporter.lookup("kajbdc##@3&");
        assertEquals(ReportFormat.STDOUT, rf);
    }

    @Test
    public void stdoutStringLookup() {
        Reporter reporter = new Reporter();
        ReportFormat rf =  reporter.lookup("stdOut");
        assertEquals(ReportFormat.STDOUT, rf);
    }

    @Test
    public void plainTextStringLookup() {
        Reporter reporter = new Reporter();
        ReportFormat rf =  reporter.lookup("plain_teXt");
        assertEquals(ReportFormat.PLAIN_TEXT, rf);
    }

    @Test
    public void htmlStringLookup() {
        Reporter reporter = new Reporter();
        ReportFormat rf =  reporter.lookup("HtmL");
        assertEquals(ReportFormat.HTML, rf);
    }
}