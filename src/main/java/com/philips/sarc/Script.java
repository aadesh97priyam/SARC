package com.philips.sarc;

import java.io.File;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.philips.sarc.Utils;

import org.json.simple.JSONObject;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

public class Script {
    private String command;
    private String callback;
    private boolean isValid = false;

    public String getCommand() {
        return command;
    }

    public String getCallback() {
        return callback;
    }

    public void setCommand(String command) {
        this.command = command; 
    }

    public void setCallback(String callback) {
        this.callback = callback;
    }

    public String toString() {
        return command;
    }

    public boolean compile() {
        if (command == null || callback == null) return false;
        try {
            final ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
            final Compilable compilable = (Compilable) engine;

            String[] splits = callback.split(":");
            if (splits == null || splits.length != 2) return false;

            File scriptjs = new File("resources/scripts/" + splits[0]);
            if (!scriptjs.exists() || !scriptjs.isFile())
                throw new ScriptException("[ERROR] Invalid callback script");
            final CompiledScript compiledJS = compilable.compile(Utils.getFileContents(scriptjs));
            compiledJS.eval();
        } catch (ScriptException se) {
            return false;
        }

        isValid = true;
        return true;
    }

    public Object invoke() {
        if (!isValid) return null;

        String arg = null;
        try {
            Process p = Runtime.getRuntime().exec(command);
            arg = Utils.getStreamContents(p.getInputStream());
        } catch (Exception exception) {
            return null;
        }

        Object output;
        try {
            final ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
            final Compilable compilable = (Compilable) engine;
            final Invocable invocable = (Invocable) engine;

            File scriptjs = new File("resources/scripts/" + callback.split(":")[0]);
            if (!scriptjs.exists() || !scriptjs.isFile())
                throw new ScriptException("[ERROR] Invalid callback script");
            final CompiledScript compiledJS = compilable.compile(Utils.getFileContents(scriptjs));
            compiledJS.eval();
            ScriptObjectMirror som = (ScriptObjectMirror) invocable.invokeFunction(callback.split(":")[1], arg);
            output = new JSONObject(som);
        } catch (ScriptException|NoSuchMethodException se) {
            output = null;
        }

        return output;
    }
}