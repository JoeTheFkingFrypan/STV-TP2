package ca.uqac.core;

public class ConstraintValue extends Constraint {

    private final String value;

    public ConstraintValue(final String key, final String value) {
        super(key);
        this.value = value;
    }

    @Override
    public String toString() {
        return this.key + " = " + this.value;
    }
}
