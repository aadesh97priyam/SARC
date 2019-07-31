package com.philips.sarc;

import java.io.File;

import com.philips.sarc.CLI;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;

public class Application {
    public static void main(String[] args) {

        final CLI cli = new CLI(new File("resources/dotfiles/.options"));

        for (String arg : args) {
            if (arg.equals("-h") || arg.equals("--help")) {
                cli.printHelp();
                return;
            }
        }

        cli.registerCallback("h", new CLI.Callable() {
        
            @Override
            public boolean onFlagEncountered(CommandLine cmdline) {
                cli.printHelp();
                return false;
            }
        });

        cli.registerMandatoryCallback("s", new Processor());

        try {
            cli.parse(args);
        } catch (ParseException pe) {
            System.out.println(pe);
        }
    }
}