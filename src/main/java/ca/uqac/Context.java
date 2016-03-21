package ca.uqac;

import ca.uqac.core.Option;
import ca.uqac.core.Rule;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class Context {
    private List<Option> options;
    private List<Rule> rules;
    private int optionValues;

    public Context() {
        this.options = new ArrayList<>();
        this.rules = new ArrayList<>();
        this.optionValues = 0;
    }

    public void add(final Option option) {
        this.options.add(option);
        this.optionValues += option.getNumberOfPossibleValues();
    }

    public void add(final Rule rule) {
        this.rules.add(rule);
    }

    public int getNumberOfParameters() {
        return this.options.size();
    }

    public int getNumberOfPairs() {
        int numberOfPairs = 0;
        final int numberOfParameters = this.options.size();
        for(int i=0; i < numberOfParameters-1; ++i) {
            for(int j=i+1; j < numberOfParameters; ++j) {
                numberOfPairs += this.options.get(i).getNumberOfPossibleValues() * this.options.get(j).getNumberOfPossibleValues();
            }
        }
        return numberOfPairs;
    }

    public void displayInfo() {
        System.out.println("\n============== INFO ==============");
        System.out.println("- There are " + this.options.size() + " parameters/options.");
        System.out.println("- There are " + this.optionValues + " parameter values.");
        System.out.println("- There are " + this.getNumberOfPairs() + " possible pairs (without taking into account rules).");
        System.out.println("- There are " + this.rules.size() + " rules.");
        System.out.println(this.toString());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n============== OPTIONS ==============\n");
        sb.append(StringUtils.join(this.options, "\n"));
        if(!this.rules.isEmpty()) {
            sb.append("\n\n============== RULES ==============\n");
            sb.append(StringUtils.join(this.rules, "\n"));
        }
        return sb.toString();
    }
}
