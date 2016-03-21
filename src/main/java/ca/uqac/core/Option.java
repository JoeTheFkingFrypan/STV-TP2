package ca.uqac.core;

import org.apache.commons.lang3.StringUtils;;

import java.util.ArrayList;
import java.util.List;

public class Option {
    private final String name;
    private final List<String> possibleValues;

    public Option(final String name) {
        this.name = name;
        this.possibleValues = new ArrayList<>();
    }

    public void addPossibleValue(final String value) {

        if(!value.equals("flag")) {
            this.possibleValues.add(value);
        } else {
            this.possibleValues.add(FlagStatus.FLAG_PRESENT.getLabel());
            this.possibleValues.add(FlagStatus.FLAG_ABSENT.getLabel());
        }
    }

    public String getName() {
        return this.name;
    }

    public List<String> getPossibleValues() {
        return this.possibleValues;
    }

    public int getNumberOfPossibleValues() {
        return this.possibleValues.size();
    }

    @Override
    public String toString() {
        return "[OPTION] -" + this.name + "   " +StringUtils.join(this.possibleValues, ", ");
    }
}
