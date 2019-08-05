package com.philips.sarc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.io.File;
import java.util.List;

import org.junit.Test;

public class ConfigurationTest {

    @Test
    public void allNullArgs() {
        assertEquals(null, Configuration.parse(null, null, null));
    }
    
    @Test 
    public void fileNullArg() {
        assertEquals(null, Configuration.parse(null, Configuration.Format.JSON, Configuration.class));
    }
    
    @Test 
    public void formatNullArg() {
        assertEquals(null, Configuration.parse(new File("resources/templates/indextemplate.txt"), null, ConfigurationTest.class));
    }
    
    @Test 
    public void classNullArg() {
        assertEquals(null, Configuration.parse(new File("resources/templates/indextemplate.txt"), Configuration.Format.JSON, null));
    }

    @Test
    public void fileDoesntExist() {
        List<Script> config = Configuration.parse(new File("hi"), Configuration.Format.JSON, Script.class);
        assertNotEquals(null, config);
        assertEquals(0, config.size());
    }

    @Test
    public void improperMapping1() {
        File f = new File("hi.txt");
        Utils.writeFileContents(f, "[{\"hi\" : \"hi\"}]");
        List<Script> config = Configuration.parse(f, Configuration.Format.JSON, Script.class);
        f.delete();
        assertNotEquals(null, config);
        assertEquals(0, config.size());
    }

    @Test
    public void properMapping2() {
        File f = new File("hi.txt");
        Utils.writeFileContents(f, "[{\"command\" : \"java\", \"callback\" : \"callback\"}]");
        List<Script> config = Configuration.parse(f, Configuration.Format.JSON, Script.class);
        f.delete();
        assertNotEquals(null, config);
        assertEquals(1, config.size());
        assertEquals("java", config.get(0).getCommand());
        assertEquals("callback", config.get(0).getCallback());
    }

    @Test
    public void improperMapping3() {
        File f = new File("hi.txt");
        Utils.writeFileContents(f, "[{\"command\" : \"java\", \"callback\" : \"callback\"}]");
        List<Configuration> config = Configuration.parse(f, Configuration.Format.JSON, Configuration.class);
        f.delete();
        assertNotEquals(null, config);
        assertEquals(0, config.size());
    }

    @Test
    public void nodesCheckup() {
        File f = new File("hi.txt");
        Utils.writeFileContents(f, "[{\"command\" : \"java\", \"callback\" : \"callback\"}]");
        List<Configuration> config = Configuration.parse(f, Configuration.Format.JSON, Configuration.class);
        f.delete();
        assertNotEquals(null, config);
        assertEquals(0, config.size());
    }

    @Test
    public void nodesCheckupOnValidArgs() {
        File f = new File("hi.txt");
        Utils.writeFileContents(f, "[{\"command\" : \"java\", \"callback\" : \"callback\"}, {\"command\" : \"java\", \"callback\" : \"callback\"}, {\"command\" : \"java\", \"callback\" : \"callback\"}]");
        List<Script> config = Configuration.parse(f, Configuration.Format.JSON, Script.class);
        f.delete();
        assertNotEquals(null, config);
        assertEquals(3, config.size());
    }
}