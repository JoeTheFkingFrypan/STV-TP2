package ca.uqac;

import ca.uqac.io.ScriptParser;
import ca.uqac.io.ScriptParserException;

public class Main {

    public static void main(String[] args) {

        Context context;
        int poolSize;

        switch (args.length) {
            case 0:
                System.out.println("[INFO] No argument detected, using simulated output with a pool size initialized at 20.");
                context = ScriptParser.runScriptSimulated(2);
                poolSize = 20;

            case 1:
                context = ScriptParser.runScript(args[0]);
                poolSize = 20;
                System.out.println("[INFO] Pool size has been initialized with its default value set at 20.");
                System.out.println("[INFO] This can be customized using an additional argument");
                break;

            case 2:
                context = ScriptParser.runScript(args[0]);
                poolSize = Integer.parseInt(args[1]);
                break;

            default:
                System.err.println("[ERROR] The application requires one parameter (path to the log file to be parsed)");
                System.err.println("[ERROR] An optional parameter can be used to customize the pool size for candidate selection");
                System.err.println("[ERROR] Usage : 'program <scriptPath> <poolSize>'");
                throw new ScriptParserException("[ERROR] Usage exception: invalid number of parameters", new IllegalArgumentException());
        }

        TestCaseGenerator generator = new TestCaseGenerator(context, poolSize);
        generator.printSuggestedTestCases();
    }
}