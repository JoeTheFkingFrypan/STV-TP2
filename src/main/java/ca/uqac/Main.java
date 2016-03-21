package ca.uqac;

import ca.uqac.validation.TestCaseGenerator;
import io.ScriptParser;

public class Main {

    public static void main(String[] args) {
        Context context = ScriptParser.runScriptSimulated(2);
        TestCaseGenerator generator = new TestCaseGenerator(context);
        generator.printSuggestedTestCases();
    }
}