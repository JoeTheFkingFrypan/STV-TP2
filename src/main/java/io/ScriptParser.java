package io;

import ca.uqac.Context;
import ca.uqac.core.*;
import com.google.common.base.Splitter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ScriptParser {

    public static Context runScript(final String filepath) throws ScriptParserException {
        try {
            // Run script file
            System.out.println("[STATUS] About to run script: " + filepath);
            String[] command = { filepath, "-h"};
            Process process = Runtime.getRuntime().exec(command);

            // Setup variables to read output
            Context context = new Context();
            String output;

            // Parse output one line at a time, adding the result to context object
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while(null != (output = reader.readLine())) {
                parseLine(output, context);
            }

            // Return context object after output has been parsed
            return context;
        } catch (IOException e) {
            e.printStackTrace();
            throw new ScriptParserException(e);
        }
    }

    public static Context runScriptSimulated(final int testCase) {
        Context context = new Context();
        for (String line: simulateOutput(testCase)) {
            parseLine(line, context);
        }
        return context;
    }

    private static void parseLine(final String output, Context context) {
        if(isOption(output)) {
            Option option = parseOption(output);
            context.add(option);
        } else if(isRule(output)) {
            Rule rule = parseRule(output);
            context.add(rule);
        }
    }

    private static boolean isOption(final String output) {
        return output.startsWith(" -") && output.contains("   ");
    }

    private static Option parseOption(String output) {
        Option option = new Option(output.substring(2,3));
        Splitter splitter = Splitter.on(',').omitEmptyStrings().trimResults();
        for(String token: splitter.split(output.substring(6))){
            option.addPossibleValue(token);
        }
        //System.out.println(option);
        return option;
    }

    private static boolean isRule(final String output) {
        return output.contains("=") && output.contains("&");
    }

    private static Rule parseRule(String output) {
        Rule rule = new Rule();
        Splitter splitter = Splitter.on('&').omitEmptyStrings().trimResults();
        for(String token: splitter.split(output)){
            String key = token.substring(0,1);
            if(token.contains("=")) {
                rule.addConstraint(new ConstraintValue(key, token.substring(4)));
            } else {
                rule.addConstraint(new ConstraintPresence(key));
            }
        }
        //System.out.println(rule);
        return rule;
    }

    private static List<String> simulateOutput(int testCase) {
        List<String> list = new ArrayList<>();
        switch (testCase) {
            case 1:
            default:
                list.add("usage: tp2-app.sh [options] argument");
                list.add(" -0   0, 1");
                list.add(" -1   2, 3, 4, 5");
                list.add(" -2   6, 7, 8");
                list.add(" -3   9, 10");
                break;

            case 2:
                list.add("usage: tp2-app.sh [options] argument");
                list.add(" -a   1, 2, 3, 4");
                list.add(" -f   on, off, unknown");
                list.add(" -h   flag");
                list.add(" -j   -1, 1");
                list.add(" -p   flag");
                list.add(" Invalid combinations:");
                list.add("");
                list.add(" a = 1 & j = -1");
                list.add(" f = unknown & p");
                list.add(" h & a = 1 & f = on");
                break;
        }
        return list;
    }
}
