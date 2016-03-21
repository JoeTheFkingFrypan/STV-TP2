package ca.uqac.core;

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

    boolean equals(final String key, final String value) {
        return this.key.equals(key) && this.value.equals(value);
    }
}
