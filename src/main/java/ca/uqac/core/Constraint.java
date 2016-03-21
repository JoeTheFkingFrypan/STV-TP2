package ca.uqac.core;

import ca.uqac.validation.ParameterValue;

public class Constraint {

    private final String key;
    private final String value;

    public Constraint(final String key, final String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return this.key;
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return this.key + " = " + this.value;
    }

    boolean match(final ParameterValue couple) {
        return this.key.equals(couple.getKey()) && this.value.equals(couple.getValue());
    }
}
