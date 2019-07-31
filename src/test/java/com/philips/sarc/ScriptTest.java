package com.philips.sarc;

import static org.junit.Assert.assertEquals;

import java.io.File;

import com.philips.sarc.Script;

import org.junit.Test;

public class ScriptTest {

    @Test
    public void compilationForNullOrPartialArgs() {
        Script script = new Script();
        assertEquals(false, script.compile());
        
        script = new Script();
        script.setCommand("command");
        assertEquals(false, script.compile());

        script = new Script();
        script.setCallback("callback");
        assertEquals(false, script.compile());
    }

    @Test
    public void compilationForImproperArgs() {
        Script script = new Script();
        script.setCallback("callback");
        script.setCommand("command");

        assertEquals(false, script.compile());
    }

    @Test
    public void callbackPointsToNonExistantFile() {
        Script script = new Script();
        script.setCommand("java --version");
        script.setCallback("something.js:main");
        assertEquals(false, script.compile());
    }

    @Test
    public void callbackPointsToProperFileButErrorInFile() throws Exception {
        Utils.writeFileContents(new File("resources/scripts/something.js"), "function({ return 'Hi'}");
        Script script = new Script();
        script.setCommand("java --version");
        script.setCallback("something.js:lol");
        Utils.deleteDirectoryRecursion(new File("resources/scripts/something.js").toPath());
        assertEquals(false, script.compile());
    }

    @Test
    public void invokeOnInvalid() {
        Script script = new Script();
        script.setCallback("callback");
        script.setCommand("command");
        script.compile();
        assertEquals(null, script.invoke());
    }

    @Test
    public void invokeOnNonExistingFuntion() throws Exception {
        Utils.writeFileContents(new File("resources/scripts/something.js"), "function lol(){ return 'Hi'}");
        Script script = new Script();
        script.setCommand("java --version");
        script.setCallback("something.js:pom");
        Utils.deleteDirectoryRecursion(new File("resources/scripts/something.js").toPath());
        assertEquals(false, script.compile());
    }

    @Test
    public void invokeOnExistingFuntion() throws Exception {
        Utils.writeFileContents(new File("resources/scripts/something.js"), "function lol(arg){ return {'arg' : arg};}");
        Script script = new Script();
        script.setCommand("java --version");
        script.setCallback("something.js:lol");
        Utils.deleteDirectoryRecursion(new File("resources/scripts/something.js").toPath());
        
        assertEquals(false, script.compile());
    }
}