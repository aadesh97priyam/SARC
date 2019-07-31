package com.philips.sarc;

import java.io.File;
import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.junit.Test;

public class CLITest {

    // Test cases for th epublic CLI constructor
    @Test(expected = RuntimeException.class)
    public void constructorNullFile() {
        CLI cli = new CLI(null);
    }

    @Test(expected = RuntimeException.class)
    public void nonExistantFile() {
        File f = new File("");
        CLI cli = new CLI(f);
    }

    @Test(expected = IOException.class)
    public void properFileButEmptyContents() throws IOException {
        File f = new File("hi.txt");
        f.createNewFile();
        try {
            CLI cli = new CLI(f);
        } catch (Exception e) {
            f.delete();
            if (e.getMessage().equals("ERROR: Illegal file contents!"));
                throw new IOException();
        }
    }

    @Test(expected = IOException.class)
    public void properFileRandomContent() throws IOException {
        File f = new File("hi.txt");
        f.createNewFile();
        Utils.writeFileContents(f, "blah blah blah");
        try {
            CLI cli = new CLI(f);
        } catch (Exception e) {
            f.delete();
            if (e.getMessage().equals("ERROR: Illegal file contents!"));
                throw new IOException();
        }
    }

    @Test
    public void properInput() throws IOException {
        File f = new File("hi.txt");
        f.createNewFile();
        Utils.writeFileContents(f, "a,accept,false,description");
        CLI cli = new CLI(f);
        f.delete();
    }

    // Test cases for parse
    @Test
    public void nullArgs() throws Exception {
        File f = new File("hi.txt");
        f.createNewFile();
        Utils.writeFileContents(f, "a,accept,false,description");
        CLI cli = new CLI(f);
        f.delete();

        cli.parse(null);
    }

    @Test
    public void emptyArgs() throws Exception {
        File f = new File("hi.txt");
        f.createNewFile();
        Utils.writeFileContents(f, "a,accept,false,description");
        CLI cli = new CLI(f);
        f.delete();

        cli.parse(new String[] {""});
    }

    @Test
    public void improperArgs() throws Exception {
        File f = new File("hi.txt");
        f.createNewFile();
        Utils.writeFileContents(f, "a,accept,false,description");
        CLI cli = new CLI(f);
        f.delete();

        cli.parse(new String[] {"-vjhv knkn- ijbk"});
    }

    @Test
    public void improperArgsWithCallback() throws Exception {
        File f = new File("hi.txt");
        f.createNewFile();
        Utils.writeFileContents(f, "a,accept,false,description");
        CLI cli = new CLI(f);
        f.delete();

        cli.registerCallback("h", new CLI.Callable(){
        
            @Override
            public boolean onFlagEncountered(CommandLine cmdline) {
                throw new RuntimeException();
            }
        });

        cli.parse(new String[] {"-a hello"});
    }

    @Test
    public void improperArgsWithCallback2() throws Exception {
        File f = new File("hi.txt");
        f.createNewFile();
        Utils.writeFileContents(f, "a,accept,false,description");
        CLI cli = new CLI(f);
        f.delete();

        cli.registerCallback("a", new CLI.Callable(){
        
            @Override
            public boolean onFlagEncountered(CommandLine cmdline) {
                throw new RuntimeException();
            }
        });

        cli.parse(new String[] {"-a hello"});
    }

    @Test(expected = RuntimeException.class)
    public void properArgsWithCallback() throws Exception {
        File f = new File("hi.txt");
        f.createNewFile();
        Utils.writeFileContents(f, "a,accept,false,description");
        CLI cli = new CLI(f);
        f.delete();

        cli.registerCallback("a", new CLI.Callable(){
        
            @Override
            public boolean onFlagEncountered(CommandLine cmdline) {
                throw new RuntimeException();
            }
        });

        cli.parse(new String[] {"-a"});
    }

    @Test
    public void improperArgsWithCallback3() throws Exception {
        File f = new File("hi.txt");
        f.createNewFile();
        Utils.writeFileContents(f, "a,accept,false,description");
        CLI cli = new CLI(f);
        f.delete();

        cli.registerCallback("a", new CLI.Callable(){
        
            @Override
            public boolean onFlagEncountered(CommandLine cmdline) {
                throw new RuntimeException();
            }
        });

        cli.parse(new String[] {"-j kiol -a"});
    }

    public void properArgsWithCallback2() throws Exception {
        File f = new File("hi.txt");
        f.createNewFile();
        Utils.writeFileContents(f, "a,accept,true,description");
        CLI cli = new CLI(f);
        f.delete();

        cli.registerCallback("a", new CLI.Callable(){
        
            @Override
            public boolean onFlagEncountered(CommandLine cmdline) {
                if (!cmdline.getOptionValue("a").equals("hi"))
                    throw new RuntimeException();
                return false;
            }
        });

        cli.parse(new String[] {"-a hi"});
    }

    @Test(expected = RuntimeException.class)
    public void properArgsWithMandatoryCallback() throws Exception {
        File f = new File("hi.txt");
        f.createNewFile();
        Utils.writeFileContents(f, "a,accept,false,description");
        CLI cli = new CLI(f);
        f.delete();

        cli.registerMandatoryCallback("a", new CLI.Callable(){
        
            @Override
            public boolean onFlagEncountered(CommandLine cmdline) {
                throw new RuntimeException();
            }
        });

        cli.parse(null);
    }
}