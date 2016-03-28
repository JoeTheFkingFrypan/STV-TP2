package ca.uqac;

import ca.uqac.core.Option;
import ca.uqac.core.Rule;
import ca.uqac.validation.Pair;
import ca.uqac.validation.ParameterValue;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class Context {
    private List<Pair> availablePairs;
    private List<Option> options;
    private List<Rule> rules;
    private int optionValues;
    private List<String> parameterOrdering;

    public Context() {
        this.availablePairs = new ArrayList<>();
        this.options = new ArrayList<>();
        this.rules = new ArrayList<>();
        this.optionValues = 0;
    }

    public void add(final Option option) {
        this.options.add(option);
        this.optionValues += option.getNumberOfPossibleValues();
    }

    public List<Option> getOptions(){ return options; }

    public void add(final Rule rule) {
        this.rules.add(rule);
    }

    public List<Pair> getPairs() {
        if(!this.availablePairs.isEmpty()) {
            return this.availablePairs;
        }

        System.out.println("- Pair generation has begun");
        final int numberOfParameters = this.options.size();
        for(int i=0; i < numberOfParameters-1; ++i) {
            for(int j=i+1; j < numberOfParameters; ++j) {

                Option lhs = this.options.get(i);
                Option rhs = this.options.get(j);

                for(int x=0; x < lhs.getNumberOfPossibleValues(); x++) {
                    for(int y=0; y < rhs.getNumberOfPossibleValues(); y++) {

                        final ParameterValue currentLhs = new ParameterValue(lhs.getKey(), lhs.getValueAt(x));
                        final ParameterValue currentRhs = new ParameterValue(rhs.getKey(), rhs.getValueAt(y));

                        if(pairNotForbidden(currentLhs, currentRhs)) {
                            this.availablePairs.add(new Pair(lhs.getKey(), lhs.getValueAt(x), rhs.getKey(), rhs.getValueAt(y)));
                        } else {
                            System.out.println("- Pair discarded for not complying to rules: " + currentLhs + " & " + currentRhs);
                        }
                    }
                }
            }
        }

        return this.availablePairs;
    }

    private boolean pairNotForbidden(final ParameterValue lhs, final ParameterValue rhs) {
        for(Rule rule: this.rules) {
            if(rule.forbidPair()) {
                if(!rule.validateParameters(lhs, rhs)) {
                    return false;
                }
            }
        }
        return true;
    }

    private int getTotalNumberOfPairs() {
        int numberOfPairs = 0;
        final int numberOfParameters = this.options.size();
        for(int i=0; i < numberOfParameters-1; ++i) {
            for(int j=i+1; j < numberOfParameters; ++j) {
                numberOfPairs += this.options.get(i).getNumberOfPossibleValues() * this.options.get(j).getNumberOfPossibleValues();
            }
        }
        return numberOfPairs;
    }

    public int getNumberOfParameterValues() {
        return this.optionValues;
    }

    public int getNumberOfParameters() {
        return this.options.size();
    }

    public void displayInfo() {
        System.out.println("\n============== INFO ==============");
        System.out.println("- There are " + this.options.size() + " parameters/options.");
        System.out.println("- There are " + this.optionValues + " parameter values.");
        System.out.println("- There are " + this.getTotalNumberOfPairs() + " possible pairs (without taking into account rules).");
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

    public List<String> getParameterPositions() {
        List<String> positions = new ArrayList<>();
        for (Option option : this.options) {
            for (int j = 0; j < option.getNumberOfPossibleValues(); ++j) {
                positions.add(option.getKey());
            }
        }
        return positions;
    }

    public List<String> getParameterOrdering() {
        List<String> ordering = new ArrayList<>();
        for (Option option : this.options) {
            ordering.add(option.getKey());
        }
        return ordering;
    }
}
